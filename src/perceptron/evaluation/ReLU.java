package perceptron.evaluation;

public class ReLU implements EvaluationFunction {
	public static ReLU INSTANCE = new ReLU();
	
	@Override
	public double evaluate(double entry) {
		return Math.max(0,entry);
	}

	public static EvaluationFunction getInstance() {
		return INSTANCE;
	}

	@Override
	public double evaluateDerivation(double entry) {
		if (entry < 0)
			return 0;
		else 
			return 1;
	}

}
