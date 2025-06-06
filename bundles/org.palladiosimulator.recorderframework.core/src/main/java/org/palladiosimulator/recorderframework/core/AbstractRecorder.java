package org.palladiosimulator.recorderframework.core;

import org.palladiosimulator.measurementframework.MeasuringValue;

/**
 * This class is the super class of any recorder implementation. A recorder is responsible of making
 * the measurements persistent.
 * 
 * @author Baum, Sebastian Lehrig
 */
public abstract class AbstractRecorder implements IRecorder {

    /**
     * The default constructor for a recorder.
     */
    public AbstractRecorder() {
        super();
    }

    @Override
    public final void newMeasurementAvailable(final MeasuringValue measurement) {
        this.writeData(measurement);
    }

    @Override
    public final void preUnregister() {
        this.flush();
    }
}
