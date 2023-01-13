package dn.cfind.model;

import java.util.*;

// We advise that subclasses of this class be immutable.
@ModelObject
public class School implements java.io.Serializable, Named.Settable, HasChildren<Set<Campus>, Campus> {

	private static final long serialVersionUID = -5051908614554459288L;
	
	private  String name;
	// "Children" of our school - the campuses.
	private final Set<Campus> children;

	public School() {
		this("School", Collections.emptySet());
	}
	
	public School(String name) {
		this(name, Collections.emptySet());
	}
	
	public School(String name, Collection<Campus> campuses) {
		this.name = name;
		// Synchronize to prevent concurrent modification.
		this.children = Collections.synchronizedSet(new HashSet<>(campuses));
	}

	@Override
	public Set<Campus> getChildren() {
		return Collections.unmodifiableSet(children);
	}

	@Override
	public boolean addChild(Campus child) {
		if(children.add(child)) {
			child.setParent(this);
			return true;
		}
		else {
			return false;
		}
	}
	
	@Override
	public boolean removeChild(Campus child) {
		if(children.remove(child)) {
			child.setParent(null);
			return true;
		}
		else {
			return false;
		}
	}
	
	@Override
	public void clearChildren() {
		children.clear();
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}
}

