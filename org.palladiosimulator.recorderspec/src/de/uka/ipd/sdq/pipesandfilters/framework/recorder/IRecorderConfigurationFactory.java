package de.uka.ipd.sdq.pipesandfilters.framework.recorder;

import java.util.Map;

import de.uka.ipd.sdq.pipesandfilters.framework.recorder.launch.IRecorderConfiguration;

public interface IRecorderConfigurationFactory {

    public void initialize(Map<String, Object> configuration);

    public IRecorderConfiguration createRecorderConfiguration(Map<String,Object> configuration);

}
