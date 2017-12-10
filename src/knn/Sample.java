package knn;

public class Sample {
	
	private int num;
	private Double[] features;
	private String classLabel;
	
	public Sample(int sampleNum, Double[] someFeatures){
		this.num=sampleNum;
		this.features=someFeatures;
		this.classLabel="NONE";
	}
	
	public Sample(int sampleNum, Double[] someFeatures, String someClassLabel){
		this.num=sampleNum;
		this.features=someFeatures;
		this.classLabel=someClassLabel;
	}
	
	public String toString(){
		return "Sample "+ num + " : " + this.features.toString() +" | class label : "+ classLabel;
	}
	
	public void setFeatures(Double[] features) {
		this.features = features;
	}

	public void setClassLabel(String classLabel) {
		this.classLabel = classLabel;
	}

	public Double[] getFeatures() {
		return features;
	}

	public String getClassLabel() {
		return classLabel;
	}

}
