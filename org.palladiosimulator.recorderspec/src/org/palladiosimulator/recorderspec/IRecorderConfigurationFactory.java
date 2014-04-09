package org.palladiosimulator.recorderspec;

import java.util.Map;

import org.palladiosimulator.recorderspec.launch.IRecorderConfiguration;

public interface IRecorderConfigurationFactory {

    public void initialize(Map<String, Object> configuration);

    public IRecorderConfiguration createRecorderConfiguration(Map<String,Object> configuration);

}
