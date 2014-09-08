package org.palladiosimulator.recorderframework.edp2;

import java.util.HashMap;
import java.util.Map;

import org.palladiosimulator.commons.datastructureutils.MapHelper;
import org.palladiosimulator.edp2.impl.RepositoryManager;
import org.palladiosimulator.edp2.models.ExperimentData.ExperimentDataFactory;
import org.palladiosimulator.edp2.models.ExperimentData.ExperimentGroup;
import org.palladiosimulator.edp2.models.ExperimentData.Measure;
import org.palladiosimulator.edp2.models.ExperimentData.Measurements;
import org.palladiosimulator.edp2.models.ExperimentData.Run;
import org.palladiosimulator.edp2.models.Repository.Repository;
import org.palladiosimulator.edp2.models.measuringpoint.MeasuringPoint;
import org.palladiosimulator.metricspec.MetricDescription;
import org.palladiosimulator.metricspec.MetricSetDescription;
import org.palladiosimulator.recorderframework.config.AbstractRecorderConfiguration;
import org.palladiosimulator.recorderframework.config.AbstractRecorderConfigurationFactory;
import org.palladiosimulator.recorderframework.config.IRecorderConfiguration;
import org.palladiosimulator.recorderframework.edp2.config.EDP2RecorderConfiguration;

import de.uka.ipd.sdq.identifier.Identifier;

public abstract class AbstractEDP2RecorderConfigurationFactory<RUN extends Run> extends
        AbstractRecorderConfigurationFactory {

    public static final String REPOSITORY_ID = "EDP2RepositoryID";
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

        initalizeEDP2Repository(MapHelper.getValue(configuration, REPOSITORY_ID, String.class));
        initializeExperimentGroup();
    }

    @Override
    public IRecorderConfiguration createRecorderConfiguration(final Map<String, Object> configuration) {
        final MetricDescription metricDescription = (MetricDescription) configuration
                .get(AbstractRecorderConfiguration.RECORDER_ACCEPTED_METRIC);
        addMetricDescriptionToRepository(metricDescription);

        final MeasuringPoint measuringPoint = (MeasuringPoint) configuration
                .get(AbstractRecorderConfiguration.MEASURING_POINT);

        final Measurements measure = initializeMeasurements(initializeMeasure(metricDescription, measuringPoint));
        final EDP2RecorderConfiguration result = new EDP2RecorderConfiguration();
        final Map<String, Object> newConfiguration = new HashMap<String, Object>();
        newConfiguration.putAll(configuration);
        newConfiguration.put(EDP2RecorderConfiguration.MEASUREMENTS, measure);
        result.setConfiguration(newConfiguration);

        return result;
    }

    private Measure initializeMeasure(final MetricDescription measureMetric, final MeasuringPoint measuringPoint) {
        // Check for existing Edp2Measures in the experimentGroup
        for (final Measure edp2Measure : this.experimentGroup.getMeasure()) {
            if (edp2Measure.getMetric().equals(measureMetric) && edp2Measure.getMeasuringPoint().equals(measuringPoint)) {
                return edp2Measure;
            }
        }

        return createMeasure(measureMetric, measuringPoint);
    }

    protected Measure createMeasure(final MetricDescription measureMetric, final MeasuringPoint measuringPoint) {
        final Measure measure;

        measure = ExperimentDataFactory.eINSTANCE.createMeasure();
        measure.setMeasuringPoint(measuringPoint);
        measure.setMetric(measureMetric);
        measure.setExperimentGroup(this.experimentGroup);

        return measure;
    }

    /**
     * Initialize EDP2 measurements.
     */
    private Measurements initializeMeasurements(final Measure measure) {
        final Measurements measurements = ExperimentDataFactory.eINSTANCE.createMeasurements();

        measurements.setMeasure(measure);
        this.experimentRun.getMeasurements().add(measurements);

        return measurements;
    }

    /**
     * Receives the EDP2 from the central repository registry, based on a given repository ID.
     * 
     * @param repositoryID
     *            the repository ID to check for.
     */
    private void initalizeEDP2Repository(final String repositoryID) {
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

    private MetricDescription addMetricDescriptionToRepository(final MetricDescription metricDescription) {
        if (metricDescription.getRepository() != null) {
            return metricDescription;
        }

        // Find existing description based on metric UUID
        for (final Identifier identifiable : repository.getDescriptions()) {
            if (identifiable.getId().equals(metricDescription.getId())) {
                return (MetricDescription) identifiable;
            }
        }

        repository.getDescriptions().add(metricDescription);
        if (metricDescription instanceof MetricSetDescription) {
            for (final MetricDescription childMetricDescription : ((MetricSetDescription) metricDescription)
                    .getSubsumedMetrics()) {
                addMetricDescriptionToRepository(childMetricDescription);
            }
        }
        return metricDescription;
    }

    @Override
    public void finalizeRecorderConfigurationFactory() {
        this.repository.flush();
    }
}
