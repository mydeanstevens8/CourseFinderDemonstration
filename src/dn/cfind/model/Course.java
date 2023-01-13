package dn.cfind.model;

import java.util.*;

@ModelObject
public class Course implements java.io.Serializable, HasParent<Campus>, Named.Settable {

	private static final long serialVersionUID = -7726549459538737449L;

	private String name;
	private Campus parent;
	
	// Key is the course keyword, value is a double relevance score.
	private final Map<Keyword, Double> keywords;
	
	public Course() {
		this("Course", Collections.emptyMap());
	}
	
	public Course(String name) {
		this(name, Collections.emptyMap());
	}
	
	public Course(String name, Map<Keyword, Double> keywords) {
		this.name = name;
		this.keywords = new Hashtable<>(keywords);
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public Campus getParent() {
		return parent;
	}
	
	void setParent(Campus parent) {
		this.parent = parent;
	}
	
	public Map<Keyword, Double> getKeywords() {
		return Collections.unmodifiableMap(keywords);
	}
	
	// Return true if the key was successfully added from a null.
	public boolean addKeyword(Keyword keyword, double relevance) {
		// Returns the last value for the key - if null then we added a keyword.
		return keywords.put(keyword, relevance) == null;
	}
	
	// Return true if the key was initially present and successfully removed.
	public boolean removeKeyword(Keyword keyword) {
		// If the last value is not null, then the key existed.
		return keywords.remove(keyword) != null;
	}
	
	public void removeAllKeywords() {
		keywords.clear();
	}
}
