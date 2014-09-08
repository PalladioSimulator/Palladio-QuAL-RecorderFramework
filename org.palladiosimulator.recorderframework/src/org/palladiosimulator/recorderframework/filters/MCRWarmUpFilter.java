package org.palladiosimulator.recorderframework.filters;

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Implements the "Marginal Confidence Rule" (MCR) for filtering the warm-up period of a steady
 * state simulation.
 * 
 * The Filter is still experimental!
 * 
 * TODO To be integrated, this filter may become an EDP2 filter [Lehrig]
 * 
 * @author Philipp Merkle
 * 
 */
public class MCRWarmUpFilter {

    private static final Logger LOGGER = Logger.getLogger(MCRWarmUpFilter.class.getName());
    private int minIndex = 0;

    public List<Double> filter(List<Double> samples) {

        if (samples.size() <= 150) {
            if (LOGGER.isEnabledFor(Level.WARN)) {
                LOGGER.warn("MCRWarmUpFilter Warning: Too few samples to get a meaningful result.");
            }
        }

        int truncatedSamplesSize = samples.size();
        double truncatedSamplesSum = 0;
        for (Double d : samples) {
            truncatedSamplesSum += d;
        }

        double minValue = Double.MAX_VALUE;

        for (int i = 0; i < samples.size() - 1; i++) {
            int remaining = samples.size() - i;
            double factor = 1 / Math.pow(remaining, 3.0);

            double truncatedSampleMean = truncatedSamplesSum / truncatedSamplesSize;
            double sum = 0;
            for (int j = i + 1; j < samples.size(); j++) {
                sum += Math.pow(samples.get(j) - truncatedSampleMean, 2.0);
            }
            double d = factor * sum;

            if (d < minValue) {
                // LOGGER.warn(i + ": " + d);
                minIndex = i;
                minValue = d;
            }

            truncatedSamplesSize--;
            truncatedSamplesSum -= samples.get(0);
        }

        if (minIndex > samples.size() / 3) {
            if (LOGGER.isEnabledFor(Level.WARN)) {
                LOGGER.warn("Truncation point is in the last two thirds of the samples, so the confidence in this result is low.");
            }
        }

        // TODO Create new list?
        return samples.subList(minIndex, samples.size() - 1);
    }

    public int getTruncationIndex() {
        return minIndex;
    }
}
