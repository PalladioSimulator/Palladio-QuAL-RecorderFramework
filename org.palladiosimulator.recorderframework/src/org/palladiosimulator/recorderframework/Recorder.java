package org.palladiosimulator.recorderframework;

import org.palladiosimulator.measurementspec.Measurement;
import org.palladiosimulator.measurementspec.listener.IMeasurementSourceListener;


/**
 * This class is the super class of any recorder implementations. A recorder is
 * responsible of making the measurements persistent, using a specified
 * WriteStrategy. The measurements can either be aggregated before storing or be
 * stored as raw measurements.
 * 
 * @author Baum, Sebastian Lehrig
 * 
 */
public abstract class Recorder implements IRecorder, IMeasurementSourceListener {

    /**
     * The default constructor for a recorder.
     * 
     * @param writeStrategy
     *            The write strategy of the recorder.
     */
    public Recorder() {
        super();
    }

    @Override
    public final void newMeasurementAvailable(final Measurement measurement) {
        this.writeData(measurement);
    }

    @Override
    public final void preUnregister() {
        this.flush();
    }
}
