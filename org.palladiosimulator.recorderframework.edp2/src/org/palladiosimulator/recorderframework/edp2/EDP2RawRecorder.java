package org.palladiosimulator.recorderframework.edp2;

import java.util.Date;

import javax.measure.Measure;
import javax.measure.quantity.Quantity;
import javax.measure.unit.SI;

import org.palladiosimulator.edp2.dao.MeasurementsDao;
import org.palladiosimulator.edp2.models.ExperimentData.DataSeries;
import org.palladiosimulator.edp2.models.ExperimentData.ExperimentDataFactory;
import org.palladiosimulator.edp2.models.ExperimentData.Measurements;
import org.palladiosimulator.edp2.models.ExperimentData.MeasurementsRange;
import org.palladiosimulator.edp2.models.ExperimentData.RawMeasurements;
import org.palladiosimulator.edp2.models.ExperimentData.Run;
import org.palladiosimulator.edp2.util.MeasurementsUtility;
import org.palladiosimulator.measurementframework.Measurement;
import org.palladiosimulator.recorderframework.AbstractRecorder;
import org.palladiosimulator.recorderframework.config.IRecorderConfiguration;
import org.palladiosimulator.recorderframework.edp2.config.EDP2RecorderConfiguration;

/**
 * This class provides methods necessary to write measurements to EDP2. It follows the typical three
 * steps of ProbeFramework:
 * 
 * 1) initialize: Sets up the whole experiment by specifying the EDP2 repository, an experiment
 * group, EDP2Measure objects, an ExperimentSetting, an ExperimentRun, and Measurements.
 * 
 * 2) writeData: Writes measurements into EDP2.
 * 
 * 3) flush: Ends the experiment by writing to EDP2.
 * 
 * Note that this class copes with raw measurements only. Handling aggregated measurements would be
 * the task of a different recorder.
 * 
 * @author Sebastian Lehrig, Steffen Becker
 */
public class EDP2RawRecorder extends AbstractRecorder {

    /** Shortcut to experiment data factory. */
    private static final ExperimentDataFactory EXPERIMENT_DATA_FACTORY = ExperimentDataFactory.eINSTANCE;

    /** EDP2 recorder configuration. */
    private EDP2RecorderConfiguration edp2RecorderConfig;

    /** The measurements object where measurement data will be attached to. */
    private Measurements measurements;

    @Override
    public void initialize(final IRecorderConfiguration recorderConfiguration) {
        edp2RecorderConfig = (EDP2RecorderConfiguration) recorderConfiguration;
        measurements = edp2RecorderConfig.getMeasurements();
        attachRawMeasurementRangeToMeasurements();
    }

    /**
     * This method writes the given measurement data to EDP2.
     * 
     * @param data
     *            the given measurement data.
     */
    @Override
    public void writeData(final Measurement data) {
        MeasurementsUtility.storeMeasurement(measurements, data);
    }

    /**
     * This method ends the current experiment and close the data output stream.
     */
    @Override
    public void flush() {
        final MeasurementsRange measurementsRange = this.measurements.getMeasurementsRanges().get(0);
        final Run run = this.measurements.getRun();

        final long startTime = run.getStartTime().getTime();
        final long endTime = new Date().getTime();
        run.setDuration(Measure.valueOf(endTime - startTime, SI.SECOND));
        measurementsRange.setStartTime(Measure.valueOf(startTime, SI.SECOND));
        measurementsRange.setEndTime(Measure.valueOf(endTime, SI.SECOND));

        for (final DataSeries ds : measurementsRange.getRawMeasurements().getDataSeries()) {
            final MeasurementsDao<?, ? extends Quantity> dao = MeasurementsUtility.getMeasurementsDao(ds);
            dao.flush();
        }

    }

    /**
     * In this method, an EDP2 experiment run is prepared by initializing EDP2's MeasurementRange
     */
    private void attachRawMeasurementRangeToMeasurements() {
        final MeasurementsRange measurementsRange = EXPERIMENT_DATA_FACTORY.createMeasurementsRange(measurements);
        final RawMeasurements rawMeasurements = EXPERIMENT_DATA_FACTORY.createRawMeasurements(measurementsRange);
        MeasurementsUtility.createDAOsForRawMeasurements(rawMeasurements);
    }
}
