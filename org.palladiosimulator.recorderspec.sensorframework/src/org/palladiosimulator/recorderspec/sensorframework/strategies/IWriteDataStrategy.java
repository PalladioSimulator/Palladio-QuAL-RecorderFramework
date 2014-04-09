package org.palladiosimulator.recorderspec.sensorframework.strategies;

import org.palladiosimulator.recorderspec.launch.IRecorderConfiguration;

import de.uka.ipd.sdq.probespec.framework.measurements.Measurement;

public interface IWriteDataStrategy {

    public void initialise(IRecorderConfiguration metaData);

    public void writeData(Measurement data);

}
