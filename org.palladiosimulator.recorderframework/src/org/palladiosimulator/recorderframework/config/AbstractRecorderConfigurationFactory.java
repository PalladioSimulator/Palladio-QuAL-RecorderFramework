/**
 * 
 */
package org.palladiosimulator.recorderframework.config;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author snowball
 *
 */
public abstract class AbstractRecorderConfigurationFactory implements IRecorderConfigurationFactory {

    public static final String EXPERIMENT_RUN_NAME = "experimentRun";

    public static final String EXPERIMENT_RUN_DATE_FORMAT = "yyyy/MM/dd HH:mm:ss:SSS";

    /**
     * The name of the experiment.
     */
    private String experimentName;

    /**
     * The name of the experiment run.
     */
    private String experimentRunName;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.palladiosimulator.recorderframework.IRecorderConfigurationFactory#initialize(java.util
     * .Map)
     */
    @Override
    public void initialize(final Map<String, Object> configuration) {
        experimentName = getValue(configuration, EXPERIMENT_RUN_NAME, String.class);

        // Do not use just Date.toString here, as that cannot be parsed anymore.
        // If another date format shall be used here, do it properly with defining a DateFormat.
        SimpleDateFormat dateFormat = new SimpleDateFormat(EXPERIMENT_RUN_DATE_FORMAT);
        experimentRunName = dateFormat.format(new Date());

    }

    /**
     * @return the experimentName
     */
    public final String getExperimentName() {
        return experimentName;
    }

    /**
     * @return the experimentName
     */
    public final String getExperimentRunName() {
        return experimentRunName;
    }

    protected <T> T getValue(final Map<String, Object> configuration, final String configurationAttributeID,
            final Class<T> dataType) {
        @SuppressWarnings("unchecked")
        final T result = (T) configuration.get(configurationAttributeID);
        if (result == null) {
            throw new RuntimeException("Expected configuation entry not found");
        }
        if (!dataType.isInstance(result)) {
            throw new RuntimeException("Data in configuration does not have expected type");
        }

        return result;
    }

}
