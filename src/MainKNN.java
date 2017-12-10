import java.util.ArrayList;

import javax.management.RuntimeErrorException;

import data.Dataset;
import distances.ManhattanDistance;
import knn.KNearestNeighbours;
import nested_cross_validation.NestedCV;
import nested_cross_validation.ParameterSpace;

public class MainKNN {

	public static void main(String[] args) {
		
		Dataset iris=new Dataset();
//		System.out.println(iris);
		if(args.length==0)
			throw new RuntimeException("Not enough arguments ! Please specify path to data file !");
		iris.loadFromCSVFile(args[0]);
//		iris.normalize();


		KNearestNeighbours knnClassifier = new KNearestNeighbours(5, new ManhattanDistance()); //the parameter  '5' will be overridden in the nested cross-validation
				
		NestedCV nestedCrossValidation = new NestedCV(5);
		
		ParameterSpace parameterSpace = new ParameterSpace();
		ArrayList<Double> k_num_neighbours_possibilities = new ArrayList<>();
//		k_num_neighbours_possibilities.add((double) 20);
//		k_num_neighbours_possibilities.add((double) 1);
//		k_num_neighbours_possibilities.add((double) 2);
		k_num_neighbours_possibilities.add((double) 3);
		k_num_neighbours_possibilities.add((double) 4);
		k_num_neighbours_possibilities.add((double) 5);
		k_num_neighbours_possibilities.add((double) 6);
		k_num_neighbours_possibilities.add((double) 7);
		k_num_neighbours_possibilities.add((double) 8);
		parameterSpace.addParameterAndValues("k_num_neighbours", k_num_neighbours_possibilities);
		
		nestedCrossValidation.NestedCrossValidate(knnClassifier, parameterSpace, iris, false);
		
		System.out.println("\n\n =============================== Nested cross-validation Results ===============================");
		System.out.println(nestedCrossValidation);
		

	}

}
