package dn.cfind.model;

import dn.cfind.Debug;

// Advised to be immutable.
public final class Keyword implements ModelObject, Named, java.io.Serializable {
	private static final long serialVersionUID = 1538581074252626583L;

	public static enum Category {
		GENERAL("General"),
		NAME("Name"),
		HOBBY("Hobby"),
		PLACE("Place");
		
		public final String description;
		private Category(String description) {
			this.description = description;
		}
	}
	
	private final String name;
	private final Category category;
	
	public Keyword(String name) {
		this(name, Category.GENERAL);
	}
	
	public Keyword(String name, Category category) {
		this.name = name;
		this.category = category;
	}
	
	@Override
	public final String getName() {
		return name;
	}
	
	public final Category getCategory() {
		return category;
	}
	
	@Override
	public boolean equals(Object other) {
		if((other != null) && (other instanceof Keyword)) {
			// A course keyword type, compare it.
			Keyword otherk = (Keyword) other;
			
			// True if and only if both category and name are equal.
			boolean result = 
					(this.getName().equals(otherk.getName())) && 
					(this.getCategory().equals(otherk.getCategory()));
			
			if(result) {
				assert this.hashCode() == other.hashCode(): "Hash codes wrong for true equality!";
			}
			
			return result;
		}
		// Else we are either null or the wrong instance type.
		else return false;
	}
	
	@Override 
	public int hashCode() {
		// Per contract of the equality method above.
		// TODO: Is signed integer overflow well defined in Java?
		return this.getName().hashCode() * this.getCategory().ordinal();
	}
	
	// If it were not for the categories, then Keywords would
	// be comparable with each other.

	// A score from 0 to 1 describing how well the keyword matches with the other
	public double matchScore(Keyword other) {
		if((other.getCategory() != Keyword.Category.GENERAL) && (this.getCategory() != other.getCategory())) {
			Debug.out.println("\t\tIncompatible categories: my "+this.getCategory() + " vs. your "+ other.getCategory());
			return 0;  // Different categories are incompatible, except with the GENERAL category.
		}
		
		// Non-case sensitive.
		String tn = this.getName().toLowerCase();
		String on = other.getName().toLowerCase();
		
		// Perform some search.
		int diffs = performSearch(tn, on);
		
		Debug.out.println("\t\tFinal diff score: "+diffs);
		
		// The more differences we have, the lower our score.
		// TODO: Abstract the scoring function.
		double result = Math.pow(1.0/2.0, diffs);
		
		assert (result >= 0 && result <= 1): "Result out of range!";
		
		return result;
	}
	
	private static int performSearch(String tn, String on) {
		// We wish to find a string in another - Ukkonnen's algorithm can help us!
		// Unfortunately, it is very complicated and could be attempted another time.
		
		// Execute only when length is not zero.
		int diffs = Math.min(tn.length(), on.length());
		
		for(int i = 0; i < tn.length(); i++) {
			// Measure local differences. Keywords must match contiguously.
			int localDiffs = Math.min(tn.length(), on.length());
			for(int j = 0; (j < on.length()) && (i+j) < tn.length(); j++) {
				if(tn.charAt(i+j) == on.charAt(j)) {
					localDiffs--;
				}
			}
			
			diffs = Math.min(localDiffs, diffs);
		}
		
		return diffs;
	}
	
	public static double matchScore(Keyword k1, Keyword k2) {
		return k1.matchScore(k2);
	}
}
