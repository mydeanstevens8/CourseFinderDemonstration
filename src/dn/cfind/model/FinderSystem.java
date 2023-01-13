package dn.cfind.model;

import java.util.*;

public class FinderSystem implements HasChildren<Set<School>, School>, java.io.Serializable{
	// Can be saved using a serial UID
	private static final long serialVersionUID = -7736092820347840766L;
	
	// Store only this list as a serial
	private final Set<School> children;
	
	public FinderSystem() {
		this.children = Collections.synchronizedSet(new HashSet<School>());
	}

	@Override
	public Set<School> getChildren() {
		return Collections.unmodifiableSet(children);
	}
	
	@Override
	public boolean addChild(School child) {
		if(children.add(child)) {
			child.setParent(this);
			return true;
		}
		else {
			return false;
		}
	}
	
	@Override
	public boolean removeChild(School child) {
		if(children.remove(child)) {
			child.setParent(null);
			return true;
		}
		else {
			return false;
		}
	}
	
	@Override
	public void clearChildren() {
		children.clear();
	}
	
	
	// Helper methods
	public Set<School> getSchools() {
		return getChildren();
	}
	
	public Set<Campus> getCampuses() {
		Set<Campus> campuses = new HashSet<>();
		
		for(School s : getSchools()) {
			campuses.addAll(s.getChildren());
		}
		
		return Collections.unmodifiableSet(campuses);
	}
	
	public Set<Course> getCourses() {
		Set<Course> courses = new HashSet<>();
		
		for(Campus c : getCampuses()) {
			courses.addAll(c.getChildren());
		}
		
		return Collections.unmodifiableSet(courses);
	}
	
	public Map<Course, Map<Keyword, Double>> getAllCourseKeywords() {
		Set<Course> courses = getCourses();
		
		Map<Course, Map<Keyword, Double>> keywords = new HashMap<>();
		
		for(Course c : courses) {
			keywords.put(c, c.getKeywords());
		}
		
		return Collections.unmodifiableMap(keywords);
	}
	
	public Map<Campus, Map<Keyword, Double>> getAllCampusKeywords() {
		Set<Campus> campuses = getCampuses();
		
		Map<Campus, Map<Keyword, Double>> keywords = new HashMap<>();
		
		for(Campus c : campuses) {
			keywords.put(c, c.getKeywords());
		}
		
		return Collections.unmodifiableMap(keywords);
	}
	
	public Map<Course, Double> getCourseRelevanceMap(Keyword kw) {
		Map<Course, Double> courseMap = new Hashtable<Course, Double>();
		
		for(Course c : getCourses()) {
			double rel = c.getTotalRelevance(kw);
			courseMap.put(c, rel);
		}
		
		return courseMap;
	}
	
	public List<Course> getRelevantCourses(Keyword kw) {
		Map<Course, Double> courseMap = getCourseRelevanceMap(kw);
		
		List<Map.Entry<Course, Double>> sortedMap = new Vector<>(courseMap.entrySet());
		// Sort with 1 at the top and 0 at the bottom.
		Collections.sort(sortedMap, Collections.reverseOrder(Map.Entry.comparingByValue()));

		List<Course> sortedList = new Vector<>();
		
		// Hand-map
		for(Map.Entry<Course, Double> entry : sortedMap) {
			sortedList.add(entry.getKey());
		}
		
		return sortedList;
	}
}
