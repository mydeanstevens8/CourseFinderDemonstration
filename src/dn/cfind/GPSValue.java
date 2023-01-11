package dn.cfind;

import dn.cfind.math.*;

/**
 * A GPS longitudinal and latitudinal style point descriptor.
 * 
 * @author deanstevens
 *
 */
public final class GPSValue implements java.io.Serializable {
	private static final long serialVersionUID = -3975427220240595121L;
	
	// Our calculations are based on a spherical calculation, as an approximation to the true
	// oblate ellipsoid that is the Earth.
	// All of these measurements are in meters.
	public static final double EQUATORIAL_RADIUS = 6378137.0;
	public static final double POLAR_RADIUS = 6356752.314245;
	
	public static final double APPROX_RADIUS = 6371000.0;
	
	
	/**
	 * Latitude, in <i>degrees</i>. 
	 */
	private final double latitude;

	/**
	 * Longitude, in <i>degrees</i>. 
	 */
	private final double longitude;
	
	public GPSValue() {
		this(0, 0);
	}
	
	/**
	 * Constructs a GPSValue with the specified latitude and longitude, both in degrees.
	 */
	public GPSValue(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	// Use Vectors to calculate location on earth. We reference +Z to be at 0 longitude, and XZ to be the equatorial plane,
	// right-handed with Y up and positive X going east from starting 0 deg long.
	private static double greatCircleDistance(GPSValue a, GPSValue b) {
		// Convert deg to rad
		double alat = a.getLatitudeRad();
		double along = a.getLongitudeRad();
		
		double blat = b.getLatitudeRad();
		double blong = b.getLongitudeRad();
		
		// Represent as direction vectors, right-handed, with +z towards the camera and +y up.
		Vector3 avd = new Vector3(Math.sin(along), Math.sin(alat), Math.cos(along));
		Vector3 bvd = new Vector3(Math.sin(blong), Math.sin(blat), Math.cos(blong));
		
		// Use the angle (in radians) between the two vectors to shorthand a computation.
		double angle = avd.angle(bvd);
		
		// Use the angle to compute the spherical (approxiate only) distance.
		// Thankfully, angle is already in radians, so this is just a direct multiply by the radius itself.
		// The full ellipsoid formula would involve calculating the arc-length of an ellipse using calculus.
		// Calculating the arc-length of an ellipse has no closed-form solution, and requires analysis via
		// elliptic curves. Numerical approximation may be possible, however.
		return angle * APPROX_RADIUS;
	}
	
	private GPSValue greatCircleMove(double northMeters, double eastMeters) {
		return new GPSValue(
				this.latitude + Math.toDegrees(northMeters / APPROX_RADIUS), 
				// For the longitude we need to adjust by the latitude value to compensate for reduced longitude range
				// when at high latitude.
				this.longitude + (Math.toDegrees(eastMeters / APPROX_RADIUS) / Math.cos(this.getLatitudeRad()))
			).normalized();
	}
	
	public double dist(GPSValue other) {
		return greatCircleDistance(this, other);
	}
	
	/**
	 * Returns a new GPS coordinate describing the position after moving by the specified north and east displacement.
	 * 
	 * @param northMeters The amount of meters to move north from the current position.
	 * @param eastMeters The amount of meters to move east from the current position.
	 * @return A new GPS value coordinate describing the new position after moving from this coordinate.
	 */
	public GPSValue move(double northMeters, double eastMeters) {
		return greatCircleMove(northMeters, eastMeters);
	}
	
	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}
	
	public double getLatitudeRad() {
		return Math.toRadians(latitude);
	}

	public double getLongitudeRad() {
		return Math.toRadians(longitude);
	}
	
	public GPSValue normalized() {
		// In degrees.
		double boundedLat = getLatitude();
		double boundedLong = getLongitude();
		
		// Both 90 and -90 must be included to represent poles.
		while(boundedLat < -90 || boundedLat > 90) {
			// Reverse latitude.
			// When angle > 90 deg: Starting at 90 deg and going down to 0 when going towards 180 deg lat.
			// When angle < -90 deg: Starting at -90 deg and going up to 0 when going towards -180 deg lat.
			boundedLat = 180*Math.signum(boundedLat) - boundedLat;
			
			// Longitude must reverse when this happens. Whichever direction doesn't matter,
			// but to reduce future loop iterations we can do a sig-num of lat (consequently increasing 
			// overhead here).
			boundedLong = Math.signum(boundedLat)*180;
		}
		
		// Include the negative -180 by convention. 
		// Both -180 and 180 represent the same line.
		while(boundedLong < -180) {
			boundedLong += 360;
		}
		while(boundedLong >= 180) {
			boundedLong -= 360;
		}

		assert (-90 <= boundedLat && boundedLat <= 90): "Latitude out of range: " + boundedLat;
		assert (-180 <= boundedLong && boundedLong < 180): "Longitude out of range: " + boundedLong;
		
		return new GPSValue(boundedLat, boundedLong);
	}
	
	// XXX: Non-localized description
	public String description() {
		String latStr = Math.abs(latitude) + (latitude >= 0? "N" : "S");
		String longStr = Math.abs(longitude) + (longitude >= 0? "E" : "W");
		
		return latStr + ", " + longStr;
	}
}
