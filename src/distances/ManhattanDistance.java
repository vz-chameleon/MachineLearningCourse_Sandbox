package distances;

import java.util.Arrays;

import data.Sample;

public class ManhattanDistance implements DistanceMetric {

	public ManhattanDistance() {
		//Nothing
	}
	
	@Override
	public double computeDistance(Sample sample1, Sample sample2) {
		
		if(sample1.getFeatures().length != sample2.getFeatures().length) {
        	throw new RuntimeException("Manhattan Distance : vectors size are not the same ("+sample1.getFeatures().length+"!="+sample2.getFeatures().length+") !\n Sample1:"
        			+
        			sample1
        			+
					"\n Sample2:"
        			+
					sample2
					);
        }
        
        double dist = 0;
        for(int i = 0; i < sample1.getFeatures().length; i++) {
        	dist += Math.abs(sample1.getFeatures()[i] - sample2.getFeatures()[i]);
        }
        
		return dist;
	}

}
