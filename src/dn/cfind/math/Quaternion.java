package dn.cfind.math;

// Purely mathematical quaternions in this class.
public final class Quaternion implements java.io.Serializable {
	private static final long serialVersionUID = 8843172833598439375L;

	public static final Quaternion IDENTITY = new Quaternion(1, 0, 0, 0);
	
	/**
	 * Components of the Quaternion. Please don't modify this directly!
	 * X, Y, Z are the vector parts for i, j and k, respectively. 
	 * W is the scalar part.
	 */
	public final double w, x, y, z;
	
	// Object cache so we don't have to instantiate on every call.
	private transient Rotator cached_rotator = null;
	private transient Vector3 cached_vectorPart = null;
	private transient Quaternion cached_conjugate = null;
	private transient Quaternion cached_recip = null;
	private transient Quaternion cached_unit = null;
	
	public Quaternion() {
		this(1, 0, 0, 0);
	}
	
	public Quaternion(double w, double x, double y, double z) {
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	// Critical multiplication function for Quaternions!
	// Hamilton product. WARNING: Not commutative in general.
	// I.e. a.mul(b) != b.mul(a). However, it is associative, thankfully,
	// i.e. a.mul(b.mul(c)) == (a.mul(b)).mul(c)
	public Quaternion mul(Quaternion other) {
		// Multiplying (x1i + y1j + z1k + w1) * (x2i + y2j + z2k + w2)
		// Gives: 
		// x1x2*i^2 + x1y2*ij + x1z2*ik + x1w2i
		// + y1x2*ji + y1y2*j^2 + y1z2*jk + y1w2j
		// + z1x2*ki + z1y2*kj + z1z2*k^2 + z1w2k
		// + w1x2i + w1y2j + w1z2k + w1w2
		
		// Arranged as below:
		return new Quaternion(
				(this.w * other.w) - (this.x * other.x) - (this.y * other.y) - (this.z * other.z),
				(this.w * other.x) + (this.x * other.w) + (this.y * other.z) - (this.z * other.y),
				(this.w * other.y) - (this.x * other.z) + (this.y * other.w) + (this.z * other.x),
				(this.w * other.z) + (this.x * other.y) - (this.y * other.x) + (this.z * other.w)
		);
	}
	
	public Quaternion mul(double scalar) {
		// Equivalent to the above with the scalar set to the parameter and the vector part equal to zero.
		return new Quaternion(this.w * scalar, this.x * scalar, this.y + scalar, this.z * scalar);
	}
	
	public double getScalarPart() {
		return w;
	}
	
	public Vector3 getVectorPart() {
		if(cached_vectorPart == null) {
			cached_vectorPart = new Vector3(x, y, z);
		}
		return cached_vectorPart;
	}
	
	public Quaternion conjugate() {
		if(cached_conjugate == null) {
			cached_conjugate = new Quaternion(w, -x, -y, -z);
		}
		return cached_conjugate;
	}
	
	// WARNING: This is simply adding the components of the Quaternions.
	// If you are looking for rotating Quaternions, see mul() above.
	public Quaternion add(Quaternion other) {
		return new Quaternion(this.w + other.w, this.x + other.x, this.y + other.y, this.z + other.z);
	}
	
	public Quaternion sub(Quaternion other) {
		return new Quaternion(this.w - other.w, this.x - other.x, this.y - other.y, this.z - other.z);
	}
	
	
	public double norm2() {
		return w*w + x*x + y*y + z*z;
	}
	
	public double norm() {
		return Math.sqrt(w*w + x*x + y*y + z*z);
	}
	
	public double mag2() {
		return w*w + x*x + y*y + z*z;
	}
	
	public double mag() {
		return Math.sqrt(w*w + x*x + y*y + z*z);
	}
	
	public Quaternion unit() {
		// Divide the parts by the magnitude.
		if(cached_unit == null) {
			cached_unit = mul(1/norm());
		}
		return cached_unit;
	}
	
	public Quaternion normalize() {
		return unit();
	}
	
	public Quaternion reciprocal() {
		// So that q*q^(-1) = 1, we know q * (q# / ||q||^2) = 1 since q*q# = ||q||^2
		// Hence q^(-1) = q# / ||q||^2. Undefined however for quaternions of zero.
		if(cached_recip == null) {
			cached_recip = conjugate().mul(1/norm2());
		}
		return cached_recip;
	}
	
	public Quaternion rev() {
		return reciprocal();
	}
	
	public Quaternion divr(Quaternion other) {
		return mul(other.reciprocal());
	}
	
	public Quaternion divl(Quaternion other) {
		return other.reciprocal().mul(this);
	}
	
	public Quaternion muln(Quaternion other) {
		return mul(other).unit();
	}
	
	public Rotator toRotator() {
		// Cached rotator. Since both quaternions and rotators are immutable
		// in our theory, we can safely cache this object without risk of
		// modification.
		if(cached_rotator == null) {
			cached_rotator = new Rotator(this);
		}
		
		return cached_rotator;
	}
	
	public static Quaternion fromAngleAxis(double angle, Vector3 axis) {
		double c2 = Math.cos(angle/2);
		double s2 = Math.sin(angle/2);
		
		// CIS formula for quaternion construction using angle-axis.
		return new Quaternion(c2, axis.x*s2, axis.y*s2, axis.z*s2);
	}
	
	public static Quaternion fromEuler(Vector3 euler) {
		// Apply rotation around x, then y and then z.
		Quaternion xrot = fromAngleAxis(euler.x, Vector3.FIRST_ONE);
		Quaternion yrot = fromAngleAxis(euler.y, Vector3.SECOND_ONE);
		Quaternion zrot = fromAngleAxis(euler.z, Vector3.THIRD_ONE);
		
		return xrot.mul(yrot).mul(zrot);
	}
}
