package cross_validation;

import java.util.ArrayList;

import classification.Classifier;
import data.Dataset;
import data.Sample;

public class KFoldCrossValidation {
	private KFold kFold;
	private double crossValidationResult;
	
	public KFoldCrossValidation(KFold kfold) {
		this.kFold = kfold;
	}
	
	/**
	 * This function does the Cross Validation measurement for Classifier argument using the KFold used to instantiate the CrossValidation object. 
	 * @param classifier
	 * @return crossValidationResult 
	 */
	public double kFoldCrossValidate(Classifier classifier) {
		double crossValidationResult =0;
		for (int i=0; i<kFold.getNumberOfFolds(); i++) {	//for each validation set
			System.out.println("\t \t Internal validation - fold number "+i);

			Dataset validationSet = kFold.getFold(i);		//validation set selection
			ArrayList<Dataset>  learningSets = kFold.getOtherFolds(i);
			
			Dataset fusedLeaningSet = new Dataset();
			fusedLeaningSet.addDatasetList(learningSets);	//learning set construction
			
			classifier.buildClassifier(fusedLeaningSet);	//classifier building == learning
			
			
			crossValidationResult+=Validation.validate(classifier, validationSet);
		}
		crossValidationResult/=kFold.getNumberOfFolds();	//average validation set result
		
		
		this.crossValidationResult = crossValidationResult;
		return this.crossValidationResult;
	}

}
