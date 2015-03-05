package org.palladiosimulator.recorderframework;

import org.palladiosimulator.measurementframework.MeasuringValue;
import org.palladiosimulator.measurementframework.listener.IMeasurementSourceListener;
import org.palladiosimulator.recorderframework.config.IRecorderConfiguration;

/**
 * A Recorder is responsible for storing the measurements to any external, independent device.
 *
 * @author Sebastian Lehrig
 */
public interface IRecorder extends IMeasurementSourceListener {

    /**
     * Initializes the recorder with the given configuration.
     * 
     * @param recorderConfiguration
     *            The configuration used for this recorder.
     */
    public abstract void initialize(final IRecorderConfiguration recorderConfiguration);

    /**
     * Writes data into the recorder.
     * 
     * @param measurement
     *            The measurement that should be stored.
     */
    public abstract void writeData(final MeasuringValue measurement);

    /**
     * This method is called at the end of the writing process. Flushing assures that all data is
     * written to the configured data sink, e.g., a file data source.
     */
    public abstract void flush();
}
