package perceptron.evaluation;

public class Linear implements EvaluationFunction {
	public static Linear INSTANCE = new Linear();

	@Override
	public double evaluate(double entry) {
		return entry;
	}

	public static EvaluationFunction getInstance() {
		return INSTANCE;
	}

	@Override
	public double evaluateDerivation(double entry) {
		return 1;
	}

}
