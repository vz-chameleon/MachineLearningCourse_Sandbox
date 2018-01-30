package perceptron;

import java.util.ArrayList;

import perceptron.evaluation.EvaluationFunction;

public class ConvolutionLayer extends Layer {
	private double[][] convolution_weights;
	private PNeuron[][] tab_neurons;
	
	public ConvolutionLayer(Layer previous, EvaluationFunction f, int nb_neurons, int width, int height, int nb_weights, double[][] weights) {
		previous_layer = previous;
		previous_layer.setNextLayer(this);
		neurons = new PNeuron[nb_neurons];
		int w = nb_neurons/nb_weights;
		tab_neurons = new PNeuron[nb_weights][w];
		convolution_weights = weights;
		int p_size = previous.size(), px = 0, py = 0;
		for (int i = 0; i < nb_neurons; i++) {
			PNeuron[] predecessors = new PNeuron[width*height];
			for (int x = 0; x < width; x++)
				for (int y = 0; y < height; y++)
					predecessors[y + x * height] = previous.getNeuron(x, py + y);
			PNeuron n = new PNeuron(f, predecessors, weights[px],0, i);
			neurons[i] = n;
			tab_neurons[px][py] = n;
			py++;
			if (py >= w){
				py = 0;
				px++;
			}
		}
	}
	
	/** Override pour mettre à jour les poids partagés **/
	@Override
	public void retropropagateGradient() {
		for (int i = 0 ; i < tab_neurons.length; i++) {
			for (int j = 0; j < tab_neurons[0].length; j++) {
				tab_neurons[i][j].retropropagateGradientCalculation(next_layer.getNeurons());
				double[] dw = tab_neurons[i][j].getDeltaWeights();
				for (int k = 0; k < dw.length; k++) {
					convolution_weights[i][k] += dw[k];
				}
			}
		}		
		for (int i = 0 ; i < tab_neurons.length; i++) {
			for (int j = 0; j < tab_neurons[0].length; j++) {
				tab_neurons[i][j].setWeights(convolution_weights[i]);
			}
		}		
	}
	
	@Override
	public PNeuron getNeuron(int x, int y) {
		return tab_neurons[x][y];
	}

}
