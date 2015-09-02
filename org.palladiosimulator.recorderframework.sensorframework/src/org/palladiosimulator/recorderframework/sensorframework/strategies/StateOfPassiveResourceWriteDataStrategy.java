package org.palladiosimulator.recorderframework.sensorframework.strategies;

import java.util.HashMap;

import javax.measure.Measure;
import javax.measure.quantity.Dimensionless;
import javax.measure.quantity.Duration;
import javax.measure.unit.SI;

import org.palladiosimulator.measurementframework.measureprovider.IMeasureProvider;
import org.palladiosimulator.metricspec.constants.MetricDescriptionConstants;
import org.palladiosimulator.recorderframework.config.IRecorderConfiguration;
import org.palladiosimulator.recorderframework.sensorframework.SensorFrameworkRecorderConfiguration;
import org.palladiosimulator.recorderframework.sensorframework.SensorHelper;

import de.uka.ipd.sdq.sensorframework.entities.Experiment;
import de.uka.ipd.sdq.sensorframework.entities.ExperimentRun;
import de.uka.ipd.sdq.sensorframework.entities.State;
import de.uka.ipd.sdq.sensorframework.entities.StateSensor;
import de.uka.ipd.sdq.sensorframework.entities.dao.IDAOFactory;

/**
 * @deprecated Superseded by EDP2.
 */
@Deprecated
public class StateOfPassiveResourceWriteDataStrategy extends AbstractWriteDataStrategy {

    private final HashMap<String, State> statesCache = new HashMap<String, State>();

    private State allTokensTakenState;

    public StateOfPassiveResourceWriteDataStrategy(final IDAOFactory daoFactory, final Experiment experiment,
            final ExperimentRun run) {
        super(daoFactory, experiment, run);
    }

    @Override
    public void initialise(final IRecorderConfiguration recorderConfiguration) {
        final SensorFrameworkRecorderConfiguration sensorFrameworkRecorderConfig = (SensorFrameworkRecorderConfiguration) recorderConfiguration;
        final String sensorId = sensorFrameworkRecorderConfig.getRecorderAcceptedMetric().getName() + " of "
                + sensorFrameworkRecorderConfig.getMeasuringPoint().getStringRepresentation();
        this.allTokensTakenState = SensorHelper.createOrReuseState(this.daoFactory, "All tokens taken");

        this.sensor = SensorHelper.createOrReuseStateSensor(this.daoFactory, this.experiment, sensorId,
                this.allTokensTakenState);
        if (!((StateSensor) this.sensor).getSensorStates().contains(this.allTokensTakenState)) {
            ((StateSensor) this.sensor).addSensorState(this.allTokensTakenState);
        }
    }

    @Override
    public void writeData(final IMeasureProvider data) {
        final Measure<Double, Duration> measurementTimeMeasure = data
                .getMeasureForMetric(MetricDescriptionConstants.POINT_IN_TIME_METRIC);
        final Measure<Long, Dimensionless> numericStateMeasure = data
                .getMeasureForMetric(MetricDescriptionConstants.STATE_OF_PASSIVE_RESOURCE_METRIC);
        final double measurementTime = measurementTimeMeasure.doubleValue(SI.SECOND);
        final int numericState = numericStateMeasure.intValue(Dimensionless.UNIT);
        State state = null;
        if (numericState == 0) {
            state = this.allTokensTakenState;
        } else {
            final String stateLiteral = Integer.toString(numericState) + " token(s) left";
            if (!this.statesCache.containsKey(stateLiteral)) {
                final State newState = SensorHelper.createOrReuseState(this.daoFactory, stateLiteral);
                this.statesCache.put(stateLiteral, newState);
                if (!((StateSensor) this.sensor).getSensorStates().contains(newState)) {
                    ((StateSensor) this.sensor).addSensorState(newState);
                }
            }
            state = this.statesCache.get(stateLiteral);
        }
        this.run.addStateMeasurement((StateSensor) this.sensor, state, measurementTime);
    }

}
