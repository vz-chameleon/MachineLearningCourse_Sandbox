package nested_cross_validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import classification.Classifier;
import cross_validation.KFoldCrossValidation;
import cross_validation.Validation;
import cross_validation.KFold;
import data.Dataset;

public class NestedCV {
	private int kFoldNumber;
	private ArrayList<HashMap<ArrayList<Double>,Double>> nestedCrossValidationResults;
	private HashMap<ArrayList<Double>,Double> outerCrosvalidationResults;
	private double performanceEstimation;


	public NestedCV(int akFoldNumber) {
		this.kFoldNumber=akFoldNumber;
	}

	public void NestedCrossValidate(Classifier aClassifier, ParameterSpace aParameterSpace, Dataset aDataset, boolean isDatasetRandomised) {
		this.nestedCrossValidationResults = new ArrayList<>();

		KFold kFold = new KFold(kFoldNumber, aDataset, isDatasetRandomised);

		for (int K = 0; K<kFoldNumber; K++) { //outer loop : for each external test fold
			System.out.println("External Test fold number "+K);
			HashMap<ArrayList<Double>,Double> innerCrossValidationResults = new HashMap<>();
			Dataset testDataset = kFold.getFold(K);
			ArrayList<Dataset> innerFolds = kFold.getOtherFolds(K);
			KFold innerKfold = new KFold(innerFolds);
			KFoldCrossValidation cv = new KFoldCrossValidation(innerKfold);

			for (ArrayList<Double> paramValues : aParameterSpace.createEveryParameterCombination()) { //inner loop : for each combination of parameters
				System.out.println("parameters values: "+paramValues);
				aClassifier.setParameters(paramValues);
				innerCrossValidationResults.put(paramValues, cv.kFoldCrossValidate(aClassifier));
			}

			nestedCrossValidationResults.add(innerCrossValidationResults);
		}

		
		//--------------------EVALUATION PERFORMANCE GLOBALE----------------------------
		performanceEstimation = 0.;
		for (int K = 0; K<kFoldNumber; K++) {
			HashMap<ArrayList<Double>, Double> innerCVResults = nestedCrossValidationResults.get(K);
			List<Entry<ArrayList<Double>, Double>> sortedEntries = new ArrayList<Entry<ArrayList<Double>, Double>>(innerCVResults.entrySet());


			Collections.sort(sortedEntries, 
					new Comparator<Entry<ArrayList<Double>, Double>>() {
				@Override
				public int compare(Entry<ArrayList<Double>, Double> e1, Entry<ArrayList<Double>, Double> e2) {
					return e2.getValue().compareTo(e1.getValue());
				}
			}
					);

			ArrayList<Double> mostEffectiveParameters = sortedEntries.get(0).getKey();
			aClassifier.setParameters(mostEffectiveParameters);
			Dataset testDataset = kFold.getFold(K);
			Dataset learingDataset = new Dataset();
			learingDataset.addDatasetList(kFold.getOtherFolds(K));

			aClassifier.buildClassifier(learingDataset);

			performanceEstimation+=Validation.validate(aClassifier, testDataset);
		}
		
		performanceEstimation/=kFoldNumber;



	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("=============*****======== NestedCV ========*****==========\n");
		sb.append("OuterK:"+kFoldNumber+"\n\n");
		HashMap[] nestedCrossvalidationArray = new HashMap[nestedCrossValidationResults.size()];
		nestedCrossValidationResults.toArray(nestedCrossvalidationArray);
		sb.append(Arrays.deepToString(nestedCrossvalidationArray));
		sb.append("\n\n Global performance estimation using testing folds : "+performanceEstimation);
		return sb.toString();
	}
}
