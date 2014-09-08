package org.palladiosimulator.recorderframework.config;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.palladiosimulator.commons.datastructureutils.MapHelper;

/**
 * @author snowball, Sebastian Lehrig
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
        this.experimentName = MapHelper.getValue(configuration, EXPERIMENT_RUN_NAME, String.class);

        // Do not use just Date.toString here, as that cannot be parsed anymore.
        // If another date format shall be used here, do it properly with defining a DateFormat.
        this.experimentRunName = new SimpleDateFormat(EXPERIMENT_RUN_DATE_FORMAT).format(new Date());
    }

    /**
     * @return the experimentName
     */
    public final String getExperimentName() {
        return this.experimentName;
    }

    /**
     * @return the experimentName
     */
    public final String getExperimentRunName() {
        return this.experimentRunName;
    }

}
