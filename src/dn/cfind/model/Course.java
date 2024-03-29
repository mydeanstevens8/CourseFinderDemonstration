package dn.cfind.model;

import java.util.*;

import dn.cfind.Debug;

public class Course extends AbstractModelObject implements HasParent<Campus> {

	private static final long serialVersionUID = -7726549459538737449L;
	private Campus parent;
	
	// Key is the course keyword, value is a double relevance score.
	
	public Course() {
		this("Course", Collections.emptyMap());
	}
	
	public Course(String name) {
		this(name, Collections.emptyMap());
	}
	
	public Course(String name, Map<Keyword, Double> keywords) {
		setName(name);
		this.addKeywords(keywords);
	}
	
	@Override
	public Campus getParent() {
		return parent;
	}
	
	void setParent(Campus parent) {
		this.parent = parent;
	}
	
	public double getTotalRelevance(Keyword kw) {
		Debug.out.println("Computing relevance for "+this);
		
		double campusRelevance = 0;
		double schoolRelevance = 0;
		if(getCampus() != null) {
			campusRelevance = getParent().getRelevance(kw);
			
			School grandparent = getParent().getParent();
			
			if(grandparent != null) {
				schoolRelevance = grandparent.getRelevance(kw);
			}
		}
		double total = Math.max(this.getRelevance(kw), Math.max(campusRelevance, schoolRelevance));
		
		Debug.out.println("Total relevance for " + this + ": "+total);
		
		return total;
	}
	
	public Campus getCampus() {
		return parent;
	}
}
