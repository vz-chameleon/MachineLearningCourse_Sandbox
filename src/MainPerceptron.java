import data.Dataset;
import data.Sample;
import perceptron.Perceptron;

public class MainPerceptron {
	public static void main(String[] args) {
		Dataset iris=new Dataset();
		if(args.length==0)
			throw new RuntimeException("Not enough arguments ! Please specify path to data file !");
		iris.loadFromCSVFile(args[0]);
		iris.shuffle();
		Perceptron p = new Perceptron();
		p.buildClassifier(iris);
		
		System.out.println(p + "\n" + iris.getData().get(0).getClassLabel() +" => "+p.classify(iris.getData().get(0)) +"\n" + p.toValue());
	}	
}
