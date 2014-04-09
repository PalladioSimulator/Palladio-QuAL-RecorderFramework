package org.palladiosimulator.recorderspec.recorder;

import java.util.Map;

import org.palladiosimulator.edp2.models.ExperimentData.MetricDescription;
import org.palladiosimulator.recorderspec.recorder.launch.IRecorderConfiguration;

public abstract class AbstractRecorderConfiguration implements IRecorderConfiguration {

    public static final String RECORDER_ACCEPTED_METRIC = "recorderAcceptedMetric";
    public static final String MEASURED_ELEMENT_DESCRIPTION = "measuredElementDescription";

    /**
     * This list should hold one MeasuredMetric with measurement information
     * for each tuple that is inducted to the pipe by the calculators.
     */
    private MetricDescription recorderAcceptedMetric;
    private String measuredElementDescription;

    public String getMeasuredElementDescription() {
        return measuredElementDescription;
    }

    @Override
    public void setConfiguration(final Map<String, Object> configuration) {
        recorderAcceptedMetric = getValue(configuration, RECORDER_ACCEPTED_METRIC, MetricDescription.class);
        measuredElementDescription = getValue(configuration, MEASURED_ELEMENT_DESCRIPTION, String.class);
    }

    /**
     * @return the recorderAcceptedMetric
     */
    public final MetricDescription getRecorderAcceptedMetric() {
        return recorderAcceptedMetric;
    }

    /**
     * @param recorderAcceptedMetric the recorderAcceptedMetric to set
     */
    public final void setRecorderAcceptedMetric(final MetricDescription recorderAcceptedMetric) {
        this.recorderAcceptedMetric = recorderAcceptedMetric;
    }

    protected <T> T getValue(final Map<String, Object> configuration, final String configurationAttributeID, final Class<T> dataType) {
        @SuppressWarnings("unchecked")
        final
        T result = (T) configuration.get(configurationAttributeID);
        if (result == null) {
            throw new RuntimeException("Expected configuation entry not found");
        }
        if (!dataType.isInstance(result)) {
            throw new RuntimeException("Data in configuration does not have expected type");
        }

        return result;
    }
}
