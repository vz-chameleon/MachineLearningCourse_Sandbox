package knn;

/**
 * An interface to define necessary functions 
 * for distance metrics
 * 
 * @author vz + tg
 *
 */
public interface DistanceMetric {
	
	/**
	 * Calculate the distance between two data entries
	 * 
	 * @param sample1 - A vector containing all attribute values for the first individual
	 * @param sample2 - A vector containing all attribute values for the second individual
	 * @return the distance between those individuals
	 */
	public double computeDistance(double[] sample1, double[] sample2);
	
}
