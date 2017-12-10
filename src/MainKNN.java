import knn.Dataset;
import knn.KNearestNeighbours;
import knn.ManhattanDistance;

public class MainKNN {

	public static void main(String[] args) {
		
		Dataset iris=new Dataset();
		iris.loadFromCSVFile("ressources/data/iris.data");
		KNearestNeighbours knnClassifier = new KNearestNeighbours(5, new ManhattanDistance());
		
		//TODO : Test with training set
		

	}

}
