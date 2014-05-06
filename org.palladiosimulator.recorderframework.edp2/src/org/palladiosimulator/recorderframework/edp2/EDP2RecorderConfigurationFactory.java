package org.palladiosimulator.recorderframework.edp2;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.palladiosimulator.edp2.impl.RepositoryManager;
import org.palladiosimulator.edp2.models.ExperimentData.Edp2Measure;
import org.palladiosimulator.edp2.models.ExperimentData.ExperimentDataFactory;
import org.palladiosimulator.edp2.models.ExperimentData.ExperimentGroup;
import org.palladiosimulator.edp2.models.ExperimentData.ExperimentRun;
import org.palladiosimulator.edp2.models.ExperimentData.ExperimentSetting;
import org.palladiosimulator.edp2.models.ExperimentData.Measurements;
import org.palladiosimulator.edp2.models.Repository.Repository;
import org.palladiosimulator.metricspec.MetricDescription;
import org.palladiosimulator.metricspec.MetricSetDescription;
import org.palladiosimulator.recorderframework.AbstractRecorderConfiguration;
import org.palladiosimulator.recorderframework.AbstractRecorderConfigurationFactory;
import org.palladiosimulator.recorderframework.IRecorderConfigurationFactory;
import org.palladiosimulator.recorderframework.launch.IRecorderConfiguration;

import de.uka.ipd.sdq.identifier.Identifier;

public class EDP2RecorderConfigurationFactory extends AbstractRecorderConfigurationFactory implements IRecorderConfigurationFactory {

    public static final String REPOSITORY_ID = "EDP2RepositoryID";

    /** EDP2 Repository where data should be stored. */
    private Repository repository;

    /** EDP2 experiment group. */
    private ExperimentGroup experimentGroup;

    private ExperimentRun experimentRun;

    @Override
    public void initialize(final Map<String, Object> configuration) {
        super.initialize(configuration);

        initalizeEDP2Repository(getValue(configuration, REPOSITORY_ID, String.class));
        initializeExperimentGroup();
    }

    @Override
    public IRecorderConfiguration createRecorderConfiguration(final Map<String, Object> configuration) {
        final MetricDescription metricDescription = (MetricDescription) configuration.get(AbstractRecorderConfiguration.RECORDER_ACCEPTED_METRIC);
        addMetricDescriptionToRepository(metricDescription);

        final Measurements measure = initializeMeasurements(initializeEDP2Measure(metricDescription));
        final EDP2RecorderConfiguration result = new EDP2RecorderConfiguration();
        final Map<String,Object> newConfiguration = new HashMap<String, Object>();
        newConfiguration.putAll(configuration);
        newConfiguration.put(EDP2RecorderConfiguration.MEASUREMENTS, measure);
        result.setConfiguration(newConfiguration);

        return result;
    }

    private void initalizeEDP2Repository(final String repositoryID) {
        repository = RepositoryManager.getRepositoryFromUUID(repositoryID);
    }

    /**
     * Initializes an EDP2 ExperimentGroup if not present.
     * 
     * @param edp2MetaData
     */
    private void initializeExperimentGroup() {
        for (final ExperimentGroup group : repository.getExperimentGroups()) {
            if (group.getPurpose().equals(getExperimentName())) {
                experimentGroup = group;
                break;
            }
        }
        ExperimentSetting experimentSetting;
        if (experimentGroup == null) {
            experimentGroup = ExperimentDataFactory.eINSTANCE.createExperimentGroup();
            experimentGroup.setPurpose(getExperimentName());
            repository.getExperimentGroups().add(experimentGroup);

            experimentSetting = ExperimentDataFactory.eINSTANCE.createExperimentSetting();
            experimentSetting.setDescription("Basic Experiment Setting"); // TODO Enable sensitivity
            // Analysis here?
            experimentGroup.getExperimentSettings().add(experimentSetting);
        } else  {
            experimentSetting = experimentGroup.getExperimentSettings().get(0);
        }

        experimentRun = ExperimentDataFactory.eINSTANCE.createExperimentRun();
        experimentRun.setStartTime(new Date());
        experimentSetting.getExperimentRuns().add(experimentRun);
    }

    /**
     * Initialize EDP2 measure.
     * @param result
     * 
     * @param edp2MetaData
     *            Meta data object that holds the object to measure
     */
    private Edp2Measure initializeEDP2Measure(final MetricDescription measureMetric) {
        // Important: Identifiers are not supported by ProbeFramework so far
        // because ordinal values are used instead to represent nominal values.
        // If identifiers should be allowed, the initial identifier must
        // be set here.
        final String measuredObject = measureMetric.getTextualDescription();

        Edp2Measure measure;
        // Check for existing Edp2Measures in the experimentGroup
        for (final Edp2Measure edp2Measure : experimentGroup.getMeasure()) {
            if (edp2Measure.getMetric().equals(measureMetric)
                    && edp2Measure.getMeasuredObject().equals(measuredObject)) {
                measure = edp2Measure;
                return measure;
            }

        }

        // Create new Edp2Measure
        measure = ExperimentDataFactory.eINSTANCE.createEdp2Measure();
        measure.setMeasuredObject(measuredObject);
        measure.setMetric(measureMetric);
        measure.setExperimentGroup(experimentGroup);
        measure.getExperimentSettings().add(experimentGroup.getExperimentSettings().get(0));

        return measure;
    }

    /**
     * @param metricDescription
     * @return
     */
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
            for (final MetricDescription childMetricDescription : ((MetricSetDescription) metricDescription).getSubsumedMetrics()) {
                addMetricDescriptionToRepository(childMetricDescription);
            }
        }
        return metricDescription;
    }

    /**
     * Initialize EDP2 measurements.
     * 
     * @param edp2MetaData
     *            Meta data object that holds the model element ID to measure
     * @return
     */
    private Measurements initializeMeasurements(final Edp2Measure measure) {
        final Measurements measurements = ExperimentDataFactory.eINSTANCE.createMeasurements();
        measurements.setMeasure(measure);
        // TODO!
        // measurements.getAdditionalInformation().put("ModelElementID", edp2MetaData.getModelElementID());

        experimentRun.getMeasurements().add(measurements);

        return measurements;
    }
}
