package org.palladiosimulator.recorderframework.edp2;

import java.util.Date;

import javax.measure.Measure;
import javax.measure.quantity.Quantity;
import javax.measure.unit.SI;

import org.palladiosimulator.edp2.dao.MeasurementsDao;
import org.palladiosimulator.edp2.models.ExperimentData.DataSeries;
import org.palladiosimulator.edp2.models.ExperimentData.ExperimentDataFactory;
import org.palladiosimulator.edp2.models.ExperimentData.ExperimentRun;
import org.palladiosimulator.edp2.models.ExperimentData.Measurements;
import org.palladiosimulator.edp2.models.ExperimentData.MeasurementsRange;
import org.palladiosimulator.edp2.models.ExperimentData.RawMeasurements;
import org.palladiosimulator.edp2.util.MeasurementsUtility;
import org.palladiosimulator.measurementframework.Measurement;
import org.palladiosimulator.recorderframework.Recorder;
import org.palladiosimulator.recorderframework.launch.IRecorderConfiguration;

/**
 * This abstract class provides methods necessary to write raw or aggregated measurements to the
 * EDP2. It follows the typical three steps of the ProbeFramework Framework:
 * 
 * 1) initialize(MetaDataInit metaData) Sets up the whole experiment by specifying the EDP2
 * repository, an experiment group, EDP2Measure objects, an ExperimentSetting, an ExperimentRun, and
 * Measurements.
 * 
 * 2) writeData(PipeData pipeData) Writes measurements into EDP2.
 * 
 * 3) flush() Ends the experiment after writing.
 * 
 * @author Baum, Sebastian Lehrig
 * 
 */
public class EDP2RawRecorder extends Recorder {

    /** Shortcut to experiment data factory. */
    private final static ExperimentDataFactory experimentDataFactory = ExperimentDataFactory.eINSTANCE;

    private EDP2RecorderConfiguration edp2RecorderConfig;

    private Measurements measurements;

    @Override
    public void initialize(final IRecorderConfiguration recorderConfiguration) {
        edp2RecorderConfig = (EDP2RecorderConfiguration) recorderConfiguration;
        measurements = edp2RecorderConfig.getMeasurements();
        attachRawMeasurementRangeToMeasurements();
    }

    /**
     * This method writes given measurement data to the EDP2.
     */
    @Override
    public void writeData(final Measurement data) {
        MeasurementsUtility.storeMeasurement(measurements, data);
    }

    /**
     * This method will end the current experiment and close the data output
     * stream.
     */
    @Override
    public void flush() {
        final MeasurementsRange measurementsRange = this.measurements.getMeasurementsRanges().get(0);
        final ExperimentRun experimentRun = this.measurements.getExperimentRun();

        final long startTime = experimentRun.getStartTime().getTime();
        final long endTime = new Date().getTime();
        experimentRun.setDuration(Measure.valueOf(endTime - startTime,
                SI.SECOND));
        measurementsRange.setStartTime(Measure.valueOf(startTime, SI.SECOND));
        measurementsRange.setEndTime(Measure.valueOf(endTime, SI.SECOND));

        for (final DataSeries ds : measurementsRange.getRawMeasurements().getDataSeries()) {
            final MeasurementsDao<?, ? extends Quantity> dao = MeasurementsUtility.getMeasurementsDao(ds);
            dao.flush();
        }

    }

    /**
     * In this method, an EDP2 experiment run is prepared by initializing
     * EDP2's MeasurementRange
     */
    private void attachRawMeasurementRangeToMeasurements() {
        final MeasurementsRange measurementsRange = experimentDataFactory.createMeasurementsRange(measurements);
        final RawMeasurements rawMeasurements = experimentDataFactory.createRawMeasurements(measurementsRange);
        MeasurementsUtility.createDAOsForRawMeasurements(rawMeasurements);
    }
}
