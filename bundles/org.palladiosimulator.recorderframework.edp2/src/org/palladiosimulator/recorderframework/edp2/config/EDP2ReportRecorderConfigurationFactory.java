package org.palladiosimulator.recorderframework.edp2.config;

import java.util.Date;
import java.util.Map;

import org.palladiosimulator.edp2.models.ExperimentData.ExperimentDataFactory;
import org.palladiosimulator.edp2.models.ExperimentData.ExperimentGroupRun;

/**
 * Factory for EDP2 recorder configuration objects that configure the recorder to store measurements
 * within experiment group runs.
 * 
 * @author Sebastian Lehrig
 */
public class EDP2ReportRecorderConfigurationFactory extends
        AbstractEDP2RecorderConfigurationFactory<ExperimentGroupRun> {

    @Override
    public void initialize(final Map<String, Object> configuration) {
        super.initialize(configuration);

        initializeExperimentGroupRun();
    }

    /**
     * A single run for a given experiment group.
     */
    private void initializeExperimentGroupRun() {
        this.experimentRun = ExperimentDataFactory.eINSTANCE.createExperimentGroupRun();
        this.experimentRun.setStartTime(new Date());
        this.experimentGroup.getReports().add(this.experimentRun);
    }
}
