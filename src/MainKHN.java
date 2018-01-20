import KohonenCard.Neuron;
import KohonenCard.SOM;
import View.MainInterface;
import data.Dataset;
import data.Sample;
import distances.EuclidianDistance;
import knn.KNearestNeighbours;

public class MainKHN {
	public static void main(String[] args) {
		Dataset iris=new Dataset();
		if(args.length==0)
			throw new RuntimeException("Not enough arguments ! Please specify path to data file !");
		iris.loadFromCSVFile(args[0]);
		iris.normalize();
		
		
		KNearestNeighbours knn = new KNearestNeighbours(5, new EuclidianDistance());
		
		knn.buildClassifier(iris);
		SOM kohonen_card = new SOM(iris.getData().get(0),8,8);
		kohonen_card.classificationWithKNN(knn);
		MainInterface mi = new MainInterface(kohonen_card);
		mi.show();
		iris.shuffle();
		int cpt=0, good_cpt=0;
		kohonen_card.setMaxEpoch(1000);
		/*for (int i = 0; i < 1000; i++) {
			Double[] value = new Double[2];
			for (int v = 0; v < 2; v++)
				value[v] = Math.random();
			Sample data = new Sample(2, value);
			Neuron n = kohonen_card.step(data, true);
			kohonen_card.classificationWithKNN(knn);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			cpt++;
		}*/
		for (Sample data : iris.getData()) {
			Neuron n = kohonen_card.step(data, true);
			kohonen_card.classificationWithKNN(knn);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (Sample data : iris.getData()) {
			Neuron n = kohonen_card.step(data, false);
			kohonen_card.classificationWithKNN(knn);
			if (n.getDataClass().equals(data.getClassLabel()))
				good_cpt++;
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			cpt++;
		}
		System.out.println(kohonen_card +"\n"+good_cpt+"/"+cpt);
	}
}
