package classification;

import data.Dataset;
import data.Sample;

public interface Classifier {
	public void buildClassifier(Dataset data);
	public String classify(Sample Y);

}
