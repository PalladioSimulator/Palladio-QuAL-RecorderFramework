package org.palladiosimulator.recorderspec.sensorframework.strategies;

import java.util.HashMap;

import javax.measure.Measure;
import javax.measure.quantity.Dimensionless;
import javax.measure.quantity.Duration;
import javax.measure.unit.SI;

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

public class UtilisationWriteDataStrategy extends AbstractWriteDataStrategy {

    private final HashMap<String, State> statesCache = new HashMap<String, State>();

    private State idleState;

    private State busyState;

    public UtilisationWriteDataStrategy(final IDAOFactory daoFactory,
            final Experiment experiment, final ExperimentRun run) {
        super(daoFactory, experiment, run);
    }

    @Override
    public void initialise(final IRecorderConfiguration recorderConfiguration) {
        final SensorFrameworkRecorderConfiguration sensorFrameworkRecorderConfig = (SensorFrameworkRecorderConfiguration) recorderConfiguration;
        final String sensorId = sensorFrameworkRecorderConfig.getMeasuredElementDescription();
        this.idleState = SensorHelper.createOrReuseState(daoFactory, "Idle");
        this.busyState = SensorHelper.createOrReuseState(daoFactory, "Busy");
        sensor = SensorHelper.createOrReuseStateSensor(daoFactory, experiment,
                sensorId, idleState);
        if (!((StateSensor) sensor).getSensorStates().contains(idleState)) {
            ((StateSensor) sensor).addSensorState(idleState);
        }
        ((StateSensor) sensor).addSensorState(busyState);
    }

    @Override
    public void writeData(final Measurement data) {
        final Measure<Double, Duration> measurementTimeMeasure = data.getMeasureForMetric(MetricDescriptionConstants.POINT_IN_TIME_METRIC);
        final Measure<Long, Dimensionless> numericStateMeasure = data.getMeasureForMetric(MetricDescriptionConstants.CPU_STATE_METRIC);
        final double measurementTime = measurementTimeMeasure.doubleValue(SI.SECOND);
        final int numericState = numericStateMeasure.intValue(Dimensionless.UNIT);
        State state = null;
        if (numericState == 0) {
            state = idleState;
        } else {
            final String stateLiteral = "Busy " + Integer.toString(numericState)
                    + " Job(s)";
            if (!statesCache.containsKey(stateLiteral)) {
                final State newState = SensorHelper.createOrReuseState(daoFactory,
                        stateLiteral);
                statesCache.put(stateLiteral, newState);
                if (!((StateSensor) sensor).getSensorStates()
                        .contains(newState)) {
                    ((StateSensor) sensor).addSensorState(newState);
                }
            }
            state = statesCache.get(stateLiteral);
        }
        run.addStateMeasurement((StateSensor)sensor, state, measurementTime);
    }

}
