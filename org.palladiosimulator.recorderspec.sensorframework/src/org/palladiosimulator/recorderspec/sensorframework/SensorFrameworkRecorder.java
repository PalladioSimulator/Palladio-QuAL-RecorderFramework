package org.palladiosimulator.recorderspec.sensorframework;

import static org.palladiosimulator.probespec.framework.constants.MetricDescriptionConstants.CPU_STATE_OVER_TIME_METRIC;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.palladiosimulator.probespec.framework.measurements.Measurement;
import org.palladiosimulator.recorderspec.IRawWriteStrategy;
import org.palladiosimulator.recorderspec.Recorder;
import org.palladiosimulator.recorderspec.launch.IRecorderConfiguration;
import org.palladiosimulator.recorderspec.sensorframework.strategies.AbstractWriteDataStrategy;
import org.palladiosimulator.recorderspec.sensorframework.strategies.DemandedTimeWriteDataStrategy;
import org.palladiosimulator.recorderspec.sensorframework.strategies.ExecutionResultWriteDataStrategy;
import org.palladiosimulator.recorderspec.sensorframework.strategies.OverallUtilisationWriteDataStrategy;
import org.palladiosimulator.recorderspec.sensorframework.strategies.ResponseTimeWriteDataStrategy;
import org.palladiosimulator.recorderspec.sensorframework.strategies.UtilisationWriteDataStrategy;
import org.palladiosimulator.recorderspec.sensorframework.strategies.WaitingTimeWriteDataStrategy;

import de.uka.ipd.sdq.sensorframework.entities.Experiment;
import de.uka.ipd.sdq.sensorframework.entities.ExperimentRun;
import de.uka.ipd.sdq.sensorframework.entities.dao.IDAOFactory;
	
/**
 * Recorder for the SensorFramework.
 *
 * @author pmerkle, Sebastian Lehrig
 */
public class SensorFrameworkRecorder extends Recorder implements IRawWriteStrategy {

    private static Logger logger = Logger.getLogger(SensorFrameworkRecorder.class.getName());

    private SensorFrameworkRecorderConfiguration recorderConfiguration;

    /**
     * All instances of this class uses the same IDAOFactory. Hence it is
     * sufficient when a single writer flushes the data. Several subsequent
     * flushes would be redundant. This variable is used to coordinate the flush
     * between several instances of this writer. It is false when one instance
     * has written data that has not yet been flushed; true else.
     */
    private static boolean flushed;

    private AbstractWriteDataStrategy writeDataStrategy;

    @Override
    public void initialize(final IRecorderConfiguration myRecorderConfiguration) {
        if (myRecorderConfiguration instanceof SensorFrameworkRecorderConfiguration) {
            this.recorderConfiguration = (SensorFrameworkRecorderConfiguration) myRecorderConfiguration;
        } else {
            throw new IllegalArgumentException("Expected meta data of type "
                    + SensorFrameworkRecorderConfiguration.class.getSimpleName()
                    + " but was " + myRecorderConfiguration.getClass().getSimpleName());
        }

        if (this.recorderConfiguration.isRemoteRun()) {
            throw new UnsupportedOperationException("Remote runs are not implemented yet");
        }

        // Create sensor
        // TODO Remove hard coded metric names "Response Time", ... Use Enum
        // instead!?
        final IDAOFactory daoFactory = this.recorderConfiguration.getDaoFactory();
        final Experiment experiment = this.recorderConfiguration.getExperiment();
        final ExperimentRun run = this.recorderConfiguration.getExperimentRun();
        final String recorderAcceptedMetric = recorderConfiguration.getRecorderAcceptedMetric().getName(); 
        if (recorderAcceptedMetric.equals("Response Time")) {
            writeDataStrategy = new ResponseTimeWriteDataStrategy(daoFactory,
                    experiment, run);
        } else if (recorderAcceptedMetric.equals("Waiting Time")) {
            writeDataStrategy = new WaitingTimeWriteDataStrategy(daoFactory,
                    experiment, run);
        } else if (recorderAcceptedMetric.equals("Hold Time")) {
            writeDataStrategy = new WaitingTimeWriteDataStrategy(daoFactory,
                    experiment, run);
        } else if (recorderAcceptedMetric.equals("Demand")) {
            writeDataStrategy = new DemandedTimeWriteDataStrategy(daoFactory,
                    experiment, run);
        } else if (recorderAcceptedMetric.equals("State") || recorderAcceptedMetric.equals(CPU_STATE_OVER_TIME_METRIC.getName()) ) {
            writeDataStrategy = new UtilisationWriteDataStrategy(daoFactory,
                    experiment, run);
        } else if (recorderAcceptedMetric.equals("Overall Utilisation")) {
            writeDataStrategy = new OverallUtilisationWriteDataStrategy(
                    daoFactory, experiment, run);
        } else if (recorderAcceptedMetric.equals("ExecutionResult")) {
            writeDataStrategy = new ExecutionResultWriteDataStrategy(
                    daoFactory, experiment, run);
        } else {
        	throw new RuntimeException("Unsupported metric (\""+recorderAcceptedMetric+"\") requested to SensorFramework recorder");
        }
        writeDataStrategy.initialise(recorderConfiguration);

        flushed = false;
    }

    @Override
    public void writeData(final Measurement data) {
        if (!flushed) {
            writeDataStrategy.writeData(data);
        } else {
            if(logger.isEnabledFor(Level.WARN)) {
                logger.warn("Tried to write data, but the pipe has been flushed already");
            }
        }
    }

    @Override
    public synchronized void flush() {
        if (!flushed) {
            flushed = true;
            if(logger.isDebugEnabled()) {
                logger.debug("Flushing SensorFramework data store");
            }
            recorderConfiguration.getDaoFactory().store();
            //do not execute daoFactory.finalizeAndClose() ! This will flush all lists for file-based lists, e.g. experiments, from memory. This should not be done on any DAO requested via the singleton as the lists are not reloaded on next access.
        }
    }
}
