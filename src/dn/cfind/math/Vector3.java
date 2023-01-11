package dn.cfind.math;

// Use the right-hand, Y-up rule. (Should we use Z-up instead?)
// +X is right. -X is left.
// +Y is up. -Y is down.
// +Z is toward the camera. -Z is away from the camera.

// Further work will be to abstract this class into different Vectors.
/**
 * A utility vector class written by myself to perform Great Circle calculations.
 * Maybe I should write a Quaternion class for even more special effects...
 * 
 * @author deanstevens
 *
 */
public final class Vector3 implements java.io.Serializable {
	private static final long serialVersionUID = 6740714855671474477L;

	// Constant vectors
	/**
	 * The zero vector, i.e. (0, 0, 0)
	 */
	public static final Vector3 ZERO = new Vector3(0, 0, 0);

	/**
	 * The unit vector, i.e. (1, 1, 1)
	 */
	public static final Vector3 ONE = new Vector3(1, 1, 1);

	
	// Extra constants for direction-independent operation.
	public static final Vector3 FIRST_ONE = new Vector3(1, 0, 0);
	public static final Vector3 SECOND_ONE = new Vector3(0, 1, 0);
	public static final Vector3 THIRD_ONE = new Vector3(0, 0, 1);

	/**
	 * The right vector, i.e. (1, 0, 0)
	 */
	public static final Vector3 RIGHT = FIRST_ONE;

	/**
	 * The up vector, i.e. (0, 1, 0)
	 */
	public static final Vector3 UP = SECOND_ONE;

	/**
	 * The forward vector, i.e. (0, 0, 1) - towards the standard camera position.
	 */
	public static final Vector3 FORWARD = THIRD_ONE;

	/**
	 * The left vector, i.e. (-1, 0, 0)
	 */
	public static final Vector3 LEFT = RIGHT.rev();

	/**
	 * The down vector, i.e. (0, -1, 0)
	 */
	public static final Vector3 DOWN = UP.rev();

	/**
	 * The backward vector, i.e. (0, 0, -1) - away from the standard camera position.
	 */
	public static final Vector3 BACKWARD = FORWARD.rev();
	
	/**
	 * The components of the vector: X, Y and Z. 
	 */
	public final double x, y, z;
	
	/**
	 * Constructs a new vector with all three coordinates being zero.
	 */
	public Vector3() {
		this(0, 0, 0);
	}
	
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Adds <i>component-wise</i> this vector by the other.
	 * 
	 * @param other the vector to add to this vector.
	 * @return The addition result.
	 */
	public Vector3 add(Vector3 other) {
		return new Vector3(this.x + other.x, this.y + other.y, this.z + other.z);
	}
	
	/**
	 * Flips each component by a negative sign, effectively reversing the vector.
	 * 
	 * @return The reverse of the original vector.
	 */
	public Vector3 rev() {
		return new Vector3(-x, -y, -z);
	}
	
	public Vector3 reverse() {
		return new Vector3(-x, -y, -z);
	}
	
	/**
	 * Subtracts <i>component-wise</i> this vector by the other.
	 * 
	 * @param other the vector to subtract from this vector.
	 * @return The subtraction result.
	 */
	public Vector3 sub(Vector3 other) {
		return new Vector3(this.x - other.x, this.y - other.y, this.z - other.z);
	}
	
	/**
	 * Multiplies <b>component-wise</b> this vector by the other.
	 * 
	 * @param other the vector to multiply with this vector.
	 * @return The multiplication result.
	 */
	public Vector3 mul(Vector3 other) {
		return new Vector3(this.x * other.x, this.y * other.y, this.z * other.z);
	}
	
	/**
	 * Multiplies <i>component-wise</i> this vector by a scalar.
	 * 
	 * @param s a scalar to multiply this vector.
	 * @return The multiplication result.
	 */
	public Vector3 mul(double s) {
		return new Vector3(this.x * s, this.y * s, this.z * s);
	}
	
	/**
	 * Returns the squared magnitude of the vector. Useful for efficiency when only the squared value is needed.
	 * 
	 * @return Square of the vector magnitude.
	 * @see #norm2()
	 */
	public double mag2() {
		return x*x + y*y + z*z;
	}
	
	/**
	 * Returns the squared magnitude of the vector. Useful for efficiency when only the squared value is needed. 
	 * Alias of {@link #mag2()}.
	 * 
	 * @return Square of the vector magnitude.
	 * @see #mag2()
	 */
	public double norm2() {
		return x*x + y*y + z*z;
	}
	
	/**
	 * Returns the magnitude of the vector.
	 * 
	 * @return Vector magnitude.
	 * @see #norm()
	 */
	public double mag() {
		// To limit stack recursion we employ the direct formula. (Java may not support tail-call optimization)
		return StrictMath.sqrt(x*x + y*y + z*z);
	}
	
	/**
	 * Returns the magnitude of the vector. Alias of {@link #mag()}.
	 * 
	 * @return Vector magnitude.
	 * @see #mag()
	 */
	public double norm() {
		// To limit stack recursion we employ the direct formula.
		return StrictMath.sqrt(x*x + y*y + z*z);
	}
	
	/**
	 * Returns a unit vector with the same direction as this vector. 
	 * (I.e. normalizes the vector.)
	 * 
	 * @return A unit vector with the same direction.
	 */
	public Vector3 unit() {
		return this.mul(1/this.mag());
	}
	
	/**
	 * Computes the dot product of this vector by the other.
	 * 
	 * @param other the other vector for computing the dot product.
	 * @return The dot product.
	 */
	public double dot(Vector3 other) {
		return this.x * other.x + this.y * other.y + this.z * other.z;
	}
	
	/**
	 * Returns the angle between this vector and the other, in radians.
	 * 
	 * @param other the other vector to calculate the angle.
	 * @return The angle in radians.
	 */
	public double angle(Vector3 other) {
		// Use the formula a.b = |a||b|cos(t) to find t.
		// I.e. t = arccos(a.b / (|a||b|))
		return StrictMath.acos(this.dot(other) / (this.mag() * other.mag()));
	}
	
	/**
	 * Computes the cross product of this vector by the other. 
	 * (Not dependent if we have all-right or all-left handed coordinates.)
	 * 
	 * @param other the other vector for computing the cross product.
	 * @return The cross product.
	 */
	public Vector3 cross(Vector3 other) {
		return new Vector3(
				this.y * other.z - this.z * other.y,
				this.z * other.x - this.x * other.z,
				this.x * other.y - this.y * other.x
				);
	}
	
	/**
	 * Returns the scalar projection (i.e. {@code comp}onent of {@code ref} onto {@code this}) of the 
	 * given reference vector onto this vector.
	 * 
	 * @param ref the given vector to project onto this vector.
	 * @return The scalar projection of {@code ref} onto this vector.
	 */
	public double comp(Vector3 ref) {
		return this.dot(ref) / this.mag();
	}
	
	private double comp2(Vector3 ref) {
		return this.dot(ref) / this.mag2();
	}
	
	/**
	 * Returns the vector projection of the given reference vector onto this vector.
	 * 
	 * @param ref the given vector to project onto this vector.
	 * @return The vector projection of {@code ref} onto this vector.
	 */
	public Vector3 proj(Vector3 ref) {
		return this.mul(comp2(ref));
	}
	
	/**
	 * Returns the distance between two vectors.
	 * 
	 * @param other the other vector to calculate distance with this vector.
	 * @return The distance between the vectors.
	 */
	public double dist(Vector3 other) {
		return this.sub(other).mag();
	}
}
