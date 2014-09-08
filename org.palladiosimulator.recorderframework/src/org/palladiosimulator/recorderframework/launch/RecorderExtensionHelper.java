package org.palladiosimulator.recorderframework.launch;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.palladiosimulator.commons.eclipseutils.ExtensionHelper;
import org.palladiosimulator.recorderframework.IRecorder;
import org.palladiosimulator.recorderframework.config.IRecorderConfigurationFactory;

public class RecorderExtensionHelper {

    private static final String RECORDER_EXTENSION_POINT_ID = "org.palladiosimulator.recorderframework";
    private static final String RECORDER_EXTENSION_ELEMENT = "recorder";

    private static final String RECORDER_EXTENSION_RECORDER_IMPLEMENTATION_ATTRIBUTE = "recorderImplementation";
    private static final String RECORDER_EXTENSION_CONFIGURATION_FACTORY_ATTRIBUTE = "configurationFactory";
    private static final String RECORDER_EXTENSION_NAME_ATTRIBUTE = "name";
    private static final String RECORDER_EXTENSION_LAUNCH_CONFIG_TAB_ATTRIBUTE = "launchConfigTab";

    public static List<String> getRecorderNames() throws CoreException {
        return ExtensionHelper.getAttributes(RECORDER_EXTENSION_POINT_ID, RECORDER_EXTENSION_ELEMENT,
                RECORDER_EXTENSION_NAME_ATTRIBUTE);
    }

    public static List<ILaunchConfigurationTab> getLaunchConfigTabs() throws CoreException {
        return ExtensionHelper.getExecutableExtensions(RECORDER_EXTENSION_POINT_ID, RECORDER_EXTENSION_ELEMENT,
                RECORDER_EXTENSION_LAUNCH_CONFIG_TAB_ATTRIBUTE);
    }

    public static IRecorderConfigurationFactory getRecorderConfigurationFactoryForName(final String recorderName)
            throws CoreException {
        return ExtensionHelper.getExecutableExtension(RECORDER_EXTENSION_POINT_ID, RECORDER_EXTENSION_ELEMENT,
                RECORDER_EXTENSION_CONFIGURATION_FACTORY_ATTRIBUTE, RECORDER_EXTENSION_NAME_ATTRIBUTE, recorderName);
    }

    public static IRecorder instantiateRecorderImplementationForRecorder(final String recorderName) {
        try {
            return ExtensionHelper.getExecutableExtension(RECORDER_EXTENSION_POINT_ID, RECORDER_EXTENSION_ELEMENT,
                    RECORDER_EXTENSION_RECORDER_IMPLEMENTATION_ATTRIBUTE, RECORDER_EXTENSION_NAME_ATTRIBUTE,
                    recorderName);
        } catch (final CoreException e) {
            throw new RuntimeException("Could not instantiate recorder implementation for recorder named "
                    + recorderName);
        }
    }

}
