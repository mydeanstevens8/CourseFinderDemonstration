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
	
}
