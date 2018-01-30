package perceptron;

import java.util.ArrayList;

public abstract class Layer {
	protected PNeuron[] neurons;
	protected Layer previous_layer, next_layer;
	
	public void CalculateValue() {
		for (PNeuron n : neurons)
			n.updateValue();
	}
	
	public void setNextLayer(Layer l) {
		next_layer = l;
	}
	
	public int size() {
		return neurons.length;
	}
	
	public void retropropagateGradient() {
		for (PNeuron n : neurons)
			n.retropropagateGradientCalculation(next_layer.getNeurons());
	}

	public PNeuron[] getNeurons() {
		return neurons;
	}
	
	public PNeuron getNeuron(int index) {
		if (index > neurons.length)
			return null;
		return neurons[index];
	}	
	public PNeuron getNeuron(int x, int y) {
		if (y > neurons.length || x >= 1)
			return null;
		return neurons[y];
	}

	public double[] getValues() {
		double[] values = new double[neurons.length];
		for (int i = 0; i < neurons.length; i++) {
			values[i] = neurons[i].getValue();
		}
		return values;
	}	
	
	public void updateWeights() {
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder("Layer : ");
		for (int i = 0; i < neurons.length; i++) {
			sb.append(neurons[i]+" | ");
		}
		return sb.toString();
	}	
	public String toValue() {
		StringBuilder sb = new StringBuilder("Layer : ");
		for (int i = 0; i < neurons.length; i++) {
			sb.append(neurons[i].toValue()+" | ");
		}
		return sb.toString();
	}
}
