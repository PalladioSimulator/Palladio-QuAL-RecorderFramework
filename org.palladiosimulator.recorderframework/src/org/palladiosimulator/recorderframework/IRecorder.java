package org.palladiosimulator.recorderframework;

import org.palladiosimulator.measurementframework.Measurement;
import org.palladiosimulator.measurementframework.listener.IMeasurementSourceListener;
import org.palladiosimulator.recorderframework.launch.IRecorderConfiguration;

/**
 * A WriteStrategy is responsible for storing the measurements to a recorder, i.e., any external,
 * independent device.
 *
 * @author pmerkle
 * @author Baum
 * @author Sebastian Lehrig
 */
public interface IRecorder extends IMeasurementSourceListener {

    /**
     * Initializes the recorder with the given configuration.
     * 
     * @param recorderConfiguration
     *            The configuration used for this recorder.
     */
    public abstract void initialize(IRecorderConfiguration recorderConfiguration);

    /**
     * Writes data into the recorder.
     * 
     * @param measurement
     *            The measurement that should be stored.
     */
    public abstract void writeData(Measurement measurement);

    /**
     * This method is called at the end of the writing process. Flushing assures that all data is
     * written to the configured data sink, e.g., a file data source.
     */
    public abstract void flush();
}
