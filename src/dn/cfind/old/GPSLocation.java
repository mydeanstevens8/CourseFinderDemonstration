package dn.cfind.old;

import dn.cfind.GPSValue;

/**
 * GPS-style location involving latitudinal and longitudinal coordinates. 
 */
@Deprecated
public class GPSLocation extends PhysicalLocation {
	private static final long serialVersionUID = 7630898427300648302L;
	
	private GPSValue position;
	
	public GPSLocation() {
		this(new GPSValue());
	}
	
	public GPSLocation(GPSValue position) {
		this.position = position;
	}
	

	public GPSValue getPosition() {
		return position;
	}



	public void setPosition(GPSValue position) {
		if(position == null) {
			throw new IllegalArgumentException("Position cannot be null");
		}
		this.position = position;
	}


	@Override
	public GPSValue toGPSValue() {
		return getPosition();
	}
	
	@Override
	public double getUncertainty() {
		return 0; // By definition of GPS exact coordinates.
	}
	
	@Override
	public String getName() {
		return "GPS " + position.description();
	}
}
