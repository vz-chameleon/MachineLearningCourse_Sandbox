package perceptron;

import perceptron.evaluation.EvaluationFunction;

public class EntryLayer extends Layer {
	
	public EntryLayer(int nb_neurons) {
		neurons = new PNeuron[nb_neurons];
		for (int i = 0; i < nb_neurons; i++) {
			neurons[i] = new PNeuron(null, null,null, 0.5, i);
		}
	}
	
	public void setEntries(double[] values) {
		for (int i = 0; i < values.length; i++) {
			neurons[i].setValue(values[i]);
		}
	}
}
