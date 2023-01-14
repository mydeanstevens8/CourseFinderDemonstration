package dn.cfind.old;

// Initially I designed keywords only for courses, but due to the extended functionality
// that I see with Keywords and their use, I abstracted the Keywords in order to accommodate 
// multiple applications - in the form of an interface here.
@Deprecated
public interface Keyword<Reference> {
	
	public String getKeyword();
	
	public KeywordCategory getCategory();
	
	public Reference[] getReferences();
}
