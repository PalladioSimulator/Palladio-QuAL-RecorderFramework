package org.palladiosimulator.recorderframework.sensorframework.strategies;

import org.palladiosimulator.recorderframework.core.config.IRecorderConfiguration;
import org.palladiosimulator.recorderframework.sensorframework.SensorFrameworkRecorderConfiguration;
import org.palladiosimulator.recorderframework.sensorframework.SensorHelper;

import de.uka.ipd.sdq.sensorframework.entities.Experiment;
import de.uka.ipd.sdq.sensorframework.entities.ExperimentRun;
import de.uka.ipd.sdq.sensorframework.entities.Sensor;
import de.uka.ipd.sdq.sensorframework.entities.dao.IDAOFactory;

/**
 * @deprecated Superseded by EDP2.
 */
@Deprecated
public abstract class AbstractWriteDataStrategy implements IWriteDataStrategy {

    protected IDAOFactory daoFactory;

    protected Experiment experiment;

    protected ExperimentRun run;

    protected Sensor sensor;

    public AbstractWriteDataStrategy(final IDAOFactory daoFactory, final Experiment experiment, final ExperimentRun run) {
        this.daoFactory = daoFactory;
        this.experiment = experiment;
        this.run = run;
    }

    @Override
    public void initialise(final IRecorderConfiguration recorderConfiguration) {
        final SensorFrameworkRecorderConfiguration sensorFrameworkRecorderConfig = (SensorFrameworkRecorderConfiguration) recorderConfiguration;
        final String sensorId = sensorFrameworkRecorderConfig.getRecorderAcceptedMetric().getName() + " of "
                + sensorFrameworkRecorderConfig.getMeasuringPoint().getStringRepresentation();
        sensor = SensorHelper.createOrReuseTimeSensor(daoFactory, experiment, sensorId);
    }

}
