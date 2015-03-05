package org.palladiosimulator.recorderframework.edp2.config;

import java.util.Date;
import java.util.Map;

import org.palladiosimulator.commons.datastructureutils.MapHelper;
import org.palladiosimulator.edp2.models.ExperimentData.ExperimentDataFactory;
import org.palladiosimulator.edp2.models.ExperimentData.ExperimentRun;
import org.palladiosimulator.edp2.models.ExperimentData.ExperimentSetting;
import org.palladiosimulator.edp2.models.ExperimentData.MeasuringType;
import org.palladiosimulator.edp2.models.measuringpoint.MeasuringPoint;
import org.palladiosimulator.metricspec.MetricDescription;

/**
 * Factory for EDP2 recorder configuration objects that configure the recorder to store measurements
 * within experiment runs.
 * 
 * @author Sebastian Lehrig
 */
public class EDP2RecorderConfigurationFactory extends AbstractEDP2RecorderConfigurationFactory<ExperimentRun> {

    /** The experiment setting to be used; based on the current variation. */
    private ExperimentSetting experimentSetting;

    @Override
    public void initialize(final Map<String, Object> configuration) {
        super.initialize(configuration);

        initializeExperimentSetting(MapHelper.getValue(configuration, VARIATION_ID, String.class));
        initializeExperimentRun();
    }

    /**
     * A single run for a given experiment group and setting.
     */
    private void initializeExperimentRun() {
        this.experimentRun = ExperimentDataFactory.eINSTANCE.createExperimentRun();
        this.experimentRun.setStartTime(new Date());
        this.experimentSetting.getExperimentRuns().add(this.experimentRun);
        this.experimentGroup.getExperimentSettings().add(this.experimentSetting);
    }

    /**
     * Different settings shall refer to the same experiment group but apply a variation within a
     * group of experiments. For example, we could vary the user population within a closed workload
     * over different experiment settings.
     * 
     * @param variationID
     *            the variation to be used for this experiment setting.
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
    }

    @Override
    protected MeasuringType createMeasuringType(final MetricDescription measureMetric,
            final MeasuringPoint measuringPoint) {
        final MeasuringType measuringType = super.createMeasuringType(measureMetric, measuringPoint);
        measuringType.getExperimentSettings().add(this.experimentSetting);
        return measuringType;
    }
}
