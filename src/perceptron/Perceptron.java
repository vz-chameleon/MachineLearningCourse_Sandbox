package perceptron;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import classification.Classifier;
import data.Dataset;
import data.Sample;
import perceptron.evaluation.Linear;
import perceptron.evaluation.ReLU;
import perceptron.evaluation.TanH;

public class Perceptron implements Classifier {
	private Layer[] layers;
	private int nb_layers;
	private double learning_rate_init= 0.5, epoch, tau=1;
	
	public Perceptron() {
		layers = new Layer[7];
		nb_layers = 7;
		layers[0] = new EntryLayer(4);
		double[][] weights = new double[2][2];
		Random r = new Random();
		for (int i = 0; i < 2; i++)
			for (int j = 0; j < 2; j++)
				weights[i][j] = r.nextGaussian() * (1.0/Math.sqrt(2));
		layers[1] = new ConvolutionLayer(layers[0],ReLU.getInstance(), 6, 1, 2, 2, weights);
		weights = new double[2][4];
		for (int i = 0; i < 2; i++)
			for (int j = 0; j < 4; j++)
				weights[i][j] = r.nextGaussian() * (1.0/Math.sqrt(4));
		layers[2] = new ConvolutionLayer(layers[1],ReLU.getInstance(), 4, 2, 2, 2, weights);
		layers[3] = new HiddenLayer(layers[2], layers[0], ReLU.getInstance(),2);
		layers[4] = new HiddenLayer(layers[3], layers[0], ReLU.getInstance(),2);
		layers[5] = new HiddenLayer(layers[4], layers[0], TanH.getInstance(),2);
		layers[3].updateWeights();
		layers[4].updateWeights();
		layers[5].updateWeights();
		layers[6] = new ExitLayer(layers[5], Linear.getInstance(),3);
		PNeuron.epsilon = learning_rate_init;
		PNeuron.alpha = 0.5;
	}
	
	public double[] learningStep(double[] entries, double[] expected_values) {
		EntryLayer el = (EntryLayer) layers[0];
		el.setEntries(entries);
		ExitLayer exl = (ExitLayer) layers[nb_layers-1];
		exl.setDesiredValues(expected_values);
		for (int i = 1; i < nb_layers; i++) {
			layers[i].CalculateValue();
		}
		for (int i = nb_layers-1; i > 0; i--) {
			layers[i].retropropagateGradient();
		}
		for (int i = 1; i < nb_layers; i++) {
			layers[i].updateWeights();
		}
		epoch++;
		PNeuron.epsilon = learning_rate_init * (tau/tau+epoch);
		return layers[6].getValues();
	}
	
	public double[] step(double[] entries) {
		EntryLayer el = (EntryLayer) layers[0];
		el.setEntries(entries);
		for (int i = 1; i < nb_layers; i++) {
			layers[i].CalculateValue();
		}
		return layers[6].getValues();
	}
	
	@Override
	public void buildClassifier(Dataset data) {
		for (Sample d : data.getData()) {
			Double[] dv = d.getFeatures();
			double[] values = new double[dv.length];
			for (int i = 0; i < dv.length; i++)
				values[i] = dv[i].doubleValue();
			double[] expected = new double[3];
			switch (d.getClassLabel()) {
			case "Iris-versicolor":
				expected[0] = 1.0;
				break;
			case "Iris-setosa":
				expected[1] = 1.0;
				break;
			case "Iris-virginica":
				expected[2] = 1.0;
				break;	
			}			
			double[] res = learningStep(values,expected);
			//System.out.println("Expected : "+expected[0]+"|"+expected[1]+"|"+expected[2]+" vs "+res[0]+"|"+res[1]+"|"+res[2]);
		}
	}

	@Override
	public String classify(Sample Y) {
		Double[] dv = Y.getFeatures();
		double[] values = new double[dv.length];
		for (int i = 0; i < dv.length; i++)
			values[i] = dv[i].doubleValue();
		double[] res = step(values);
		double max = Double.NEGATIVE_INFINITY;
		int index_max = -1;
		for (int i = 0; i < res.length; i++) {
			if (res[i] > max) {
				max = res[i];
				index_max = i;
			}
		}
		String classe = "";
		switch (index_max) {
		case 0:
			classe = "Iris-versicolor";
			break;
		case 1:
			classe = "Iris-setosa";
			break;
		case 2:
			classe = "Iris-virginica";
			break;	
		}
		return classe;
	}

	@Override
	public void setParameters(ArrayList<Double> paramValues) {
		// TODO Auto-generated method stub

	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder("Perceptron : \n");
		for (int i = 0; i < 7; i++) {
			sb.append(layers[i]+"\n");
		}
		return sb.toString();
	}	
	public String toValue() {
		StringBuilder sb = new StringBuilder("Perceptron : \n");
		for (int i = 0; i < 7; i++) {
			sb.append(layers[i].toValue()+"\n");
		}
		return sb.toString();
	}

}
