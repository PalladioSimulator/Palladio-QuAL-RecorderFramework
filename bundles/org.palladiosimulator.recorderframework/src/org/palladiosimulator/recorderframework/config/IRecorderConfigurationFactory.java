package org.palladiosimulator.recorderframework.config;

import java.util.Map;

import org.palladiosimulator.recorderframework.core.config.IRecorderConfiguration;

/**
 * This interface specifies factory methods for creating configuration objects of recorders.
 * 
 * @author Sebastian Lehrig
 */
public interface IRecorderConfigurationFactory {

    /**
     * Initializes this factory as a preparation for creating a configuration object.
     * 
     * TODO Do we really need initialization or can we directly create configurations? [Lehrig]
     * 
     * @param configuration
     *            a map with key-value pairs to be used for initialization.
     */
    public void initialize(final Map<String, Object> configuration);

    /**
     * Creates a configuration object using the given configuration.
     * 
     * @param configuration
     *            a map with key-value pairs to be used for configuration.
     * @return a newly created recorder configuration object.
     */
    public IRecorderConfiguration createRecorderConfiguration(final Map<String, Object> configuration);

    /**
     * Finalizes this factory, thus, allowing to close potentially opened file handles.
     */
    public void finalizeRecorderConfigurationFactory();

}
