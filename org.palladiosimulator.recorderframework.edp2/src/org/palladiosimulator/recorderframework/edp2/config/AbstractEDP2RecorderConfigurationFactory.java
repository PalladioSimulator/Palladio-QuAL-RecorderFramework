package org.palladiosimulator.recorderframework.edp2.config;

import java.util.HashMap;
import java.util.Map;

import org.palladiosimulator.commons.datastructureutils.MapHelper;
import org.palladiosimulator.edp2.impl.RepositoryManager;
import org.palladiosimulator.edp2.models.ExperimentData.ExperimentDataFactory;
import org.palladiosimulator.edp2.models.ExperimentData.ExperimentGroup;
import org.palladiosimulator.edp2.models.ExperimentData.Measurement;
import org.palladiosimulator.edp2.models.ExperimentData.MeasuringType;
import org.palladiosimulator.edp2.models.ExperimentData.Run;
import org.palladiosimulator.edp2.models.Repository.Repository;
import org.palladiosimulator.edp2.models.measuringpoint.MeasuringPoint;
import org.palladiosimulator.edp2.models.measuringpoint.MeasuringPointRepository;
import org.palladiosimulator.metricspec.MetricDescription;
import org.palladiosimulator.metricspec.MetricSetDescription;
import org.palladiosimulator.recorderframework.config.AbstractRecorderConfiguration;
import org.palladiosimulator.recorderframework.config.AbstractRecorderConfigurationFactory;
import org.palladiosimulator.recorderframework.config.IRecorderConfiguration;

import de.uka.ipd.sdq.identifier.Identifier;

/**
 * Abstract factory for EDP2 recorder configuration objects.
 * 
 * @param <RUN>
 *            EDP2 run type, either an experiment run or an experiment group run
 * @author Sebastian Lehrig
 */
public abstract class AbstractEDP2RecorderConfigurationFactory<RUN extends Run> extends
        AbstractRecorderConfigurationFactory {

    /** Identifier for EDP2 repositories; usable in key-value maps. */
    public static final String REPOSITORY_ID = "EDP2RepositoryID";

    /** Identifier for variations; usable in key-value maps. */
    public static final String VARIATION_ID = "variationId";

    /** EDP2 Repository where data should be stored. */
    protected Repository repository;

    /** EDP2 experiment group. */
    protected ExperimentGroup experimentGroup;

    /** An EDP2 run, either an experiment run or an experiment group run. */
    protected RUN experimentRun;

    @Override
    public void initialize(final Map<String, Object> configuration) {
        super.initialize(configuration);

        initializeEDP2Repository(MapHelper.getValue(configuration, REPOSITORY_ID, String.class));
        initializeExperimentGroup();
    }

    @Override
    public IRecorderConfiguration createRecorderConfiguration(final Map<String, Object> configuration) {
        final MetricDescription metricDescription = (MetricDescription) configuration
                .get(AbstractRecorderConfiguration.RECORDER_ACCEPTED_METRIC);
        addMetricDescriptionToRepository(metricDescription);

        final MeasuringPoint measuringPoint = (MeasuringPoint) configuration
                .get(AbstractRecorderConfiguration.MEASURING_POINT);

        final Measurement measurement = initializeMeasurements(initializeMeasuringType(metricDescription,
                measuringPoint));
        final EDP2RecorderConfiguration result = new EDP2RecorderConfiguration();
        final Map<String, Object> newConfiguration = new HashMap<String, Object>();
        newConfiguration.putAll(configuration);
        newConfiguration.put(EDP2RecorderConfiguration.MEASUREMENT, measurement);
        result.setConfiguration(newConfiguration);

        return result;
    }

    @Override
    public void finalizeRecorderConfigurationFactory() {
        this.repository.flush();
    }

    /**
     * Initializes the EDP2 measure used to type measurements. Creates a new measure if none
     * conforming to the given metric description and to the given measuring point exists.
     * 
     * @param metricDescription
     *            the metric description of the measure.
     * @param measuringPoint
     *            the measuring point of the measure.
     * @return a measure conforming to the given parameters.
     */
    private MeasuringType initializeMeasuringType(final MetricDescription metricDescription,
            final MeasuringPoint measuringPoint) {
        // Check for existing measuring types in the experimentGroup
        for (final MeasuringType measuringType : this.experimentGroup.getMeasuringTypes()) {
            if (measuringType.getMetric().equals(metricDescription)
                    && measuringType.getMeasuringPoint().equals(measuringPoint)) {
                return measuringType;
            }
        }

        return createMeasuringType(metricDescription, measuringPoint);
    }

    /**
     * Creates a new measure conforming to the given metric description and to the given measuring
     * point.
     * 
     * @param measureMetric
     *            the metric description of the measure.
     * @param measuringPoint
     *            the measuring point of the measure.
     * @return a newly created measure conforming to the given parameters.
     */
    protected MeasuringType createMeasuringType(final MetricDescription measureMetric,
            final MeasuringPoint measuringPoint) {
        initializeMeasuringPointRepository(measuringPoint);

        final MeasuringType measuringType;
        measuringType = ExperimentDataFactory.eINSTANCE.createMeasuringType();
        measuringType.setMeasuringPoint(measuringPoint);
        measuringType.setMetric(measureMetric);
        measuringType.setExperimentGroup(this.experimentGroup);
        return measuringType;
    }

    /**
     * Initializes a measuring point repository if not present yet.
     * 
     * @param measuringPointRepository
     *            the repository to be initialized.
     */
    private void initializeMeasuringPointRepository(final MeasuringPoint measuringPoint) {
        if (measuringPoint.getMeasuringPointRepository() == null) {
            throw new IllegalArgumentException("Measuring point \"" + measuringPoint + "\" lacks a repository!");
        }

        // repo already reference by experiment group?
        final MeasuringPointRepository measuringPointRepository = measuringPoint.getMeasuringPointRepository();
        for (final MeasuringPointRepository repo : this.experimentGroup.getMeasuringPointRepositories()) {
            if (repo.getId().equals(measuringPointRepository.getId())) {
                return; // yes!
            }
        }

        // no!
        this.experimentGroup.getMeasuringPointRepositories().add(measuringPointRepository);
    }

    /**
     * Initialize EDP2 measurements.
     * 
     * @param measuringType
     *            the measure typing the measurements to be initialized.
     * @return a newly created measurements object; typed by the given measure.
     */
    private Measurement initializeMeasurements(final MeasuringType measuringType) {
        final Measurement measurements = ExperimentDataFactory.eINSTANCE.createMeasurement();

        measurements.setMeasuringType(measuringType);
        this.experimentRun.getMeasurement().add(measurements);

        return measurements;
    }

    /**
     * Receives the EDP2 from the central repository registry, based on a given repository ID.
     * 
     * @param repositoryID
     *            the repository ID to check for.
     */
    private void initializeEDP2Repository(final String repositoryID) {
        this.repository = RepositoryManager.getRepositoryFromUUID(repositoryID);
        if (this.repository == null) {
            throw new IllegalArgumentException(
                    "The provided EDP2 repository does not exist. Please check your configuration");
        }
    }

    /**
     * Initializes an EDP2 ExperimentGroup if not present.
     * 
     * Note that the purpose of an experiment group shall be equal to the experiment name of a
     * typical analysis run configuration and, thus, shall uniquely identify an experiment group.
     */
    private void initializeExperimentGroup() {
        // check for existing experiment group
        for (final ExperimentGroup group : this.repository.getExperimentGroups()) {
            if (group.getPurpose().equals(getExperimentName())) {
                this.experimentGroup = group;
                return;
            }
        }

        // create new experiment group
        this.experimentGroup = ExperimentDataFactory.eINSTANCE.createExperimentGroup();
        this.experimentGroup.setPurpose(getExperimentName());
        this.repository.getExperimentGroups().add(this.experimentGroup);
    }

    /**
     * Adds the given metric description to the metric description repository if not present.
     * 
     * @param metricDescription
     *            the metric description to be added.
     * @return the original metric description if it was added; an existing one if the metric
     *         description was already available in the repository.
     */
    private MetricDescription addMetricDescriptionToRepository(final MetricDescription metricDescription) {
        if (metricDescription.getRepository() != null) {
            return metricDescription;
        }

        // Find existing description based on metric UUID
        for (final Identifier identifiable : this.repository.getDescriptions()) {
            if (identifiable.getId().equals(metricDescription.getId())) {
                return (MetricDescription) identifiable;
            }
        }

        this.repository.getDescriptions().add(metricDescription);
        if (metricDescription instanceof MetricSetDescription) {
            for (final MetricDescription childMetricDescription : ((MetricSetDescription) metricDescription)
                    .getSubsumedMetrics()) {
                addMetricDescriptionToRepository(childMetricDescription);
            }
        }
        return metricDescription;
    }
}
