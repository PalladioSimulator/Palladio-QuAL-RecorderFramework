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
public class HoldingTimeWriteDataStrategy extends AbstractWriteDataStrategy {

    public HoldingTimeWriteDataStrategy(final IDAOFactory daoFactory, final Experiment experiment,
            final ExperimentRun run) {
        super(daoFactory, experiment, run);
    }

    @Override
    public void writeData(final IMeasureProvider data) {
        final Measure<Double, Duration> eventTimeMeasure = data
                .getMeasureForMetric(MetricDescriptionConstants.POINT_IN_TIME_METRIC);
        final Measure<Double, Duration> timeSpanMeasure = data
                .getMeasureForMetric(MetricDescriptionConstants.HOLDING_TIME_METRIC);
        final double eventTime = eventTimeMeasure.doubleValue(SI.SECOND);
        final double timeSpan = timeSpanMeasure.doubleValue(SI.SECOND);
        run.addTimeSpanMeasurement((TimeSpanSensor) sensor, eventTime, timeSpan);
    }

}
