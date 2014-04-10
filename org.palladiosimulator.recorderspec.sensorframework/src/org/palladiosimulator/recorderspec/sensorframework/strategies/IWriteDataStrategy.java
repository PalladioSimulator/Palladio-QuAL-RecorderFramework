package org.palladiosimulator.recorderspec.sensorframework.strategies;

import org.palladiosimulator.probespec.framework.measurements.Measurement;
import org.palladiosimulator.recorderspec.launch.IRecorderConfiguration;

public interface IWriteDataStrategy {

    public void initialise(IRecorderConfiguration metaData);

    public void writeData(Measurement data);

}
