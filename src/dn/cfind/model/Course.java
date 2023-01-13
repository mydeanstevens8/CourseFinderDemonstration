package dn.cfind.model;

import java.util.*;

@ModelObject
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
		double campusRelevance = 0;
		double schoolRelevance = 0;
		if(getCampus() != null) {
			campusRelevance = getParent().getRelevance(kw);
			
			School grandparent = getParent().getParent();
			
			if(grandparent != null) {
				schoolRelevance = grandparent.getRelevance(kw);
			}
		}
		return Math.max(this.getRelevance(kw), Math.max(campusRelevance, schoolRelevance));
	}
	
	public Campus getCampus() {
		return parent;
	}
}
