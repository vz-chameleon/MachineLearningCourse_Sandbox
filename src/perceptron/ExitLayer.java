package perceptron;

import java.util.Random;

import perceptron.evaluation.EvaluationFunction;

public class ExitLayer extends Layer {
	private double[] desired_values;
	
	public ExitLayer(Layer previous, EvaluationFunction f, int nb_neurons) {
		previous_layer = previous;
		previous_layer.setNextLayer(this);
		neurons = new PNeuron[nb_neurons];
		desired_values = new double[nb_neurons];
		int p_size = previous.size(), px = 0, py = 0;
		Random r = new Random();
		for (int i = 0; i < nb_neurons; i++) {
			PNeuron[] predecessors = new PNeuron[p_size];
			double[] neuron_weight = new double[predecessors.length];
			for (int k = 0; k < p_size; k++) {
				predecessors[k] = previous.getNeuron(k);
				neuron_weight[k] = r.nextGaussian() * (1.0/Math.sqrt(p_size));
			}
			neurons[i] = new PNeuron(f, predecessors,neuron_weight, 0, i);
		}
	}
	
	@Override
	public void CalculateValue() {
		double[] values = new double[neurons.length];
		double exp_sum = 0.0;
		for (int i = 0; i < neurons.length; i++) {
			neurons[i].updateValue();
			values[i] = Math.exp(neurons[i].getValue());
			exp_sum += values[i];
		}
		for (int i = 0; i < neurons.length; i++) {
			values[i] /= exp_sum;
			neurons[i].setValue(values[i]);
		}
	}
	
	@Override
	public void retropropagateGradient() {
		for (int i = 0; i < neurons.length; i++)
			neurons[i].desiredValueUpdate(desired_values[i]);
	}

	public void setDesiredValues(double[] expected_values) {
		desired_values = expected_values;		
	}
}
