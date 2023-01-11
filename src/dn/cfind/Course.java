package dn.cfind;

import java.time.*;
import java.util.*;

/**
 * A Course describes a course that a user can choose, from a set of keywords for the course.
 * 
 * @author deanstevens
 *
 */
/*
 * XXX: I was considering making this class immutable, but due to the possible extended states
 * and extra functionality that could be represented by subclasses, it was not done. If I made
 * this class immutable, I would have had no guarantee that subclasses would also be immutable.
 */
public class Course implements Scorer<Location>, java.io.Serializable {
	private static final long serialVersionUID = -6008413049023996209L;

	/**
	 * The name of the course.
	 */
	private String name;
	
	/**
	 * A description for the course.
	 */
	private String description;
	
	/**
	 * The expected duration of the course, as a Period object.
	 * Internally represented as Years, Months and Days.
	 * 
	 */
	// XXX: May not work in Java SE <1.8. This has been converted from a simple integer representing days.
	private Period duration;
	
	/**
	 * Location of the course.
	 */
	private Location location;
	
	/**
	 * Place aliases.
	 */
	private Set<Place> placeAliases;
	
	/**
	 * Constructs a course with a default name, a {@code null} description and a duration of {@link Period#ZERO}. 
	 */
	public Course() {
		this("Course", null, Period.ZERO);
	}
	
	/**
	 * Constructs a course with the given name, but with a {@code null} description and a duration of {@link Period#ZERO}. 
	 * 
	 * @param name the name of the course
	 */
	public Course(String name) {
		this(name, null, Period.ZERO);
	}

	/**
	 * Constructs a course with the given name and period, but with a {@code null} description.
	 * 
	 * @param name the name of the course 
	 * @param duration the duration of the course as a {@link Period}.
	 */
	public Course(String name, Period duration) {
		this(name, null, duration);
	}

	/**
	 * Constructs a course with the given name, description and period. 
	 * 
	 * @param name the name of the course
	 * @param description the description of the course
	 * @param duration the duration of the course as a {@link Period}.
	 */
	public Course(String name, String description, Period duration) {
		this(name, description, duration, null);
		placeAliases = Collections.synchronizedSet(new HashSet<>());
	}
	
	public Course(String name, String description, Period duration, Location location) {
		setName(name);
		setDescription(description);
		setDuration(duration);
		setLocation(location);
	}

	/**
	 * Returns the name of the course.
	 * 
	 * @return Course name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Changes the course name.
	 * 
	 * @param name new name of the course.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the description of the course.
	 * 
	 * @return Course description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Changes the course description.
	 * 
	 * @param description new description of the course.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Returns the course duration as a {@link Period} object consisting of a representation of years, months and days.
	 * 
	 * @return Course period (i.e. duration).
	 */
	public Period getDuration() {
		return duration;
	}

	/**
	 * Sets the duration of the course, as a {@link Period} object consisting of a year-month-day bundle.
	 * 
	 * @param duration new duration of the course
	 */
	public void setDuration(Period duration) {
		this.duration = duration;
	}
	
	/**
	 * Sets the duration of the course, as a combination of years, months and days.
	 * 
	 * @param years duration in this many years
	 * @param months duration in this many months
	 * @param days duration in this many days.
	 * 
	 * @see Period#of
	 */
	public void setDuration(int years, int months, int days) {
		setDuration(Period.of(years, months, days));
	}

	/**
	 * Gets the location of the course.
	 * 
	 * @return The location of the course.
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Sets the location of the course to a new value.
	 * 
	 * @param location The new location to set.
	 */
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public Place[] getPlaceAliases() {
		return placeAliases.toArray(new Place[placeAliases.size()]);
	}
	
	public boolean addPlaceAlias(Place e) {
		return placeAliases.add(e);
	}
	

	public boolean removePlaceAlias(Place e) {
		return placeAliases.remove(e);
	}
	
	public void clearPlaceAliases() {
		placeAliases.clear();
	}
	
	public double score(Location other) {
		// Return based on our location's comparison.
		return location.score(other);
	}
}
