package org.palladiosimulator.recorderspec.sensorframework.strategies;

import javax.measure.Measure;
import javax.measure.quantity.Duration;
import javax.measure.unit.SI;

import org.palladiosimulator.measurementspec.Measurement;
import org.palladiosimulator.metricspec.MetricDescriptionConstants;

import de.uka.ipd.sdq.sensorframework.entities.Experiment;
import de.uka.ipd.sdq.sensorframework.entities.ExperimentRun;
import de.uka.ipd.sdq.sensorframework.entities.TimeSpanSensor;
import de.uka.ipd.sdq.sensorframework.entities.dao.IDAOFactory;

public class PointInTimeWriteDataStrategy extends AbstractWriteDataStrategy {

    public PointInTimeWriteDataStrategy(final IDAOFactory daoFactory,
            final Experiment experiment, final ExperimentRun run) {
        super(daoFactory, experiment, run);
    }

    @Override
    public void writeData(Measurement data) {
        final Measure<Double, Duration> eventTimeMeasure = data.getMeasureForMetric(MetricDescriptionConstants.POINT_IN_TIME_METRIC);
        final double eventTime = eventTimeMeasure.doubleValue(SI.SECOND);
        //FIXME: implement real point in time sensor
        run.addTimeSpanMeasurement((TimeSpanSensor)sensor, eventTime, 0.0);
    }

}
