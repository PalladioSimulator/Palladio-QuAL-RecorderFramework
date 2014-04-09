package de.uka.ipd.sdq.pipesandfilters.framework.recorder.edp2;

import java.io.Serializable;
import java.util.Map;

import org.palladiosimulator.edp2.models.ExperimentData.Measurements;

import de.uka.ipd.sdq.pipesandfilters.framework.recorder.AbstractRecorderConfiguration;
import de.uka.ipd.sdq.pipesandfilters.framework.recorder.launch.IRecorderConfiguration;

public class EDP2RecorderConfiguration extends AbstractRecorderConfiguration implements IRecorderConfiguration, Serializable {

    private static final long serialVersionUID = 1L;

    public static final String MEASUREMENTS = "measurements";
    private Measurements measurements;

    @Override
    public void setConfiguration(final Map<String,Object> configuration) {
        super.setConfiguration(configuration);
        measurements = getValue(configuration, MEASUREMENTS, Measurements.class);
    }

    public String getModelElementID() {
        // TODO!
        return null;
    }

    public Measurements getMeasurements() {
        return measurements;
    }
}
