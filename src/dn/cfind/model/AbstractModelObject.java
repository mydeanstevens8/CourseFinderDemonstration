package dn.cfind.model;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

@ModelObject
public abstract class AbstractModelObject implements java.io.Serializable, Keywordable, Named.Settable {
	private static final long serialVersionUID = -6426989652706888674L;
	
	private String name;
	private final Map<Keyword, Double> keywords;
	
	public AbstractModelObject() {
		this.keywords = new Hashtable<>();
	}

	@Override
	public Map<Keyword, Double> getKeywords() {
		return Collections.unmodifiableMap(keywords);
	}
	
	// Return true if the key was successfully added from a null.
	@Override
	public boolean addKeyword(Keyword keyword, double relevance) {
		if(relevance < 0 || relevance > 1) 
			throw new IllegalArgumentException("Relevance must be between 0 and 1");
		
		// Returns the last value for the key - if null then we added a keyword.
		return keywords.put(keyword, relevance) == null;
	}
	
	// Return true if the key was initially present and successfully removed.
	@Override
	public boolean removeKeyword(Keyword keyword) {
		// If the last value is not null, then the key existed.
		return keywords.remove(keyword) != null;
	}

	@Override
	public void removeAllKeywords() {
		keywords.clear();
	}

	@Override
	public final String getName() {
		return name;
	}
	
	@Override
	public final void setName(String name) {
		this.name = name;
	}
	
	@Override
	public double getRelevance(Keyword keyword) {
		// Find the maximum score from our list of keywords.
		double maximumScore = Double.NEGATIVE_INFINITY; // Fail-safe for negatives.
		
		for(Map.Entry<Keyword, Double> ours : keywords.entrySet()) {
			// Computed score is the relevance contribution of the keyword multiplied by the
			// score between the given keyword and the keyword being compared.
			// Minimum 0 and maximum 1.
			double computedScore = ours.getValue() * ours.getKey().matchScore(keyword);
			maximumScore = Math.max(maximumScore, computedScore);
		}
		
		return maximumScore;
	}
	
	@Override
	public void addKeywords(Map<Keyword, Double> keywords) {
		this.keywords.putAll(keywords);
	}
}
