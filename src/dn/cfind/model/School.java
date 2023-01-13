package dn.cfind.model;

import java.util.*;

// We advise that subclasses of this class be immutable.
@ModelObject
public class School extends AbstractModelObject 
	implements HasChildren<Set<Campus>, Campus>, HasParent<FinderSystem> {

	private static final long serialVersionUID = -5051908614554459288L;
	
	// "Children" of our school - the campuses.
	private final Set<Campus> children;
	
	private FinderSystem parent;

	public School() {
		this("School", Collections.emptySet());
	}
	
	public School(String name) {
		this(name, Collections.emptySet());
	}
	
	public School(String name, Collection<Campus> campuses) {
		setName(name);
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
	
	public FinderSystem getParent() {
		return parent;
	}
	
	void setParent(FinderSystem parent) {
		this.parent = parent;
	}
}

