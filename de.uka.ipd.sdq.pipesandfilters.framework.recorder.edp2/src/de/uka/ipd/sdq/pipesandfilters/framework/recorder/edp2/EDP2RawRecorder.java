package de.uka.ipd.sdq.pipesandfilters.framework.recorder.edp2;

import java.util.Date;

import javax.measure.Measure;
import javax.measure.unit.SI;

import org.palladiosimulator.edp2.impl.DataNotAccessibleException;
import org.palladiosimulator.edp2.impl.Measurement;
import org.palladiosimulator.edp2.impl.MeasurementsUtility;
import org.palladiosimulator.edp2.models.ExperimentData.ExperimentDataFactory;
import org.palladiosimulator.edp2.models.ExperimentData.ExperimentRun;
import org.palladiosimulator.edp2.models.ExperimentData.Measurements;
import org.palladiosimulator.edp2.models.ExperimentData.MeasurementsRange;
import org.palladiosimulator.edp2.models.ExperimentData.MetricSetDescription;
import org.palladiosimulator.edp2.models.ExperimentData.RawMeasurements;
import org.palladiosimulator.edp2.models.Repository.Repository;

import de.uka.ipd.sdq.pipesandfilters.framework.recorder.Recorder;
import de.uka.ipd.sdq.pipesandfilters.framework.recorder.launch.IRecorderConfiguration;

/**
 * This abstract class provides methods necessary to write raw or aggregated measurements to the
 * EDP2. It follows the typical three steps of the ProbeSpec Framework:
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
    public void writeData(final de.uka.ipd.sdq.probespec.framework.measurements.Measurement data) {
        final Measurement measurement = new Measurement(edp2RecorderConfig.getRecorderAcceptedMetric());

        for (int i = 0; i < ((MetricSetDescription)edp2RecorderConfig.getRecorderAcceptedMetric()).getSubsumedMetrics().size(); i++) {
            measurement.setMeasuredValue(i, data.getMeasureForMetric(((MetricSetDescription)edp2RecorderConfig.getRecorderAcceptedMetric()).getSubsumedMetrics().get(i)));
        }

        MeasurementsUtility.storeMeasurement(measurements, measurement);
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

        try {
            final Repository repository = experimentRun.getExperimentSetting().getExperimentGroup().getRepository();
            MeasurementsUtility.ensureClosedRepository(repository);
            MeasurementsUtility.ensureOpenRepository(repository);
        } catch (final DataNotAccessibleException e) {
            throw new RuntimeException(e);
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
