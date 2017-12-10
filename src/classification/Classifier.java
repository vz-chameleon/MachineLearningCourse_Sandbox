package classification;

import java.util.ArrayList;

import data.Dataset;
import data.Sample;

public interface Classifier {
	public void buildClassifier(Dataset data);
	public String classify(Sample Y);
	public void setParameters(ArrayList<Double> paramValues);

}
