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
			return (this.getName().equals(otherk.getName())) && (this.getCategory().equals(otherk.getCategory()));
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

}
