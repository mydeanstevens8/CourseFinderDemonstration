package dn.cfind.model;

public interface Named {
	public String getName();
	
	public static interface Settable extends Named {
		public void setName(String name);
	}
}
