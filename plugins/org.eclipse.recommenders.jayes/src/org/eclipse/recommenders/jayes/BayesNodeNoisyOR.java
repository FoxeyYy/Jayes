package org.eclipse.recommenders.jayes;

import org.eclipse.recommenders.jayes.factor.arraywrapper.DoubleArrayWrapper;
import org.eclipse.recommenders.jayes.util.MathUtils;

/**
 * A noisy-or node for bayesian networks.
 */
public class BayesNodeNoisyOR extends BayesNodeBase {

    public BayesNodeNoisyOR(String name) {
        super(name);
    }

    @Override
    public void setProbabilities(double... probabilities) {
        adjustFactordimensions();
        if (probabilities.length != factor.getDimensions().length - 1) {
            throw new IllegalArgumentException("Probability table does not have expected size. Expected: "
                    + factor.getDimensions().length + " but got: " + probabilities.length);
        }

        factor.setValues(new DoubleArrayWrapper(createFactor(probabilities)));
    }

    /**
     * Creates the factor values, given the lambda values.
     * @param probabilities the lambda values.
     * @return a double array which contains the probability for each occurrence.
     */
    private double[] createFactor(double... probabilities) {
        final int numElements = MathUtils.product(factor.getDimensions());
        double[] table = new double[numElements];

        for (int i = 0; i < numElements; i++) {
            int mask = i << (Integer.SIZE - factor.getDimensions().length);
            double prob = 1;
            for (int bitIndex = 0; bitIndex < factor.getDimensions().length; bitIndex++) {
                if (Integer.MIN_VALUE == (mask & Integer.MIN_VALUE)) {
                    prob = bitIndex == factor.getDimensions().length-1 ? 1 - prob : prob * (1- probabilities[bitIndex]);
                }
                mask = mask << 1;
            }
            table[i] = prob;
        }

        return table;
    }

}
