package org.palladiosimulator.recorderframework;

import java.util.Map;

import org.palladiosimulator.edp2.models.measuringpoint.MeasuringPoint;
import org.palladiosimulator.metricspec.MetricDescription;
import org.palladiosimulator.recorderframework.launch.IRecorderConfiguration;

public abstract class AbstractRecorderConfiguration implements IRecorderConfiguration {

    public static final String RECORDER_ACCEPTED_METRIC = "recorderAcceptedMetric";
    public static final String MEASURING_POINT = "measuringPoint";

    /**
     * This list should hold one MeasuredMetric with measurement information
     * for each tuple that is inducted to the pipe by the calculators.
     */
    private MetricDescription recorderAcceptedMetric;
    private MeasuringPoint measuringPoint;

    public MeasuringPoint getMeasuringPoint() {
        return measuringPoint;
    }

    @Override
    public void setConfiguration(final Map<String, Object> configuration) {
        recorderAcceptedMetric = getValue(configuration, RECORDER_ACCEPTED_METRIC, MetricDescription.class);
        measuringPoint = getValue(configuration, MEASURING_POINT, MeasuringPoint.class);
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
