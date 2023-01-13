package dn.cfind.model;

import java.util.Collection;

public interface HasChildren< // ::: Complex generic declaration :::
		// First type is a container that stores all children, and is expected to
		// contain some collection of a type.
		Container extends Collection<Type>, 
		// The second is the type itself, augmented to make sure that it implements
		// the HasParent interface, with the condition that its given parent type is 
		// this type itself, or a subclass.
		Type extends HasParent<? extends HasChildren<Container, Type>>
		// I.e. Collection of our types, then the type itself with a condition that it 
		// has a parent that is our type.
	> {
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
