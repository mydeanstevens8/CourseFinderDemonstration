package dn.cfind.model;

import java.util.*;

// We advise that subclasses of this class be immutable.
@ModelObject
public class Campus extends AbstractModelObject 
	implements HasParent<School>, HasChildren<Set<Course>, Course> {

	private static final long serialVersionUID = -6357650039215274741L;
	
	private final Set<Course> children;
	
	// Bi-directional associations help to speed things up, but they
	// are harder to maintain. Here we place access control restrictions to
	// make it easier for us.
	private School parent;
	
	public Campus() {
		this("Campus", Collections.emptySet());
	}
	
	public Campus(String name) {
		this(name, Collections.emptySet());
	}
	
	public Campus(String name, Collection<Course> courses) {
		setName(name);
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
	
	@Override
	public School getParent() {
		return parent;
	}
	
	void setParent(School parent) {
		this.parent = parent;
	}
	
	public School getSchool() {
		return parent;
	}
}
