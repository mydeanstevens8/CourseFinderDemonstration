package dn.cfind;

public class Place implements java.io.Serializable{
	private static final long serialVersionUID = -7802924797338907397L;

	private final String name;

	private final GPSValue pointLocation;
	private final double uncertianty;
	
	public Place() {
		this("Place", new GPSValue(), 0);
	}
	
	public Place(String name) {
		this(name, new GPSValue(), 0);
	}
	
	public Place(String name, GPSValue pointLocation) {
		this(name, pointLocation, 0);
	}
	
	public Place(String name, GPSValue pointLocation, double uncertainty) {
		this.name = name;
		
		this.pointLocation = pointLocation;
		this.uncertianty = uncertainty;
	}

	public String getName() {
		return name;
	}

	public GPSValue getGPSLocation() {
		return pointLocation;
	}

	public double getUncertianty() {
		return uncertianty;
	}
}
