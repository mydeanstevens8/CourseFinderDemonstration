package dn.cfind.model;

import java.util.*;

public interface Keywordable {
	// The double is a range from 0 to 1 for relevance.
	public Map<Keyword, Double> getKeywords();
	
	// Return true if the key was successfully added from a null.
	public boolean addKeyword(Keyword keyword, double relevance);
	
	// Return true if the key was initially present and successfully removed.
	public boolean removeKeyword(Keyword keyword);
	
	public void removeAllKeywords();
	
	
	public default void addKeywords(Map<Keyword, Double> keywords) {
		for(Map.Entry<Keyword, Double> keyword : keywords.entrySet()) {
			addKeyword(keyword.getKey(), keyword.getValue());
		}
	}
	
	// Get the relevance of a particular keyword for this object.
	// At least 0, and no more than 1.
	public double getRelevance(Keyword keyword);
	
	public default Keyword getSelfKeyword() {
		// May be null for no such self keyword.
		return null;
	}
}
