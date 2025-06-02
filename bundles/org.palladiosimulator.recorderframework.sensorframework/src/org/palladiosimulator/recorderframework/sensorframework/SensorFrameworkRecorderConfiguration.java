package org.palladiosimulator.recorderframework.sensorframework;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import org.palladiosimulator.commons.datastructureutils.MapHelper;
import org.palladiosimulator.recorderframework.core.config.AbstractRecorderConfiguration;

import de.uka.ipd.sdq.sensorframework.entities.Experiment;
import de.uka.ipd.sdq.sensorframework.entities.ExperimentRun;
import de.uka.ipd.sdq.sensorframework.entities.dao.IDAOFactory;

/**
 * @deprecated Superseded by EDP2.
 */
public class SensorFrameworkRecorderConfiguration extends AbstractRecorderConfiguration implements
        Serializable {

    private static final long serialVersionUID = 1L;

    private boolean isRemoteRun;

    public static final String DAO_FACTORY = "daoFactory";
    public static final String EXPERIMENT = "experiment";
    public static final String RUN = "run";

    private IDAOFactory daoFactory;
    private Experiment experiment;
    private ExperimentRun run;

    public static final String PERSISTENCE_RECORDER_ID = "SensorFramework";

    @Override
    public void setConfiguration(final Map<String, Object> configuration) {
        super.setConfiguration(configuration);
        daoFactory = MapHelper.getValue(configuration, DAO_FACTORY, IDAOFactory.class);
        experiment = MapHelper.getValue(configuration, EXPERIMENT, Experiment.class);
        run = MapHelper.getValue(configuration, RUN, ExperimentRun.class);
    }

    /**
     * @return the daoFactory
     */
    public final IDAOFactory getDaoFactory() {
        return daoFactory;
    }

    /**
     * @return the experiment
     */
    public final Experiment getExperiment() {
        return experiment;
    }

    /**
     * @return the run
     */
    public final ExperimentRun getExperimentRun() {
        return run;
    }

    public boolean isRemoteRun() {
        return isRemoteRun;
    }

    @SuppressWarnings("unchecked")
    public Map<Integer, String> getExecutionResultTypes() {
        return Collections.EMPTY_MAP;
    }

}
