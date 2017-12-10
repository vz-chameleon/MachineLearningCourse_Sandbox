package knn;

public class ManhattanDistance implements DistanceMetric {

	public ManhattanDistance() {
		//Nothing
	}
	
	@Override
	public double computeDistance(double[] sample1, double[] sample2) {
		
		if(sample1.length != sample2.length) {
        	throw new RuntimeException("Manhattan Distance : vectors size are not the same !");
        }
        
        double dist = 0;
        for(int i = 0; i < sample1.length; i++) {
        	dist += Math.abs(sample1[i] - sample2[i]);
        }
        
		return dist;
	}

}
