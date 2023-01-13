package dn.cfind.model;

import java.util.*;

// We advise that subclasses of this class be immutable.
@ModelObject
public class Campus implements java.io.Serializable, HasParent<School>, Named.Settable, HasChildren<Set<Course>, Course> {

	private static final long serialVersionUID = -6357650039215274741L;
	
	protected String name;
	private final Set<Course> children;
	
	// Bi-directional associations help to speed things up, but they
	// are harder to maintain.
	private School parent;
	
	public Campus() {
		this("Campus", Collections.emptySet());
	}
	
	public Campus(String name) {
		this(name, Collections.emptySet());
	}
	
	public Campus(String name, Collection<Course> courses) {
		this.name = name;
		// Synchronize to prevent concurrent modification.
		this.children = Collections.synchronizedSet(new HashSet<>(courses));
	}
	
	@Override
	public Set<Course> getChildren() {
		return Collections.unmodifiableSet(children);
	}

	@Override
	public boolean addChild(Course child) {
		if(children.add(child)) {
			child.setParent(this);
			return true;
		}
		else {
			return false;
		}
	}
	
	@Override
	public boolean removeChild(Course child) {
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
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public School getParent() {
		return parent;
	}
	
	void setParent(School parent) {
		this.parent = parent;
	}
}
