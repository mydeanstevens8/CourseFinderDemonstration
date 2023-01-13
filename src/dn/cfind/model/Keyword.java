package dn.cfind.model;

@ModelObject
// Advised to be immutable.
public final class Keyword implements Named, java.io.Serializable {
	private static final long serialVersionUID = 1538581074252626583L;

	public static enum Category {
		GENERAL("General"),
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
		if(this.getCategory() != other.getCategory()) {
			return 0;  // Different categories are incompatible.
		}
		
		String tn = this.getName();
		String on = other.getName();
		
		int diffs = 0;
		
		for(int i = 0; i < Math.min(tn.length(), on.length()); i++) {
			// Measure differences here
			if(tn.charAt(i) != on.charAt(i)) diffs++;
		}
		
		// The more differences we have, the lower our score.
		// TODO: Abstract the scoring function.
		double result = Math.pow(1/2, diffs);
		
		assert (result >= 0 && result <= 1): "Result out of range!";
		
		return result;
	}
	
	public static double matchScore(Keyword k1, Keyword k2) {
		return k1.matchScore(k2);
	}
}
