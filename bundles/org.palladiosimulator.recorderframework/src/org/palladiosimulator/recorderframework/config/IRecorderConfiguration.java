package org.palladiosimulator.recorderframework.config;

import java.util.Map;

/**
 * Recorders need to be configured by objects conforming to this interface. Such configuration
 * objects are initialized using a given key-value map.
 * 
 * @author pmerkle, Sebastian Lehrig
 */
public interface IRecorderConfiguration {

    /**
     * Initializes the configuration object based on a given key-value map.
     * 
     * @param configuration
     *            the given key-value map.
     */
    public void setConfiguration(final Map<String, Object> configuration);

}
