package knn;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Dataset {
	
	private List<Double[]> attributes;
	private List<String> classLabels;
		
	/**
	 * Add a single sample (attributes and class label) to the data set
	 * @param attr
	 * @param cLabel
	 */
	public void addEntry(Double[] attr, String cLabel){
		attributes.add(attr);
		classLabels.add(cLabel);
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
			
	        while ((line = br.readLine()) != null) {
	        	final String[] split = line.split(",");
	        	
	            final Double[] features = new Double[split.length - 1];
	            for (int i = 0; i < split.length - 1; i++)
	                features[i] = Double.parseDouble(split[i]);
	            
	            final String label = split[features.length];
	             
	            this.addEntry(features, label);
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
		if (attributeIndex > this.attributes.get(0).length)
			throw new RuntimeException("Dataset : you asked for attribute number" + attributeIndex+" and there are only "+
					this.attributes.get(0).length+" attributes in this dataset !");
		
		Double [] datasetAttributeValues=new Double[this.attributes.size()];
		
		int k=0;
		for (Double[] attrList: this.attributes){
			datasetAttributeValues[k]=attrList[attributeIndex];
			k++;
		}
		
		return datasetAttributeValues;
	}
	
	/**
	 * Normalize this set attribute by attribute
	 */
	public void normalize(){
		
		int attrNumber= this.attributes.get(0).length;
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
			for (Double[] attrList:this.attributes){
				attrList[attributeIndex]= (attrList[attributeIndex] - min)/(max - min);
			}
			
		}
		
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder("=========== DATASET ===============");
		
		int sampleNum = this.classLabels.size();
		
		for(int sn=0;sn<sampleNum;sn++){
			sb.append("Sample "+ sn+1 + " : "+ this.attributes.get(sn).toString() +" | class label : "+classLabels.get(sn));
		}
		return sb.toString();
	}
	
	public List<Double[]> getAttributes() {
		return attributes;
	}

	public List<String> getClassLabels() {
		return classLabels;
	}
}
