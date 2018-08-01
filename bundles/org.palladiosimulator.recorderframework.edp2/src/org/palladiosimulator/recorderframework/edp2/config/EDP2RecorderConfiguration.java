package org.palladiosimulator.recorderframework.edp2.config;

import java.util.Map;

import org.palladiosimulator.commons.datastructureutils.MapHelper;
import org.palladiosimulator.edp2.models.ExperimentData.Measurement;
import org.palladiosimulator.recorderframework.config.AbstractRecorderConfiguration;

/**
 * Specifies EDP2 recorder configuration objects. These configuration objects allow for accessing
 * EDP2 {@link Measurements}.
 * 
 * @author Sebastian Lehrig
 */
public class EDP2RecorderConfiguration extends AbstractRecorderConfiguration {

    /** Identifier for measurement; usable in key-value maps. */
    public static final String MEASUREMENT = "measurement";

    /** The measurement available in this configuration. */
    private Measurement measurement;

    @Override
    public void setConfiguration(final Map<String, Object> configuration) {
        super.setConfiguration(configuration);
        this.measurement = MapHelper.getValue(configuration, MEASUREMENT, Measurement.class);
    }

    /**
     * Getter for the measurements of this configuration.
     * 
     * @return the measurements.
     */
    public Measurement getMeasurement() {
        return this.measurement;
    }
}
