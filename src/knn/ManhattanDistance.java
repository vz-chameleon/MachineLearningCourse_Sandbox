package knn;

public class ManhattanDistance implements DistanceMetric {

	public ManhattanDistance() {
		//Nothing
	}
	
	@Override
	public double computeDistance(Sample sample1, Sample sample2) {
		
		if(sample1.getFeatures().length != sample2.getFeatures().length) {
        	throw new RuntimeException("Manhattan Distance : vectors size are not the same !");
        }
        
        double dist = 0;
        for(int i = 0; i < sample1.getFeatures().length; i++) {
        	dist += Math.abs(sample1.getFeatures()[i] - sample2.getFeatures()[i]);
        }
        
		return dist;
	}

}
