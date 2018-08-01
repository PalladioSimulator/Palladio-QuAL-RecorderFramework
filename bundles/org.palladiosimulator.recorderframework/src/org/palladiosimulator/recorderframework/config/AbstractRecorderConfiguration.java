package org.palladiosimulator.recorderframework.config;

import java.util.Map;

import org.palladiosimulator.commons.datastructureutils.MapHelper;
import org.palladiosimulator.edp2.models.measuringpoint.MeasuringPoint;
import org.palladiosimulator.metricspec.MetricDescription;

/**
 * Abstract implementation for recorder configuration objects.
 * 
 * @author Sebastian Lehrig
 */
public abstract class AbstractRecorderConfiguration implements IRecorderConfiguration {

    /** Key for accepted metrics by of recorders; usable for key-value configuration maps. */
    public static final String RECORDER_ACCEPTED_METRIC = "recorderAcceptedMetric";

    /** Accepted metric of this configuration. */
    private MetricDescription recorderAcceptedMetric;

    /** Key for measuring points to be used by recorders; usable for key-value configuration maps. */
    public static final String MEASURING_POINT = "measuringPoint";

    /** @deprecated Measuring point as needed by the sensor framework. */
    private MeasuringPoint measuringPoint;

    @Override
    public void setConfiguration(final Map<String, Object> configuration) {
        this.recorderAcceptedMetric = MapHelper.getValue(configuration, RECORDER_ACCEPTED_METRIC,
                MetricDescription.class);
        this.measuringPoint = MapHelper.getValue(configuration, MEASURING_POINT, MeasuringPoint.class);
    }

    /**
     * Getter for the measuring point.
     * 
     * @return the measuring point.
     * @deprecated Superseded by EDP2; only sensor framework needed this method.
     */
    public MeasuringPoint getMeasuringPoint() {
        return this.measuringPoint;
    }

    /**
     * Getter for the accepted metric of this configuration.
     * 
     * @return the recorderAcceptedMetric
     */
    public final MetricDescription getRecorderAcceptedMetric() {
        return this.recorderAcceptedMetric;
    }

}
