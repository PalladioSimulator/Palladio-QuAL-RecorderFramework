package org.palladiosimulator.recorderframework.config;

import java.util.Map;

import org.palladiosimulator.recorderframework.launch.IRecorderConfiguration;

public interface IRecorderConfigurationFactory {

    public void initialize(Map<String, Object> configuration);

    public IRecorderConfiguration createRecorderConfiguration(Map<String, Object> configuration);

    public void finalizeRecorderConfigurationFactory();

}
