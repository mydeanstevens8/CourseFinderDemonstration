package dn.cfind.old;

import java.util.*;

/*
 * The following class describes the category that a CourseKeyword falls into.
 * To represent such categories, several options are available.
 * 
 * The first option is to use an enumeration to represent categories.
 * 
 * - Enumerations are an excellent way to represent such categories, as we can
 *   inject methods into enumerations that allow us to display them in programs
 *   and even perform basic meta-categorization of our categories. But, they have
 *   one major flaw:
 * - A major disadvantage of enumerations is that they are not indirectly 
 *   extensible, which limits us from representing other additional categories 
 *   when they are defined in subclasses.
 *   
 * To address the above problem, we can use classes to represent categories.
 * 
 * - Classes share many of the advantages of enumerations above, while solving the
 *   problem of extensibility and inheritance.
 * - But the issue of extensibility could be solved further. It would be tedious
 *   to create new categories through keywords, as we would have to construct the
 *   objects ourselves, or otherwise have some sort of factory to link up the
 *   instances of categories together.
 * - Nonetheless, if a factory is employed, declaring a new category could conceivably
 *   be simple for trivial cases, while extending power as needed.
 * - But the issue with a factory system is that such a system would be expensive,
 *   and would need tons of representation inside an SQL database.
 * 
 * A third way is to use simple strings to define categories.
 * 
 * - Strings are very easy to extend with new categories, and are very efficient to
 *   use and store in programs and databases.
 * - But unfortunately, they are hard to abstract and extend functionality, as they
 *   are quite primitive in nature.
 * 
 * For the above reasons, and since we wish to ensure extensibility and abstraction 
 * of our framework, I chose to use a id and content model to describe categories.
 * 
 * In effect, each category consists of a set of strings that represent the category.
 * One string will be the default name, and the others are sets of aliases.
 */
@Deprecated
public final class KeywordCategory implements java.io.Serializable{
	private static final long serialVersionUID = 6730312431827614563L;

	// Since this string is final, it can't be modified, and as such no setters can be 
	// defined. Perhaps it may be possible for this field to be public, but it has not
	// yet been done.
	private final String defaultString;
	
	private final Set<String> aliases;
	
	// Varargs covers no-alias cases.
	public KeywordCategory(String defaultString, String... aliases) {
		this(defaultString, Arrays.asList(aliases));
	}
	
	public KeywordCategory(String defaultString, Collection<String> aliases) {
		this.defaultString = defaultString;
		
		// Hash set is not synchronized, but it does not matter since we wish to
		// make our aliases set immutable anyway, preventing the dreaded concurrent
		// modifications from happening.
		this.aliases = Collections.unmodifiableSet(new HashSet<String>(aliases));
	}
	
	public String[] getAliases() {
		return aliases.toArray(new String[aliases.size()]);
	}
	
	public String getDefault() {
		return defaultString;
	}
}