package KohonenCard;
import java.util.ArrayList;
import java.util.List;

import data.Sample;

public class Neuron {

	/** Poids du neurones **/
	private List<Double> weights;
	private String data_class;

	/** Voisins du neurone **/
	private List<Neuron> neighbours;
	
	/** Coordonnées dans la carte **/
	private List<Integer> coords;
	
	private int number;
	
	/** Constructeur avec une initialisation aléatoire des poids 
	 * @param dimension : nombre de poids du neurone
	 * @param position : coordonnées du neurone dans la grille de neurone de la carte
	 * **/
	public Neuron(int dimension, int[] position){
		weights  = new ArrayList<Double>(dimension);
		coords = new ArrayList<Integer>(position.length);
		
		for (int i = 0; i < position.length; i++) {
			coords.add(position[i]);
		}
		for (int j = 0; j < dimension; j++) {
			weights.add(new Double(Math.random()));
		}
		data_class = "NONE";
	}

	/** Constructeur avec une initialisation prédéfinie des poids
	 * 
	 * @param dimension : nombre de poids du neurone
	 * @param position : coordonnées du neurone dans la grille de neurone de la carte
	 * @param base_value : valeurs à mettre sur les poids du neurone, complétés par des valeurs aléatoire si nécessaire
	 */
	public Neuron(int dimension, int[] position, double[] base_value){
		weights  = new ArrayList<Double>(dimension);
		coords = new ArrayList<Integer>(position.length);
		
		for (int i = 0; i < position.length; i++) {
			coords.add(position[i]);
		}
		for (int j = 0; j < dimension; j++) {
			if (j < base_value.length) {
				weights.add(base_value[j]);
			} else {
				weights.add(new Double(Math.random()));
			}
		}
		data_class = "NONE";
	}
	
	/** Donne la distance euclidienne entre les poids du neurone et les données reçues en entrée **/
    public double distance(Sample sample){
    	double distance = 0.0;
    	Double[] features = sample.getFeatures();
    	if (features.length == weights.size()) {
    		for (int i =0; i < weights.size(); i++) {
    			distance += Math.pow(weights.get(i)-features[i],2);
    		}
    	} else {
    		System.out.println("Erreur: Données et Poids des neurones inconsistants.");
    		return -1;
    	}
    	distance = Math.sqrt(distance);
    	return distance;
    }
    
    /** Donne la distance de manhattan entre les positions dans la grille de neurones des deux neurones testés **/
    public int neighborhoodDistance(Neuron voisin) {
    	int distance = 0;
    	for (int i = 0; i < coords.size(); i++) {
    		distance += Math.abs(coords.get(i) - voisin.getPosition(i));
    	}
		return distance;    	
    }
    
    @Override
	public boolean equals(Object o){
		Neuron n = (Neuron) o;		
		boolean egaux = true;
		for (int i = 0; i < n.getWeights().size(); i++) {
			if(weights.get(i) != n.getWeight(i))
				egaux=false;
		}
		return egaux;
	}
    
	public Double getWeight(int index){
		return weights.get(index);
	}

    public void setWeights(ArrayList<Double> p) {
    	this.weights = p;
    }
    
	public List<Double> getWeights() {
    	return (List<Double>) ((ArrayList<Double>)weights).clone();
    }
    
    /** Fonction pour mettre à jour les poids d'un neurone pour qu'ils soient plus proches de données
     * 
     * @param f_v : fonction de voisinnage
     * @param c_a : coefficient d'apprentissage
     */
    public void updateWeights(double f_v, double c_a, Sample sample)
    {
	    	double n_poids;
	    	Double[] features = sample.getFeatures();
	    	for (int i = 0; i < features.length; i++) {
	    		n_poids = weights.get(i);
	    		n_poids += f_v * c_a * (features[i].doubleValue() - weights.get(i));
	    		weights.set(i, n_poids);
	    	}
    }
    
    public void setNumber(int n)
    {
    	number = n;
    }
    
    public int getNumber()
    {
    	return number;
    }
    
    public void setDataClass(String c)
    {
    	data_class = c;
    }
    
    public String getDataClass()
    {
    	return data_class;
    }
    
    public int[] getTabPosition(){
    	int[] tab_coords = new int[coords.size()];
    	for (int i = 0; i < coords.size(); i++) {
    		tab_coords[i] = coords.get(i);
    	}
    	return tab_coords;
    }
    
    public List<Integer> getPosition(){
    	return coords;
    }
    
    public Integer getPosition(int dimension){
    	return coords.get(dimension);
    }
	
    public void set(Neuron n){
    	weights = n.getWeights();
    	neighbours = n.getNeighbours();
    	coords = n.getPosition();
    }
	
    public List<Neuron> getNeighbours(){
    	return neighbours;
    }
    
    public void setNeighbours(ArrayList<Neuron> n){
    	neighbours = n;
    }
	
	public Sample getSample()
	{
		Sample res = new Sample(number, weights.toArray(new Double[1]), data_class);
		return res;
	}
	
	public String toString() 
	{
		StringBuilder sb = new StringBuilder();
		sb.append(number + " : ");
		for (int d = 0; d < coords.size(); d++)
			sb.append("("+coords.get(d)+")");
		sb.append(" -> | ");
		for (int w = 0; w < weights.size(); w++)
			sb.append(weights.get(w)+" | ");
		sb.append(data_class);
		return sb.toString();
	}
}
