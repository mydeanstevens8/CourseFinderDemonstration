package dn.cfind;

/**
 * A physical location describes a location in physical space.
 * 
 * @author deanstevens
 *
 */
public abstract class PhysicalLocation extends Location {
	private static final long serialVersionUID = -1853325471392855325L;

	/**
	 * Gets this location as a GPS coordinate. See {@link #getUncertainty()} for uncertainty measurement.
	 * 
	 * @return Location as GPS value.
	 * @see #getUncertainty()
	 */
	public abstract GPSValue toGPSValue();
	
	/**
	 * Gets the GPS location uncertainty as a double measurement in meters.
	 * 
	 * @return Uncertainty of GPS location in meters.
	 * @see #toGPSValue()
	 */
	public abstract double getUncertainty();
	
	// Score the other location
	@Override
	public double score(Location other) {
		// If we have both physical, we can run this:
		if(other instanceof PhysicalLocation) {
			PhysicalLocation otherPhys = (PhysicalLocation) other;
			// Then based off our GPS loction and uncertainty.
			double gdist = this.toGPSValue().dist(otherPhys.toGPSValue());
			// Uncertainty of distance is defined as the addition of the component uncertainties.
			double combUncertainty = this.getUncertainty() + otherPhys.getUncertainty();
			
			// Use the above to create and return a mathematical hypotenuse style decay for score.
			return Math.min(1, combUncertainty / gdist);
		}
		
		// Otherwise, we wouldn't know.
		return LOCATION_UNKNOWN_SCORE;
	}
}
