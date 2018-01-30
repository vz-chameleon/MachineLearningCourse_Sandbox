package perceptron;

import perceptron.evaluation.EvaluationFunction;

public class PNeuron {
	private EvaluationFunction function;
	private double neuron_value; // si(K)
	private double neuron_activity; //ai(K)
	private double neuron_threshold; // Thetai(K)
	private double neuron_gradient; //bi(K)
	private double neuron_desired_value = 0; //di(K)
	private int nb_entries;
	private PNeuron[] entry_neurons; // si(K-1)
	private double[] weights; // wij
	private double[] delta_weights; // wij
	public static double epsilon, alpha; //learning_rate and momentum
	public int layer_number;
	
	public PNeuron(EvaluationFunction f, PNeuron[] tab_neurons, double[] w, double threshold, int index) {
		if (tab_neurons != null)
			nb_entries = tab_neurons.length;
		 entry_neurons = tab_neurons;
		 weights = w;
		 if (w != null)
			 delta_weights = new double[w.length];
		 neuron_threshold = threshold;
		 layer_number = index;
		 function = f;
	}
	
	public void desiredValueUpdate(double desired_value) {
		neuron_desired_value = desired_value;
		neuron_gradient = alpha * neuron_gradient + (1 - alpha) * (neuron_value - neuron_desired_value) * function.evaluateDerivation(neuron_activity);
		for (int i = 0; i < nb_entries; i++) {
			weights[i] -= epsilon * entry_neurons[i].getValue() * neuron_gradient;
		}
	}
	
	public void retropropagateGradientCalculation(PNeuron[] successors) {
		double n_neuron_gradient = 0;
		for (int i = 0; i < successors.length; i++)
			n_neuron_gradient += successors[i].getGradient(this); 
		neuron_gradient = alpha * neuron_gradient + (1 - alpha) * n_neuron_gradient * function.evaluateDerivation(neuron_activity);
		for (int i = 0; i < nb_entries; i++) {
			delta_weights[i] = -epsilon * entry_neurons[i].getValue() * neuron_gradient;
			weights[i] += delta_weights[i]; 
		}
	}
	
	public double[] getDeltaWeights() {
		return delta_weights;
	}
	
	public void setWeights(double[] w) {
		weights = w;
	}	
	
	public void setWeights(double[] w, int offset) {
		for (int i = offset; i < offset+w.length; i++)
			weights[i] = w[i-offset];
	}
	
	public void updateValue() {
		neuron_activity = neuron_threshold;
		for (int i = 0; i < nb_entries; i++) {
			neuron_activity += weights[i] * entry_neurons[i].getValue();
		}
		neuron_value = function.evaluate(neuron_activity);
	}
	
	public double getValue() {
		return neuron_value;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof PNeuron) {
			PNeuron p = (PNeuron) o;
			if (p == this)
				return true;
			if (layer_number == p.layer_number)
				return true;
		}
		return false;		
	}
	
	public double getGradient(PNeuron n) {
		for (int i = 0; i < entry_neurons.length; i++)
			if (n.equals(entry_neurons[i]))
				return neuron_gradient * weights[i];
		return 0.0;
	}

	public void setValue(double d) {
		neuron_value = d;		
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder("Neurone "+layer_number+"[");
		if (weights == null)
			return sb.toString();
		for (int i = 0; i < weights.length; i++) {
			sb.append(weights[i]+"|");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("]");
		return sb.toString();
	}	
	
	public String toValue() {
		StringBuilder sb = new StringBuilder("Neurone "+layer_number+"[");
		sb.append(neuron_value);
		sb.append("]");
		return sb.toString();
	}

}
