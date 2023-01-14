package dn.cfind.old;

/**
 * <p>
 * A location describes a place or a range of places where a course can be held.
 * </p>
 * 
 * @author deanstevens
 */
/*
 * XXX: Debating whether or not this should be an interface is shown in
 * https://www.baeldung.com/java-interface-vs-abstract-class
 * 
 * In my opinion, since our subclasses of Location describe a Location, rather than
 * Location merely describing objects that can be Located, (covered separately in a
 * hypothetical Locatable interface), this should be an abstract class instead of an
 * interface.
 */
@Deprecated
public abstract class Location implements Scorer<Location>, java.io.Serializable{
	private static final long serialVersionUID = 4973856784001602516L;
	
	protected static final double LOCATION_UNKNOWN_SCORE = 1;

	public String getName() {
		return "Location";
	}
	
	public double score(String keyword) {
		// Depends on unit distance
		return CourseKeyword.unitDistanceScore(getName(), keyword);
	}
}
