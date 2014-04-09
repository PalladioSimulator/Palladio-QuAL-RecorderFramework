package de.uka.ipd.sdq.pipesandfilters.framework.recorder;

import de.uka.ipd.sdq.pipesandfilters.framework.recorder.launch.IRecorderConfiguration;
import de.uka.ipd.sdq.probespec.framework.measurements.Measurement;

/**
 * A WriteStrategy is responsible for storing the measurements
 * to any external, independent device
 *
 * @author pmerkle
 * @author Baum
 * @author Sebastian Lehrig
 */
public interface IRecorder {

    /**
     * This method contains meta data of the measurements to initialize the
     * writer.
     * 
     * @param metaData
     *            The meta data of the measurements.
     */
    public abstract void initialize(IRecorderConfiguration recorderConfiguration);

    /**
     * Writes data into the storing devices.
     * 
     * @param measurement
     *            The measurement that should be stored.
     */
    public abstract void writeData(Measurement measurement);

    /**
     * This method is called at the end of the writing process.
     */
    public abstract void flush();
}
