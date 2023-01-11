package dn.cfind;


public class VirtualLocation extends Location {
	private static final long serialVersionUID = 8569827092895855954L;

	@Override
	public String getName() {
		return "Online";
	}
	
	@Override
	public double score(Location other) {
		// When virtual, we always give an unknown score.
		return LOCATION_UNKNOWN_SCORE;
	}
}
