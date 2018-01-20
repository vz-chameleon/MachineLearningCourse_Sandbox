package sonar;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

import data.Dataset;
import data.Sample;

public class SonarDataParser {
	
	/***
	 * Sonar.data contains a dataset to train a multilayer neural network to classify whether 
	 * We've detected a Mine or a Rock.
	 * This data file is written in a special form, thus the need of a specific parser, 
	 * such as this one, to retrieve the interesting info in a classic Dataset instance form
	 * 
	 */
	
	private Dataset resultingDataset;
//	private Dataset resultingTrainDataset;
//	private Dataset resultingTestDataset;
	private String sonarDataPath = "resources/data/sonar.data";
	
	public SonarDataParser(){
		this.resultingDataset = new Dataset();
//		this.resultingTrainDataset=new Dataset();
//		this.resultingTestDataset= new Dataset();
	}
	
	public void parseSonarData(){
//		InputStream is = SonarDataParser.class.getResourceAsStream(sonarDataPath);
		Scanner scan;
		try {
			File sonarf = new File(sonarDataPath);
			System.err.println(sonarf.getAbsolutePath());
			scan = new Scanner(sonarf);
			scan.useDelimiter(";");  
			
			String content = "";
			while (scan.hasNext()) {
				content = scan.next();
				
				//Skip the first lines of data, which are info, until $TRAIN part
				while (! content.contains("$TRAIN")){
					content = scan.next();
				}				
				break;
			}
			
			int sampleNum=1;
			// Parsing and creating the data set (merge of $TRAIN and $TEST
			while (scan.hasNext()){
				String datasample = content.replace("$TRAIN", "").replace("$TEST", "").replace("\n", "").replace("\r", "").replace(" => ", ",");
		       
		        final String[] split = datasample.split(",");
		        	
		        final Double[] features = new Double[split.length - 1];
		        for (int i = 0; i < split.length - 1; i++)
		            features[i] = Double.parseDouble(split[i]);
		           
		        final String label = split[features.length];
		        if (features.length!=0)
		        	this.resultingDataset.addEntry(new Sample(sampleNum, features, label));
		        sampleNum++;
		        
				content = scan.next();
			}
			
			
			/**
			 * In the original data file, two sets are given : $TRAIN and $Test, which each make up 
			 * for 50% of the data
			 * At first, I parsed them into different dataset instances, but Timothy insisted that 
			 * I should put all samples in the same dataset, as his implementation of nested cross 
			 * validation could not take a given test set, it creates them from the original data
			 */
			
//			sampleNum=1;
//			//Parsing and creating the test set
//			while ((scan.hasNext())){
//				String datasample = content.replace("$TEST", "").replace("\n", "").replace("\r", "").replace(" => ", "");
//				
//		       
//		        final String[] split = datasample.split(",");
//		        	
//		        final Double[] features = new Double[split.length - 1];
//		        for (int i = 0; i < split.length - 1; i++)
//		            features[i] = Double.parseDouble(split[i]);
//		           
//		        final String label = split[features.length];
//		        if (features.length!=0)
//		        	this.resultingTestDataset.addEntry(new Sample(sampleNum, features, label));
//		        sampleNum++;
//		        
//				content = scan.next();
//			}
				
						
				
			scan.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
	}
	
	public Dataset getResultingDataset() {
	return resultingDataset;
}
	
	
//	public Dataset getResultingTrainDataset() {
//		return resultingTrainDataset;
//	}
//
//	public void setResultingTrainDataset(Dataset resultingTrainDataset) {
//		this.resultingTrainDataset = resultingTrainDataset;
//	}
//
//	public Dataset getResultingTestDataset() {
//		return resultingTestDataset;
//	}
//
//	public void setResultingTestDataset(Dataset resultingTestDataset) {
//		this.resultingTestDataset = resultingTestDataset;
//	}
	
	
	public static void main(String[] args) {
		SonarDataParser sdp = new SonarDataParser();
		sdp.parseSonarData();
		System.out.println(sdp.getResultingDataset());
	}
	

}


