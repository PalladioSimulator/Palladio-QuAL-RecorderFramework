package org.palladiosimulator.recorderspec.sensorframework.strategies;

import java.util.HashMap;

import javax.measure.Measure;
import javax.measure.quantity.Dimensionless;
import javax.measure.quantity.Duration;
import javax.measure.unit.SI;

import org.palladiosimulator.edp2.models.ExperimentData.Identifier;
import org.palladiosimulator.edp2.models.ExperimentData.MetricSetDescription;
import org.palladiosimulator.edp2.models.ExperimentData.TextualBaseMetricDescription;
import org.palladiosimulator.measurementspec.Measurement;
import org.palladiosimulator.metricspec.MetricDescriptionConstants;
import org.palladiosimulator.recorderspec.launch.IRecorderConfiguration;
import org.palladiosimulator.recorderspec.sensorframework.SensorFrameworkRecorderConfiguration;
import org.palladiosimulator.recorderspec.sensorframework.SensorHelper;

import de.uka.ipd.sdq.sensorframework.entities.Experiment;
import de.uka.ipd.sdq.sensorframework.entities.ExperimentRun;
import de.uka.ipd.sdq.sensorframework.entities.State;
import de.uka.ipd.sdq.sensorframework.entities.StateSensor;
import de.uka.ipd.sdq.sensorframework.entities.dao.IDAOFactory;

/**
 * Realizes a write strategy for reliability sensors.
 * 
 * @author brosch
 * 
 */
public class ExecutionResultWriteDataStrategy extends AbstractWriteDataStrategy {

    /**
     * Stores the dynamically created set of states.
     */
    private final HashMap<Identifier, State> statesCache = new HashMap<Identifier, State>();
    private TextualBaseMetricDescription exceptionResultMetric;

    /**
     * Constructor for the strategy.
     * 
     * @param daoFactory
     *            the DAO factory
     * @param experiment
     *            the current experiment
     * @param run
     *            the simulation run
     */
    public ExecutionResultWriteDataStrategy(final IDAOFactory daoFactory,
            final Experiment experiment, final ExperimentRun run) {
        super(daoFactory, experiment, run);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.palladiosimulator.recorderspec.sensorframework.strategies
     * .
     * AbstractWriteDataStrategy#initialise(org.palladiosimulator.recorderspec
     * .MetaDataInit)
     */
    @Override
    public void initialise(final IRecorderConfiguration recorderConfiguration) {
        final SensorFrameworkRecorderConfiguration sensorFrameworkRecorderConfig = (SensorFrameworkRecorderConfiguration) recorderConfiguration;
        initStatesCache(sensorFrameworkRecorderConfig);
        initSensor(sensorFrameworkRecorderConfig.getMeasuredElementDescription());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.palladiosimulator.recorderspec.sensorframework.strategies
     * .IWriteDataStrategy#writeData(org.palladiosimulator.recorderspec.
     * PipeData)
     */
    @Override
    public void writeData(final Measurement data) {
        final Measure<Double, Duration> measurementTimeMeasure = data.getMeasureForMetric(MetricDescriptionConstants.POINT_IN_TIME_METRIC);
        final Measure<Identifier, Dimensionless> numericStateMeasure = data.getMeasureForMetric(exceptionResultMetric);
        final double measurementTime = measurementTimeMeasure.doubleValue(SI.SECOND);
        final Identifier state = numericStateMeasure.getValue();
        run.addStateMeasurement((StateSensor) sensor, statesCache.get(state),
                measurementTime);
    }

    /**
     * Finds or creates the success state for the sensor to be used by this
     * strategy.
     * 
     * @return the success state
     */
    private State findSuccessState() {

        // Assume that the success state is named "Success":
        for (final State state : statesCache.values()) {
            if (state.getStateLiteral().equals("Success")) {
                return state;
            }
        }
        throw new RuntimeException("No success state found");
    }

    /**
     * Initializes the state sensor to be used by the strategy.
     * 
     * @param sensorId
     *            the id of the sensor to be used
     */
    private void initSensor(final String sensorId) {
        sensor = SensorHelper.createOrReuseStateSensor(daoFactory, experiment,
                sensorId, findSuccessState());
        for (final State state : statesCache.values()) {
            if (!((StateSensor) sensor).getSensorStates().contains(state)) {
                ((StateSensor) sensor).addSensorState(state);
            }
        }
    }

    /**
     * Initializes the cache of execution result states.
     * 
     * @param metaData
     *            the meta data for the initialization of the strategy
     */
    private void initStatesCache(final SensorFrameworkRecorderConfiguration recorderConfiguration) {
        final MetricSetDescription myMetric = (MetricSetDescription) recorderConfiguration.getRecorderAcceptedMetric();
        exceptionResultMetric = null; int i = 0;
        while (exceptionResultMetric == null) {
            if (myMetric.getSubsumedMetrics().get(i) instanceof TextualBaseMetricDescription) {
                exceptionResultMetric = (TextualBaseMetricDescription) myMetric.getSubsumedMetrics().get(i);
            }
            i++;
        }
        for (final Identifier identifier : exceptionResultMetric.getIdentifiers()) {
            final State state = SensorHelper.createOrReuseState(daoFactory,
                    identifier.getLiteral());
            statesCache.put(identifier, state);
        }
    }
}
