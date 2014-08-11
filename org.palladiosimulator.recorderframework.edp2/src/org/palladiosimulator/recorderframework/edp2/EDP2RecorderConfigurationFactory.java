package org.palladiosimulator.recorderframework.edp2;

import java.util.Date;
import java.util.Map;

import org.palladiosimulator.edp2.models.ExperimentData.ExperimentDataFactory;
import org.palladiosimulator.edp2.models.ExperimentData.ExperimentRun;
import org.palladiosimulator.edp2.models.ExperimentData.ExperimentSetting;
import org.palladiosimulator.edp2.models.ExperimentData.Measure;
import org.palladiosimulator.edp2.models.measuringpoint.MeasuringPoint;
import org.palladiosimulator.metricspec.MetricDescription;

public class EDP2RecorderConfigurationFactory extends AbstractEDP2RecorderConfigurationFactory<ExperimentRun> {

    private ExperimentSetting experimentSetting;    

    @Override
    public void initialize(final Map<String, Object> configuration) {
        super.initialize(configuration);

        initializeExperimentRun();
        initializeExperimentSetting(getValue(configuration, VARIATION_ID, String.class));        
    }
    
    /**
     * A single run for a given experiment group and setting.
     */
    private void initializeExperimentRun() {
        this.experimentRun = ExperimentDataFactory.eINSTANCE.createExperimentRun();
        this.experimentRun.setStartTime(new Date());
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
        this.experimentSetting.getExperimentRuns().add(this.experimentRun);
        this.experimentGroup.getExperimentSettings().add(this.experimentSetting);
    }
    
    

    /**
     * Initialize EDP2 measure.
     */
    @Override
    protected Measure createMeasure(final MetricDescription measureMetric, final MeasuringPoint measuringPoint) {
        final Measure measure = super.createMeasure(measureMetric, measuringPoint);
        measure.getExperimentSettings().add(this.experimentSetting);
        return measure;
    }
}
