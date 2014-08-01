package org.palladiosimulator.recorderframework.edp2;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.palladiosimulator.edp2.impl.RepositoryManager;
import org.palladiosimulator.edp2.models.ExperimentData.ExperimentDataFactory;
import org.palladiosimulator.edp2.models.ExperimentData.ExperimentGroup;
import org.palladiosimulator.edp2.models.ExperimentData.ExperimentRun;
import org.palladiosimulator.edp2.models.ExperimentData.ExperimentSetting;
import org.palladiosimulator.edp2.models.ExperimentData.Measure;
import org.palladiosimulator.edp2.models.ExperimentData.Measurements;
import org.palladiosimulator.edp2.models.Repository.Repository;
import org.palladiosimulator.edp2.models.measuringpoint.MeasuringPoint;
import org.palladiosimulator.metricspec.MetricDescription;
import org.palladiosimulator.metricspec.MetricSetDescription;
import org.palladiosimulator.recorderframework.AbstractRecorderConfiguration;
import org.palladiosimulator.recorderframework.AbstractRecorderConfigurationFactory;
import org.palladiosimulator.recorderframework.IRecorderConfigurationFactory;
import org.palladiosimulator.recorderframework.launch.IRecorderConfiguration;

import de.uka.ipd.sdq.identifier.Identifier;

public class EDP2RecorderConfigurationFactory extends AbstractRecorderConfigurationFactory implements
        IRecorderConfigurationFactory {

    public static final String REPOSITORY_ID = "EDP2RepositoryID";
    public static final String VARIATION_ID = "variationId";

    /** EDP2 Repository where data should be stored. */
    private Repository repository;

    /** EDP2 experiment group. */
    private ExperimentGroup experimentGroup;

    private ExperimentSetting experimentSetting;

    private ExperimentRun experimentRun;

    @Override
    public void initialize(final Map<String, Object> configuration) {
        super.initialize(configuration);

        initalizeEDP2Repository(getValue(configuration, REPOSITORY_ID, String.class));

        initializeExperimentGroup();
        initializeExperimentSetting(getValue(configuration, VARIATION_ID, String.class));
        initializeExperimentRun();
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

    /**
     * Different settings shall refer to the same experiment group but apply a variation within a
     * group of experiment. For example, we could vary the user population within a closed workload
     * over different experiment settings.
     */
    private void initializeExperimentSetting(final String variationID) {
        // check for existing experiment setting
        for (final ExperimentSetting setting : this.experimentGroup.getExperimentSettings()) {
            if (setting.getDescription().equals(variationID)) {
                this.experimentSetting = setting;
                return;
            }
        }

        // create new experiment setting
        this.experimentSetting = ExperimentDataFactory.eINSTANCE.createExperimentSetting();
        this.experimentSetting.setDescription(variationID);
        this.experimentGroup.getExperimentSettings().add(this.experimentSetting);
    }

    /**
     * A single run for a given experiment group and setting.
     */
    private void initializeExperimentRun() {
        this.experimentRun = ExperimentDataFactory.eINSTANCE.createExperimentRun();
        this.experimentRun.setStartTime(new Date());
        this.experimentSetting.getExperimentRuns().add(this.experimentRun);
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

    /**
     * Initialize EDP2 measure.
     */
    private Measure initializeMeasure(final MetricDescription measureMetric, final MeasuringPoint measuringPoint) {
        // Check for existing Edp2Measures in the experimentGroup
        for (final Measure edp2Measure : this.experimentGroup.getMeasure()) {
            if (edp2Measure.getMetric().equals(measureMetric) && edp2Measure.getMeasuringPoint().equals(measuringPoint)) {
                return edp2Measure;
            }
        }

        // Create new Edp2Measure
        final Measure measure;
        measure = ExperimentDataFactory.eINSTANCE.createMeasure();
        measure.setMeasuringPoint(measuringPoint);
        measure.setMetric(measureMetric);
        measure.setExperimentGroup(this.experimentGroup);
        measure.getExperimentSettings().add(this.experimentSetting);
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

    @Override
    public void finalizeRecorderConfigurationFactory() {
        this.repository.flush();
    }
}
