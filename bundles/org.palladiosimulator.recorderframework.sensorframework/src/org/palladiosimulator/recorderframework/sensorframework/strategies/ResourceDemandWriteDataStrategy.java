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
public class ResourceDemandWriteDataStrategy extends AbstractWriteDataStrategy {

    public ResourceDemandWriteDataStrategy(final IDAOFactory daoFactory, final Experiment experiment,
            final ExperimentRun run) {
        super(daoFactory, experiment, run);
    }

    @Override
    public void writeData(final IMeasureProvider data) {
        final Measure<Double, Duration> measurementTimeMeasure = data
                .getMeasureForMetric(MetricDescriptionConstants.POINT_IN_TIME_METRIC);
        final Measure<Double, Duration> demandedTimeMeasure = data
                .getMeasureForMetric(MetricDescriptionConstants.RESOURCE_DEMAND_METRIC);
        final double measurementTime = measurementTimeMeasure.doubleValue(SI.SECOND);
        final double demandedTime = demandedTimeMeasure.doubleValue(SI.SECOND);
        run.addTimeSpanMeasurement((TimeSpanSensor) sensor, measurementTime, demandedTime);
    }
}
