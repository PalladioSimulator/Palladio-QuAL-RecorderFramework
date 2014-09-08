package org.palladiosimulator.recorderframework.sensorframework.strategies;

import org.palladiosimulator.measurementframework.measureprovider.IMeasureProvider;
import org.palladiosimulator.recorderframework.launch.IRecorderConfiguration;

/**
 * @deprecated Superseded by EDP2.
 */
public interface IWriteDataStrategy {

    public void initialise(IRecorderConfiguration metaData);

    public void writeData(IMeasureProvider data);

}
