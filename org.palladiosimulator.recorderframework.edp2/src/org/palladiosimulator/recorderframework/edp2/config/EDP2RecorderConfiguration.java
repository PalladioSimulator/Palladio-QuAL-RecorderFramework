package org.palladiosimulator.recorderframework.edp2.config;

import java.util.Map;

import org.palladiosimulator.commons.datastructureutils.MapHelper;
import org.palladiosimulator.edp2.models.ExperimentData.Measurements;
import org.palladiosimulator.recorderframework.config.AbstractRecorderConfiguration;

/**
 * Specifies EDP2 recorder configuration objects. These configuration objects allow for accessing
 * EDP2 {@link Measurements}.
 * 
 * @author Sebastian Lehrig
 */
public class EDP2RecorderConfiguration extends AbstractRecorderConfiguration {

    /** Identifier for measurements; usable in key-value maps. */
    public static final String MEASUREMENTS = "measurements";

    /** The measurements available in this configuration. */
    private Measurements measurements;

    @Override
    public void setConfiguration(final Map<String, Object> configuration) {
        super.setConfiguration(configuration);
        this.measurements = MapHelper.getValue(configuration, MEASUREMENTS, Measurements.class);
    }

    /**
     * Getter for the measurements of this configuration.
     * 
     * @return the measurements.
     */
    public Measurements getMeasurements() {
        return this.measurements;
    }
}
