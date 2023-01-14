package dn.cfind.old;

@Deprecated
public interface Scorer<T> {
	/**
	 * Gives a score for the other object when compared with this object.
	 * Note that the score does not have to be clamped between 0 and 1.
	 * 
	 * @param other The other object for reference.
	 * @return A score comparing the similarity between two objects.
	 */
	public double score(T other);
}
