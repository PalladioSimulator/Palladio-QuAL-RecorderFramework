package org.palladiosimulator.recorderframework.sensorframework.strategies;

import jakarta.measure.Measure;
import jakarta.measure.quantity.Duration;
import jakarta.measure.unit.SI;

import org.palladiosimulator.measurementframework.measureprovider.IMeasureProvider;
import org.palladiosimulator.metricspec.constants.MetricDescriptionConstants;

import de.uka.ipd.sdq.sensorframework.entities.Experiment;
import de.uka.ipd.sdq.sensorframework.entities.ExperimentRun;
import de.uka.ipd.sdq.sensorframework.entities.TimeSpanSensor;
import de.uka.ipd.sdq.sensorframework.entities.dao.IDAOFactory;

/**
 * @deprecated Superseded by EDP2.
 */
@Deprecated
public class WaitingTimeWriteDataStrategy extends AbstractWriteDataStrategy {

    public WaitingTimeWriteDataStrategy(final IDAOFactory daoFactory, final Experiment experiment,
            final ExperimentRun run) {
        super(daoFactory, experiment, run);
    }

    @Override
    public void writeData(final IMeasureProvider data) {
        final Measure<Double, Duration> eventTimeMeasure = data
                .getMeasureForMetric(MetricDescriptionConstants.POINT_IN_TIME_METRIC);
        final Measure<Double, Duration> timeSpanMeasure = data
                .getMeasureForMetric(MetricDescriptionConstants.WAITING_TIME_METRIC);
        final double timeSpan = timeSpanMeasure.doubleValue(SI.SECOND);
        final double eventTime = eventTimeMeasure.doubleValue(SI.SECOND);
        this.run.addTimeSpanMeasurement((TimeSpanSensor) this.sensor, eventTime, timeSpan);
    }

}
