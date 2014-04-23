package org.palladiosimulator.recorderframework.sensorframework.strategies;

import org.palladiosimulator.measurementspec.Measurement;
import org.palladiosimulator.recorderframework.launch.IRecorderConfiguration;

public interface IWriteDataStrategy {

    public void initialise(IRecorderConfiguration metaData);

    public void writeData(Measurement data);

}
