import java.util.ArrayList;

import KohonenCard.Neuron;
import KohonenCard.SOM;
import View.MainInterface;
import data.Dataset;
import data.Sample;
import distances.EuclidianDistance;
import knn.KNearestNeighbours;
import nested_cross_validation.NestedCV;
import nested_cross_validation.ParameterSpace;

public class MainKHN {
	public static void main(String[] args) {
		Dataset iris=new Dataset();
		if(args.length==0)
			throw new RuntimeException("Not enough arguments ! Please specify path to data file !");
		iris.loadFromCSVFile(args[0]);
		iris.normalize();
		
		
		KNearestNeighbours knn = new KNearestNeighbours(3, new EuclidianDistance());
		
		knn.buildClassifier(iris);
		SOM kohonen_card = new SOM(iris.getData().get(0),8,8);
		kohonen_card.classificationWithKNN(knn);
		MainInterface mi = new MainInterface(kohonen_card);
		mi.show();
		iris.shuffle();
		int cpt=0, good_cpt=0;
		kohonen_card.setMaxEpoch(1000);
		kohonen_card.knn = knn;
		
		/*NestedCV nestedCrossValidation = new NestedCV(5);
		
		ParameterSpace parameterSpace = new ParameterSpace();
		ArrayList<Double> som_lr_init = new ArrayList<>();
		ArrayList<Double> som_sg_init = new ArrayList<>();
		for (double d = 0.1; d <= 1.0; d += 0.3){
			som_lr_init.add(d);
			som_sg_init.add(d);
		}
		parameterSpace.addParameterAndValues("som_lr_init",som_lr_init);
		parameterSpace.addParameterAndValues("som_sg_init",som_sg_init);
		
		nestedCrossValidation.NestedCrossValidate(kohonen_card, parameterSpace, iris, true);
		
		System.out.println("\n\n =============================== Nested cross-validation Results ===============================");
		System.out.println(nestedCrossValidation);//*/
		
		/*for (int i = 0; i < 1000; i++) {
			Double[] value = new Double[2];
			for (int v = 0; v < 2; v++)
				value[v] = Math.random();
			Sample data = new Sample(2, value);
			Neuron n = kohonen_card.step(data, true);
			//kohonen_card.classificationWithKNN(knn);
			kohonen_card.update();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			cpt++;
		}*/
	  for (Sample data : iris.getData()) {
			Neuron n = kohonen_card.step(data, true);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (Sample data : iris.getData()) {
			String c = kohonen_card.classify(data);
			if (c.equals(data.getClassLabel()))
				good_cpt++;
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			cpt++;
		} 
		kohonen_card.update();
		System.out.println(kohonen_card +"\n"+good_cpt+"/"+cpt);//*/
	}
}
