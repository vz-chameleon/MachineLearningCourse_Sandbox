package perceptron.evaluation;

public interface EvaluationFunction {

	public abstract double evaluate(double entry);
	
	public abstract double evaluateDerivation(double entry);
}
