package sonar;

import java.util.Random;

public class PerceptronNeuron {
	
	private float[] weights;
	private float activation;
	// parameter of the sigmoid
	static final float lambda = 1.5f;
	
	/**
	 * Constructor
	 * @param prev_n_neurons
	 */
	public PerceptronNeuron(int prev_n_neurons){
		Random rand = new Random();
			
		// each neuron has the weights of each connection with the neurons on the previous layer
		this.weights = new float[prev_n_neurons];

		// default weights are random
		for (int i = 0; i < prev_n_neurons; ++i)
			this.weights[i] = rand.nextFloat() - 0.5f;
	}

	/**
	 *  Activate the neuron with given inputs, return the output
	 * @param inputs
	 * @return stimulation
	 */
	public float activate(float inputs[]) {
		this.activation = 0.0f;
		
		// Check if there is an input for every connection ..
		assert(inputs.length == this.weights.length);

			for (int i = 0; i < inputs.length; ++i) // dot product (produit scalaire)
				this.activation += inputs[i] * this.weights[i];

			// phi(_activation), our activation function (tanh(x))
			return 2.0f / (1.0f + (float) Math.exp((-this.activation) * lambda)) - 1.0f;
		}

	/**
	 * Get the derivate value of activation  (only sigmoid function for now)
	 * @return derivate value
	 */
	public float getActivationDerivative(){
		float expmlx = (float) Math.exp(lambda * this.activation);
		return 2 * lambda * expmlx / ((1 + expmlx) * (1 + expmlx));
	}

	public float[] getWeights() { 
		return this.weights; 
	}
	
	public float getSpecificWeight(int i) { 
		return this.weights[i]; 
	}
	
	public void setSynapticWeight(int i, float v) { 
		this.weights[i] = v; 
	}

		

	

}
