package data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class Dataset {
	
	private List<Sample> data;
		
	
	public Dataset(){
		data=new ArrayList<>();
	}
	
	/**
	 * Create a copy of another data set.
	 * The Sample objects are the same objects in both data sets, but the List<Sample> data attributes are different.
	 * This allows, for example, shuffling the copy without shuffling the original.
	 * @param originalDataset
	 */
	public Dataset(Dataset originalDataset) {
		data = new ArrayList<>(originalDataset.getData());
	}

	/**
	 * Add a single sample (attributes and class label) to the data set
	 * @param attr
	 * @param cLabel
	 */
	public void addEntry(Sample aNewSample){
		data.add(aNewSample);
	}

	/**
	 * Add an entire data set to the local data set
	 * @param dataset
	 */
	public void addDataset(Dataset aNewDataset){
		data.addAll(aNewDataset.getData());
	}

	/**
	 * Add an entire list of data sets to the local data set
	 * @param dataset
	 */
	public void addDatasetList(List<Dataset> aListOfNewDatasets){
		for (Dataset aNewDataset : aListOfNewDatasets)
			addDataset(aNewDataset);
	}
	
	/**
	 * Load features and labels from a CSV file where labels are ALWAYS the last feature
	 * @param csvFilepath
	 */
	public void loadFromCSVFile(String csvFilepath){
		BufferedReader br = null;
        String line = "";
       
        try{
			br = new BufferedReader(new FileReader(csvFilepath));
			
			int sampleNum=1;
	        while ((line = br.readLine()) != null) {
	        	final String[] split = line.split(",");
	        	
	            final Double[] features = new Double[split.length - 1];
	            for (int i = 0; i < split.length - 1; i++)
	                features[i] = Double.parseDouble(split[i]);
	            
	            final String label = split[features.length];
	             
	            this.addEntry(new Sample(sampleNum, features, label));
	            sampleNum++;
	        }
	        
	        
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
		
	}
	
	
	/**
	 * Get an array containing all values for a particular dataset attribute
	 * This fonction searches for the specified attributes values in each sample or individual
	 * 
	 * @param attributeIndex
	 * @return an array containing all sample's values for the given attribute
	 */
	public Double[] getAttributeValues(int attributeIndex){
		
		if (attributeIndex > this.data.get(0).getFeatures().length)
			throw new RuntimeException("Dataset : you asked for attribute number" + attributeIndex+" and there are only "+
					data.get(0).getFeatures().length +" attributes in this dataset !");
		
		Double [] datasetAttributeValues=new Double[this.data.size()];
		
		int k=0;
		for(Sample sample: this.data){
			datasetAttributeValues[k]=sample.getFeatures()[attributeIndex];
			k++;
		}
		
		return datasetAttributeValues;
	}
	
	/**
	 * Normalize this set attribute by attribute
	 */
	public void normalize(){
		
		int attrNumber= this.data.get(0).getFeatures().length;
		double min = 0;
		double max = 0;
		
		//For each attribute
		for (int attributeIndex = 0; attributeIndex < attrNumber; attributeIndex++){
			
			//Get that attributes' values on all of the dataset's samples
			Double[] attrArray=this.getAttributeValues(attributeIndex);
			int sampleNumber=attrArray.length;
			
			//Sort the given attribute values to find min and max
			Arrays.sort(attrArray);
			min=attrArray[0];
			max=attrArray[sampleNumber-1];
			
			//Normalize values for this attribute
			for(Sample sample: this.data){
				//Get unnormalized features
				Double[] oldFeatures=sample.getFeatures();
				
				//Normalize single value
				oldFeatures[attributeIndex]= (oldFeatures[attributeIndex] - min)/(max - min);
				
				//Set features value with normalized ones
				sample.setFeatures(oldFeatures);
			}
			
		}
		
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder("========================== DATASET =======================");
		
		for (Sample s:this.data){
			sb.append(s.toString()+" \n ");
		}
		return sb.toString();
	}
	
	public List<Sample> getData(){
		return this.data;
	}

	public int getSize() {
		return this.data.size();
	}

	public void shuffle() {
		Collections.shuffle(this.data);
	}
}
