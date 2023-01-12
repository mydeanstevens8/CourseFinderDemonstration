package dn.cfind;

import java.util.*;

public class CourseCollection implements java.io.Serializable{
	private static final long serialVersionUID = 5696940639305313304L;
	
	protected Set<Course> courses = createSyncSet();
	protected Set<Location> locations = createSyncSet();
	protected Set<CourseKeyword> keywords = createSyncSet();
	
	public CourseCollection() {
		
	}
	
	private <T> Set<T> createSyncSet() {
		return Collections.synchronizedSet(new HashSet<T>());
	}
	
	public void importData(
			Collection<Course> courses, 
			Collection<CourseKeyword> keywords, 
			Collection<Location> locations
			) {
		// We allow null fields in imports. Selectively
		// import the non-null ones.
		if(courses != null) this.courses.addAll(courses);
		if(keywords != null) this.keywords.addAll(keywords);
		if(locations != null) this.locations.addAll(locations);
		
		// Add any courses we find in keywords to the courses list.
		for(CourseKeyword kw : this.keywords) {
			this.courses.addAll(Arrays.asList(kw.getReferences()));
		}
		
		// Add any locations we find in courses to the locations list.
		for(Course c : this.courses) {
			this.locations.add(c.getLocation());
		}
	}
	
	public Set<Course> getCourseSet() {
		return Collections.unmodifiableSet(courses);
	}

	public Set<Location> getLocationSet() {
		return Collections.unmodifiableSet(locations);
	}

	public Set<CourseKeyword> getKeywordSet() {
		return Collections.unmodifiableSet(keywords);
	}
	
	public boolean addCourse(Course c) {
		return courses.add(c);
	}
	
	public boolean addKeyword(CourseKeyword kw) {
		return keywords.add(kw);
	}
	
	public boolean addLocation(Location loc) {
		return locations.add(loc);
	}
	
	public boolean removeCourse(Course c) {
		return courses.remove(c);
	}
	
	public boolean removeKeyword(CourseKeyword kw) {
		return keywords.remove(kw);
	}
	
	public boolean removeLocation(Location loc) {
		return locations.remove(loc);
	}
	
	// Key controller methods!
	
	public Hashtable<Course, Double> keywordScores(String keyword) {
		// Based upon keywords and the course itself.
		Hashtable<Course, Double> scoreTable = new Hashtable<Course, Double>();
		
		for(Course c : courses) {
			// Init the table.
			scoreTable.put(c, 0.0);
		}
		
		// Compare the keyword to every member in the set.
		// This is costly - a speed up would be to create a bi-directional association, which increases
		// complexity but saves time. (Complexity bounded by O(n*m), n being the number of keywords. m 
		// being the maximum number of courses per keyword)
		for(CourseKeyword kw : keywords) {
			// Score the keyword with each respective keyword in the database.
			double localScore = kw.score(keyword);
			
			// Then for each course that the keyword applies to, apply the score.
			for(Course appliesTo : kw.getReferences()) {
				// Use addition to the double element to increase the score.
				scoreTable.put(appliesTo, scoreTable.get(appliesTo) + localScore);
			}
		}
		
		return scoreTable;
	}
	
	public Hashtable<Course, Double> locationScores(String location) {
		Hashtable<Course, Double> scoreTable = new Hashtable<Course, Double>();
		

		for(Course c : courses) {
			// Init the table with zeros.
			Location loc = c.getLocation();
			scoreTable.put(c, loc.score(location));
		}
		
		return scoreTable;
	}
	
	// Could do with more flexibility later.
	public List<Course> provideCourseArrangement(String keyword, boolean locationMode, 
			boolean reversed, double scoreThreshold) {
		List<Map.Entry<Course, Double>> scoredList = new Vector<>();
		
		Hashtable<Course, Double> scoreTable;
		
		if(locationMode) {
			scoreTable = locationScores(keyword);
		}
		else {
			scoreTable = keywordScores(keyword);
		}
		
		// Add into list as an entry set
		scoredList.addAll(scoreTable.entrySet());
		
		// Sort based on entry set value
		Collections.sort(scoredList, Map.Entry.comparingByValue());
		
		// Then, split the entry up and finish.
		if(!reversed) Collections.reverse(scoredList);
		
		List<Course> sortedList = new Vector<>();
		
		for(Map.Entry<Course, Double> e1 : scoredList) {
			// Delete any below the score threshold.
			if(e1.getValue() >= scoreThreshold) {
				sortedList.add(e1.getKey());
			}
		}
		
		return sortedList;
	}
	
	public List<Course> provideCourseArrangement(String keyword, boolean locationMode) {
		return provideCourseArrangement(keyword, locationMode, false, 0.01);
	}
}
