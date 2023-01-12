package dn.cfind;

import java.util.*;

public class CourseKeyword implements Scorer<CourseKeyword>, Keyword<Course>, java.io.Serializable {
	private static final long serialVersionUID = 6072914850210484261L;

	// Required.
	private final String keyword;
	
	// May be null.
	private final KeywordCategory category;
	
	// Required.
	private final Set<Course> references;
	
	public CourseKeyword() {
		// Invalid object...
		this("keyword", (KeywordCategory) null);
	}
	
	public CourseKeyword(String keyword, Course... references) {
		this(keyword, null, references);
	}

	public CourseKeyword(String keyword, KeywordCategory category, Course... references) {
		this.keyword = keyword;
		this.category = category;
		this.references = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(references)));
	}
	
	@Override
	public String getKeyword() {
		return keyword;
	}

	@Override
	public KeywordCategory getCategory() {
		return category;
	}

	@Override
	public Course[] getReferences() {
		return references.toArray(new Course[references.size()]);
	}

	@Override
	public double score(CourseKeyword other) {
		// Return a comparison between this keyword and the other.
		return unitDistanceScore(this.keyword, other.keyword);
	}
	
	public double score(String keyword) {
		// Return a comparison between this keyword and the other.
		return unitDistanceScore(this.keyword, keyword);
	}
	
	// Well specified unit distance.
	static int unitDistance(String s1, String s2) {
		int bound = Math.min(s1.length(), s2.length());
		int maxG = Math.max(s1.length(), s2.length());
		
		// When all characters are equal, and length is the same, distance is zero.
		// For each different character, or unit length difference, increment the score
		// by 1. (Regardless of actual code point values)
		int distanceScore = 0;
		for(int i = 0; i < bound; i++) {
			if(s1.codePointAt(i) != s2.codePointAt(i)) {
				distanceScore++;
			}
		}
		
		distanceScore += (maxG - bound);
		
		return distanceScore;
	}
	
	static double unitDistanceScore(String s1, String s2) {
		// Exp-based.
		return Math.exp(-unitDistance(s1, s2));
	}
}