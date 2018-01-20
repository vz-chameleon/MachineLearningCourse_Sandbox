package KohonenCard;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import data.Sample;
import knn.KNearestNeighbours;

public class SOM extends Observable{
	
	/** Listes des neurones de la carte **/
	private List<Neuron> neurons;
	private int nb_neurons;
	
	/** Tableau pour garder en mémoire les distances topologiques entres tous les neurones **/
	private int[][] topo_distances;
	
	/** Nombre de dimensions de la carte **/
	private int nb_dimensions;
	/** Taille de chaque dimensions de la carte **/
	private int[] dimension_size;
	
	private int data_size;
		
	/** Valeur du coefficient d'apprentissage **/
	public double learning_rate, lr_init, lr_fluctuation;
	/** Valeur d'évolution du coefficient d'apprentissage **/
	private boolean learning_rate_decreasing;
	
	/** Valeur du coefficient sigma de la fonction de voisinnage **/
	public double sigma, sigma_init, sigma_fluctuation;
	/** Valeur d'évolution de sigma **/
	private boolean sigma_decreasing;
	
	/** Valeurs utilisés pour estimer la progression et stopper l'algorithme **/
	public int actual_epoch;
	private int maximal_epoch;
	public Sample actual_data;
	
	public SOM(Sample data, int... dimensions) {
		this.nb_dimensions = dimensions.length;
		this.dimension_size = dimensions;
		this.data_size = data.getFeatures().length;
		this.nb_neurons = 1;
		for (int i = 0; i < nb_dimensions; i++)
			this.nb_neurons *= dimension_size[i];
		this.nb_neurons = nb_neurons;
		this.topo_distances = new int[nb_neurons][];
		for (int i = 0; i < nb_neurons; i++) {
			this.topo_distances[i] = new int[i + 1];
		}
		
		actual_epoch = 0;
		maximal_epoch = 100;
		
		lr_init = 0.5;
		lr_fluctuation = 0.005;
		learning_rate = lr_init;
		learning_rate_decreasing = true;
		sigma_init = 0.25;
		sigma_fluctuation = 0.03;
		sigma = sigma_init;
		sigma_decreasing = true;
		
		fillMap(false);
	}
	
	
	
    /**
     * Return the number of dimensions of the network.
     * @return The number of dimensions of the network.
     */
    public int getNbDim(){
    	return nb_dimensions;
    }
    
    public int getDataSize() {
    	return data_size;
    }
    
    public void setMaxEpoch(int n) {
    	maximal_epoch = n;
    }
    
    /** Fonction pour récupérer un neurone avec ses coordonnées dans la grille dans la liste de neurones **/
    public Neuron getNeurone(int ... coords) {
    	if (coords.length != nb_dimensions) {
    		return null;
    	}
    	int index= 0;
    	int dimension_coef = 1;
    	for (int i = 0; i < nb_dimensions; i++) {
    		index += coords[i] * dimension_coef;
    		dimension_coef *= dimension_size[i];
    	}
    	return neurons.get(index);
    }    
  
    /** Remplit la carte de neurones aléatoires et crée le voisinnage **/
    public void fillMap(boolean regular) {
    	neurons = new ArrayList<Neuron>(nb_neurons);
    	int[] position = new int[nb_dimensions];
    	double[] value = new double[data_size];
    	/* Création de tous les neurones */
    	for (int i = 0; i < nb_neurons; i++) {
    		Neuron n;
    		if (regular) {
	    		for (int h = 0; h < data_size; h++)
	    			value[h] = (double)position[h%nb_dimensions] / (double)dimension_size[h%nb_dimensions];
	    		n = new Neuron(data_size, position, value);
    		} else {
    			n = new Neuron(data_size, position);
    		}
    		n.setNumber(i);
    		neurons.add(n);
    		/* Calcul des distances topologiques */
    		for (int j = i; j >= 0; j--) {
    			int d = n.neighborhoodDistance(neurons.get(j));
    			topo_distances[i][j] = d;
    		}
    		/* Incrémentation de la position */
    		position[0]++;
    		for (int p = 0; p < nb_dimensions - 1; p++) {
    			if (position[p] >= dimension_size[p]) {
    				position[p] -= dimension_size[p];
    				position[p+1] ++;
    			} else {
    				break;
    			}
    		}
    	}
    	/* Création du voisinnage (2^(nb_dimensions))-connexe */
    	for (int i = 0; i < nb_neurons; i++) {
    		Neuron n = neurons.get(i);
    		ArrayList<Neuron> neighbours = new ArrayList<Neuron>();
    		position = n.getTabPosition();
    		for (int k = 0; k < nb_dimensions; k++) {
    			position[k]--;
    			if (position[k] >= 0) {
    				neighbours.add(getNeurone(position));
    			}
    			position[k]+=2;
    			if (position[k] < dimension_size[k]) {
    				neighbours.add(getNeurone(position));
    			}
    			position[k]--;
    		}
    		n.setNeighbours(neighbours);
    	}
    	
    }
	/** Vide la carte de tous ses neurones **/
	public void emptyMap(){
    	neurons.clear();
    	nb_neurons = 0;
    }  
	
	public Neuron step(Sample data, boolean learning) {
		/* Mise à jour des coefficients */
		double t = (actual_epoch) / (float)(maximal_epoch);
	    if (learning_rate_decreasing)
		    learning_rate = lr_init*Math.pow((lr_fluctuation/lr_init), Math.pow(t, 1.2));
		if (sigma_decreasing)
		    sigma = sigma_init*Math.pow((sigma_fluctuation/sigma_init), Math.pow(t, 1.2));
		actual_epoch++;
		actual_data = data;
		Neuron winner = getNeareastNeuron(data);
		System.out.println(data);
		System.out.println(winner);
		System.out.println(winner.distance(data));
		if (learning)
			learningStep(winner, data);
		this.setChanged();
		this.notifyObservers();
		return winner;
	}
	
	/** Utilisation de KNN pour donner une classe à chaque neurones **/
	public HashMap<String,Integer> classificationWithKNN(KNearestNeighbours knn) {
		HashMap<String,Integer> count = new HashMap<String,Integer>();
		for (Neuron n : neurons) {
			String data_class = knn.classify(n.getSample());
			n.setDataClass(data_class);
			if (count.containsKey(data_class))
				count.put(data_class, count.get(data_class)+1);
			else
				count.put(data_class, 1);
		}
		return count;
	}
	
    /**
     * Make a step in the algorithm.
     * @param data_set The set of data in which the algo evolves.
     * @param winners The neurons used for the learning.
     */
    public void learningStep(Neuron winner, Sample data){
    	int BMU_number = winner.getNumber();  
    	
	    for(Neuron target_neuron : neurons){
				/* Récupération de la distance topologique calculée précédement */
	    		int TN_number = target_neuron.getNumber();
	    		int distance = 0;
	    		if (TN_number > BMU_number) {
			    	distance = topo_distances[TN_number][BMU_number];
	    		} else {
			    	distance = topo_distances[BMU_number][TN_number];
	    		}
		    	double neighborhood = Math.exp(-1.0*Math.pow(distance,2)/sigma);
		    	
			    /* Mise à jour des poids */
			    target_neuron.updateWeights(learning_rate, neighborhood, data);
	    }	
    }
    
    /** Récupération du neurone vainqueur **/
    public Neuron getNeareastNeuron(Sample data) {
    	int min_number = -1;
    	double min_value = Double.POSITIVE_INFINITY;
    	for (Neuron n : neurons) {
    		double dist = n.distance(data);
    		if (dist < min_value) {
    			min_number = n.getNumber();
    			min_value = dist;
    		}
    	}
		return neurons.get(min_number);
    }
	
	public int size(){
    	return nb_neurons;
    }
	
	public List<Neuron> getNeurones(){
    	return neurons;
    }

	public Neuron getNeurone(int numero) {
		return neurons.get(numero);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Kohonen Card : \n");
		for (int i = 0; i < nb_neurons; i++) {
			Neuron n = neurons.get(i);
			sb.append(n.toString()+"\n");
		}
		return sb.toString();
	}
}
