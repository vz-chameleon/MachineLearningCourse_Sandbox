package cross_validation;

import classification.Classifier;
import data.Dataset;
import data.Sample;

public class Validation {
	
	public static double validate(Classifier classifier, Dataset validationSet) {
		double oneValidationSetResult =0;
		for (Sample sample : validationSet.getData()) {	//evaluation
			String classLabel = classifier.classify(sample);
			if (classLabel.equals(sample.getClassLabel()))
				oneValidationSetResult++;
//			System.out.println("Expected : "+sample.getClassLabel()+" | Found : "+ classLabel+" || Success = "+classLabel.equals(sample.getClassLabel()));
		}
		oneValidationSetResult/=validationSet.getSize();//one validation set result
		return oneValidationSetResult;
		
	}

}
