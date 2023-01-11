package dn.cfind.math;

// Application of quaternions to rotations here.
// Assumes the quaternion it works with are unit quaternions.
public final class Rotator implements java.io.Serializable {
	private static final long serialVersionUID = -8775705978442104577L;
	
	// The reference quaternion to rotate something by.
	private final Quaternion ref;
	
	public Rotator() {
		this(Quaternion.IDENTITY);
	}
	
	public Rotator(Quaternion q) {
		// Always convert to a unit quaternion.
		this.ref = q.unit();
	}
	
	public Rotator(double angle, Vector3 axis) {
		this(Quaternion.fromAngleAxis(angle, axis));
	}
	
	public Rotator(Vector3 euler) {
		this(Quaternion.fromEuler(euler));
	}
	
	public static Rotator fromQuaternion(Quaternion q) {
		return new Rotator(q);
	}
	
	public static Rotator fromAngleAxis(double angle, Vector3 axis) {
		return new Rotator(angle, axis);
	}
	
	public static Rotator fromEuler(Vector3 euler) {
		return new Rotator(euler);
	}
	
	private static Quaternion vecToQuat(Vector3 v) {
		return new Quaternion(0, v.x, v.y, v.z);
	}
	
	public Vector3 vectorPart() {
		return ref.getVectorPart();
	}
	
	public double scalarPart() {
		return ref.w;
	}
	
	// Rotate from the origin a vector by this quaternion.
	// What an elegant function.
	public Vector3 applyTo(Vector3 v) {
		// Using the formula q * p * q^(-1), where q is this quaternion, and p is
		// the point to rotate. Only work with unit quaternions.
		return ref.mul(vecToQuat(v)).mul(ref.rev()).getVectorPart();
	}
	
	public Vector3 apply(Vector3 v) {
		return applyTo(v);
	}
	
	public Vector3 axis() {
		return vectorPart().unit();
	}
	
	public double angle() {
		return 2*Math.atan2(vectorPart().mag(), scalarPart());
	}
	
	public Quaternion toQuat() {
		return ref;
	}
	
	// Composes the two rotations.
	public Rotator compose(Rotator next) {
		return new Rotator(toQuat().mul(next.toQuat()));
	}
}