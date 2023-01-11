package dn.cfind;

public class PlaceLocation extends PhysicalLocation {
	private static final long serialVersionUID = 4525335206247879048L;
	
	private Place place;
	
	public PlaceLocation() {
		this(new Place());
	}
	
	public PlaceLocation(Place place) {
		this.place = place;
	}

	@Override
	public GPSValue toGPSValue() {
		return place.getGPSLocation();
	}

	@Override
	public double getUncertainty() {
		return place.getUncertianty();
	}

	@Override
	public String getName() {
		return place.getName();
	}

	public Place getPlace() {
		return place;
	}

	public void setPlace(Place place) {
		if(place == null) {
			throw new IllegalArgumentException("Place cannot be null");
		}
		this.place = place;
	}

}
