package org.palladiosimulator.recorderspec.recorder.sensorframework.strategies;

import org.palladiosimulator.recorderspec.recorder.launch.IRecorderConfiguration;

import de.uka.ipd.sdq.probespec.framework.measurements.Measurement;

public interface IWriteDataStrategy {

    public void initialise(IRecorderConfiguration metaData);

    public void writeData(Measurement data);

}
