package org.palladiosimulator.recorderspec.sensorframework.strategies;

import javax.measure.Measure;
import javax.measure.quantity.Duration;
import javax.measure.unit.SI;

import org.palladiosimulator.probespec.framework.constants.MetricDescriptionConstants;
import org.palladiosimulator.probespec.framework.measurements.Measurement;

import de.uka.ipd.sdq.sensorframework.entities.Experiment;
import de.uka.ipd.sdq.sensorframework.entities.ExperimentRun;
import de.uka.ipd.sdq.sensorframework.entities.TimeSpanSensor;
import de.uka.ipd.sdq.sensorframework.entities.dao.IDAOFactory;

public class HoldTimeWriteDataStrategy extends AbstractWriteDataStrategy {

    public HoldTimeWriteDataStrategy(final IDAOFactory daoFactory,
            final Experiment experiment, final ExperimentRun run) {
        super(daoFactory, experiment, run);
    }

    @Override
    public void writeData(final Measurement data) {
        final Measure<Double, Duration> eventTimeMeasure = data.getMeasureForMetric(MetricDescriptionConstants.POINT_IN_TIME_METRIC);
        final Measure<Double, Duration> timeSpanMeasure = data.getMeasureForMetric(MetricDescriptionConstants.HOLD_TIME_METRIC);
        final double eventTime = eventTimeMeasure.doubleValue(SI.SECOND);
        final double timeSpan = timeSpanMeasure.doubleValue(SI.SECOND);
        run.addTimeSpanMeasurement((TimeSpanSensor)sensor, eventTime, timeSpan);
    }

}
