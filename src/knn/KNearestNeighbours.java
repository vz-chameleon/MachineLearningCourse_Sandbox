package knn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import classification.Classifier;
import data.Dataset;
import data.Sample;
import distances.DistanceMetric;

public class KNearestNeighbours implements Classifier{
	
	private Dataset trainingData;
	private int k; //Number of nearest neighbors to compute
	private DistanceMetric distance;
	
	public KNearestNeighbours(int kValue, DistanceMetric aDistance){
		this.k=kValue;
		this.distance=aDistance;
		this.trainingData=null;
	}
	
	public void buildClassifier(Dataset data){
		this.trainingData=data;
	}
	
	
	/**
	 * Computes distances to all samples of training set and returns the K nearest neighbors 
	 * of a given sample
	 * @param Y - the sample for which we seek the K-nearest neighbors
	 * @return A list of samples that are the K nearest neighbors
	 */
	public List<Sample> getNearestKNeighbors(Sample Y){
		
		//Calculate distances to each training Data sample
		// We need to keep to which sample the distance is computed from
		HashMap<Sample,Double> distances= new HashMap<>();
		
		for(Sample X : trainingData.getData()){
			distances.put(X,this.distance.computeDistance(X, Y));
		}
		
		// Sort the duos Sample-Distance Value by distance value to get the nearest neighbors
	    List<Entry<Sample,Double>> sortedEntries = new ArrayList<Entry<Sample,Double>>(distances.entrySet());

	    Collections.sort(sortedEntries, 
	            new Comparator<Entry<Sample,Double>>() {
	                @Override
	                public int compare(Entry<Sample,Double> e1, Entry<Sample,Double> e2) {
	                    return e1.getValue().compareTo(e2.getValue());
	                }
	            }
	    );
	    
	    // Add the first k nearest neighbors to results list
	    ArrayList<Sample> nearestNeighbors=new ArrayList<Sample>();
	    
	    for(int i=0;i<this.k;i++){
	    	nearestNeighbors.add(sortedEntries.get(i).getKey());
	    }
	    
	    return nearestNeighbors;
		
	}
	
	public String getMostRepresentedClass(List<Sample> nearestNeighbours){
		String mostFrequentClassLabel="NONE";
		
		HashMap<String, Integer> classFrequencies=new HashMap<>();
		
		for(Sample s : nearestNeighbours){
			// If the class label has already been encountered...
			if (classFrequencies.containsKey(s.getClassLabel())){
				//Change frequency value
				int oldFreq=classFrequencies.get(s.getClassLabel());
				classFrequencies.replace(s.getClassLabel(), oldFreq, oldFreq+1);
			}
			
			//If the class label has been encountered for the first time
			// simply add it to the hashmap
			else
				classFrequencies.put(s.getClassLabel(), 1);
		}
		
		// Sort the duos Class Label-Frequency by frequency to get the most represented class among the nearest neighbors
	    List<Entry<String,Integer>> sortedEntries = new ArrayList<Entry<String,Integer>>(classFrequencies.entrySet());

	    Collections.sort(sortedEntries, 
	            new Comparator<Entry<String,Integer>>() {
	                @Override
	                public int compare(Entry<String,Integer> e1, Entry<String,Integer> e2) {
	                    return e2.getValue().compareTo(e1.getValue());
	                }
	            }
	    );
		
	    //Most frequent class label is at the top op the sorted list
		mostFrequentClassLabel=sortedEntries.get(0).getKey();
		
		return mostFrequentClassLabel;
	}
	
	/**
	 * 
	 * @param Y - The sample Y to classify 
	 * @return the assigned class label
	 */
	public String classify(Sample Y){
		return getMostRepresentedClass(getNearestKNeighbors(Y));
	}
	
	@Override
	public void setParameters(ArrayList<Double> paramValues) {
		if (paramValues.size()>1)
			throw new Error("Too many parameters for this classifier");
		else if (paramValues.size()<1)
			throw new Error("Too few parameters for this classifier");
		else this.k=paramValues.get(0).intValue();
	}
	
}
