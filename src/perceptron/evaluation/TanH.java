package perceptron.evaluation;

public class TanH implements EvaluationFunction {
	public static TanH INSTANCE = new TanH();
	
	@Override
	public double evaluate(double entry) {
		return Math.tanh(entry);
	}

	public static EvaluationFunction getInstance() {
		return INSTANCE;
	}

	@Override
	public double evaluateDerivation(double entry) {
		return 1 - Math.pow(Math.tanh(entry), 2);
	}

}
