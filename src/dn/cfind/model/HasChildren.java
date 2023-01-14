package dn.cfind.model;

import java.util.Collection;

// Initially a very complex declaration. Made simpler for better usability.
public interface HasChildren<Container extends Collection<Type>, Type> {
	public Container getChildren();
	
	// Return true if such element is added or removed, false if not.
	public boolean addChild(Type child);
	public boolean removeChild(Type child);

	public void clearChildren();
	
	public default boolean hasChild(Type child) {
		return getChildren().contains(child);
	}
	
	public default boolean addChildren(Collection<Type> children) {
		boolean modified = false;
		for(Type c : children) {
			// Only false when all are false. When one is true,
			// it is set to true for the rest of the loop.
			modified |= addChild(c);
		}
		
		return modified;
	}
}
