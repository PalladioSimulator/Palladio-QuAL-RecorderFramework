package org.palladiosimulator.recorderspec.recorder;

import java.util.Map;

import org.palladiosimulator.recorderspec.recorder.launch.IRecorderConfiguration;

public interface IRecorderConfigurationFactory {

    public void initialize(Map<String, Object> configuration);

    public IRecorderConfiguration createRecorderConfiguration(Map<String,Object> configuration);

}
