package perceptron;

import java.util.ArrayList;
import java.util.Random;

import perceptron.evaluation.EvaluationFunction;

public class HiddenLayer extends Layer {
	private static double[] entries_weights = new double[4];
	private int entries_offset;
	
	public HiddenLayer(Layer previous, Layer entry, EvaluationFunction f, int nb_neurons) {
		previous_layer = previous;
		previous_layer.setNextLayer(this);
		entries_offset = previous_layer.size();
		neurons = new PNeuron[nb_neurons];
		int p_size = previous.size(), px = 0, py = 0;
		Random r = new Random();
		for (int i = 0; i < nb_neurons; i++) {
			PNeuron[] predecessors = new PNeuron[p_size + entry.size()];
			double[] neuron_weight = new double[predecessors.length];
			for (int k = 0; k < p_size; k++) {
				predecessors[k] = previous.getNeuron(k);
				neuron_weight[k] = r.nextGaussian() * (1.0/Math.sqrt(p_size + entry.size()));
			}
			for (int k = 0; k < entry.size(); k++) {
				predecessors[k + p_size] = entry.getNeuron(k);
				neuron_weight[k+p_size] = r.nextGaussian() * (1.0/Math.sqrt(p_size+entry.size()));
				entries_weights[k] = neuron_weight[k+p_size];
			}
			neurons[i] = new PNeuron(f, predecessors,neuron_weight, 0, i);
		}
	}
	
	@Override
	public void retropropagateGradient() {
		for (PNeuron n : neurons) {
			n.retropropagateGradientCalculation(next_layer.getNeurons());
			double[] w = n.getDeltaWeights();
			for (int i = 0; i < entries_weights.length; i++)
				entries_weights[i] += w[w.length-entries_weights.length+i];
		}
	}
	
	@Override
	public void updateWeights() {
		for (PNeuron n : neurons)
			n.setWeights(entries_weights, entries_offset);
		
	}
}
