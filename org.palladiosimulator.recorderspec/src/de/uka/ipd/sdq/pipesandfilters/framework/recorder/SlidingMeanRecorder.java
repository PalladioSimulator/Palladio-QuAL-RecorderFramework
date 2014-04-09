package de.uka.ipd.sdq.pipesandfilters.framework.recorder;

import java.util.LinkedList;
import java.util.List;

import javax.measure.Measure;
import javax.measure.quantity.Quantity;

import org.palladiosimulator.edp2.models.ExperimentData.MetricSetDescription;

import de.uka.ipd.sdq.pipesandfilters.framework.recorder.launch.IRecorderConfiguration;
import de.uka.ipd.sdq.probespec.framework.measurements.Measurement;

/**
 * This recorder calculates the sliding mean, i.e. the average value of a
 * specified number of last result tuple element whenever a new pipe data
 * element is received.
 * 
 * @author Baum, Sebastian Lehrig
 * 
 */
public abstract class SlidingMeanRecorder extends Recorder implements IAggregationWriteStrategy {

    private final List<Measure<?, ? extends Quantity>> dataQueue = new LinkedList<Measure<?, ? extends Quantity>>();
    private int dataQueueSize = 0;

    /**
     * The constructor of SlidingMeanRecorder.
     * 
     * @param writeStrategy
     *            The write strategy of the recorder.
     * @param dataQueueSize
     *            The window size of the sliding mean value, i.e. the number of
     *            last incoming result tuples the mean is computed on.
     */
    public SlidingMeanRecorder(final int dataQueueSize) {
        super();
        this.dataQueueSize = dataQueueSize;
    }

    /**
     * This method initializes the aggregation write strategy, providing it with
     * all information that is necessary.
     * 
     * @param metaData
     *            The meta data for the incoming result tuples.
     */
    @Override
    public void initialize(final IRecorderConfiguration recorderConfiguration) {
        final AbstractRecorderConfiguration abstractRecorderConfiguration = (AbstractRecorderConfiguration) recorderConfiguration;

        final int aggregatedMetricIndex = ((MetricSetDescription)abstractRecorderConfiguration.getRecorderAcceptedMetric()).getSubsumedMetrics().size() - 1;

        // Create initializing meta data for the aggregation to initialize the
        // aggregation write strategy.
        final AggregationMetaDataInit aggregationMetaData = new AggregationMetaDataInit(
                aggregatedMetricIndex);
        aggregationMetaData.setAggregationFunctionName("Sliding Mean");
        aggregationMetaData
        .setAggregationFunctionDescription("Computes the average value of the last element of the result tuple at every processData step.");
        aggregationMetaData.setValid(false);

        initializeAggregatedMeasurements(aggregationMetaData);
    }

    /**
     * This method calculates the sliding mean for each incoming data element
     * with the specified window size.
     * 
     * @param data
     *            The data to be processed.
     */
    @Override
    public void writeData(final Measurement data) {
        // TODO: Fix code below
        //		// add element to data queue
        //		dataQueue.addAll(data);
        //		if (dataQueue.size() > dataQueueSize) {
        //			throw new IllegalArgumentException("Data Queue Size seems to be incorrectly set");
        //		}
        //
        //		// Aggregation is always performed on the last result tuple element
        //		Measure<?, ? extends Quantity> measure = data.get(data
        //				.size()- 1);
        //
        //		// Return value
        //		Measure<?, ? extends Quantity> resultMeasure = Measure.valueOf(0,
        //				measure.getUnit());
        //
        //		if (measure.getValue() instanceof Long) {
        //			double value = 0;
        //			for (Measure<?, ? extends Quantity> p : dataQueue) {
        //				value += (Long) p.getValue();
        //			}
        //			resultMeasure = Measure.valueOf(value / dataQueue.size(), measure
        //					.getUnit());
        //		} else if (measure.getValue() instanceof Integer) {
        //			double value = 0;
        //			for (Measure<?, ? extends Quantity> p : dataQueue) {
        //				value += (Integer) p.getValue();
        //			}
        //			resultMeasure = Measure.valueOf(value / dataQueue.size(), measure
        //					.getUnit());
        //		} else if (measure.getValue() instanceof Double) {
        //			double value = 0;
        //			for (Measure<?, ? extends Quantity> p : dataQueue) {
        //				value += (Double) p.getValue();
        //			}
        //			resultMeasure = Measure.valueOf(value / dataQueue.size(), measure
        //					.getUnit());
        //		} else if (measure.getValue() instanceof Float) {
        //			double value = 0;
        //			for (Measure<?, ? extends Quantity> p : dataQueue) {
        //				value += (Float) p.getValue();
        //			}
        //			resultMeasure = Measure.valueOf(value / dataQueue.size(), measure
        //					.getUnit());
        //		}
        //
        //		Vector<Measure<?, ? extends Quantity>> aggregatedTuple = new Vector<Measure<?, ? extends Quantity>>();
        //		aggregatedTuple.add(resultMeasure);
        //		//PipeData aggregatedData = new PipeData(aggregatedTuple);
    }


}
