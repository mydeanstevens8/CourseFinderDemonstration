package dn.cfind.tests;

import java.time.Period;
import java.util.*;

import dn.cfind.*;

public class TestData {
	protected Set<Course> courses = Collections.synchronizedSet(new HashSet<Course>());
	protected Set<CourseKeyword> keywords = Collections.synchronizedSet(new HashSet<CourseKeyword>());
	
	// Initialize only by subclasses.
	protected TestData() {
		build();
	}
	
	protected void build() {
		
	}
	
	public void export(CourseCollection c) {
		// Data import auto-manages locations.
		c.importData(courses, keywords, null);
	}
	
	public static class TestData1 extends TestData {
		protected TestData1() {
			
		}
		
		@Override
		protected void build() {
			super.build();
			
			GPSValue akaGPS = new GPSValue(51, 0.1);
			GPSValue belGPS = new GPSValue(51.1, 0.2);
			GPSValue camGPS = new GPSValue(50.8, -0.2);
			GPSValue deltaGPS = new GPSValue(51, -0.1);
			GPSValue errinGPS = new GPSValue(51.3, 0);
			
			Place akaUni = new Place("Aka University", akaGPS);
			Place belUni = new Place("Bel University", belGPS);
			Place camUni = new Place("Camerat University", camGPS);
			Place deltaUni = new Place("Delta University", deltaGPS);
			Place errinUni = new Place("Errin University", errinGPS);
			
			PlaceLocation akaUniPlace = new PlaceLocation(akaUni);
			PlaceLocation belUniPlace = new PlaceLocation(belUni);
			PlaceLocation camUniPlace = new PlaceLocation(camUni);
			PlaceLocation delUniPlace = new PlaceLocation(deltaUni);
			PlaceLocation errUniPlace = new PlaceLocation(errinUni);
			
			Period yp3 = Period.of(3, 0, 0);
			Period yp4 = Period.of(4, 0, 0);
			
			// Here is everything...
			Course akaSci = new Course("Bachelor of Science", "Bachelor of Science at Aka Uni", yp3, akaUniPlace);
			Course akaArt = new Course("Bachelor of Arts", "Bachelor of Arts at Aka Uni", yp3, akaUniPlace);
			Course akaEng = new Course("Bachelor of Engineering", "Bachelor of Engineering at Aka Uni", yp4, akaUniPlace);
			
			Course belSci = new Course("Bachelor of Science", "Bachelor of Science at Bel Uni", yp3, belUniPlace);
			Course belArt = new Course("Bachelor of Arts", "Bachelor of Arts at Bel Uni", yp3, belUniPlace);
			Course belEng = new Course("Bachelor of Engineering", "Bachelor of Engineering at Bel Uni", yp4, belUniPlace);
			
			Course camSci = new Course("Bachelor of Science", "Bachelor of Science at Camerat Uni", yp3, camUniPlace);
			Course camArt = new Course("Bachelor of Arts", "Bachelor of Arts at Camerat Uni", yp3, camUniPlace);
			Course camEng = new Course("Bachelor of Engineering", "Bachelor of Engineering at Camerat Uni", yp4, camUniPlace);
			
			courses.add(akaSci);
			courses.add(akaArt);
			courses.add(akaEng);

			courses.add(belSci);
			courses.add(belArt);
			courses.add(belEng);

			courses.add(camSci);
			courses.add(camArt);
			courses.add(camEng);
		}
	}
	
	private static TestData instance = new TestData1();
	
	public static TestData getTestData() {
		return instance;
	}
}
