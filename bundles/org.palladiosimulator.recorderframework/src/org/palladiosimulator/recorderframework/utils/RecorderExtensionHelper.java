package org.palladiosimulator.recorderframework.utils;

import java.util.List;

import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.palladiosimulator.commons.eclipseutils.ExtensionHelper;
import org.palladiosimulator.recorderframework.IRecorder;
import org.palladiosimulator.recorderframework.config.IRecorderConfigurationFactory;

/**
 * Helper to load recorder-related extensions.
 * 
 * @author Sebastian Lehrig
 */
public class RecorderExtensionHelper {

    /** Extension point ID for the recorder framework. */
    private static final String RECORDER_EXTENSION_POINT_ID = "org.palladiosimulator.recorderframework";

    /** Recorder element name of the extension point of the recorder framework. */
    private static final String RECORDER_EXTENSION_ELEMENT = "recorder";

    /** Recorder extension attribute for the recorder implementation. */
    private static final String RECORDER_EXTENSION_RECORDER_IMPLEMENTATION_ATTRIBUTE = "recorderImplementation";

    /** Recorder extension attribute for the recorder configuration factory. */
    private static final String RECORDER_EXTENSION_CONFIGURATION_FACTORY_ATTRIBUTE = "configurationFactory";

    /** Recorder extension attribute for the recorder name. */
    private static final String RECORDER_EXTENSION_NAME_ATTRIBUTE = "name";

    /** Recorder extension attribute for the recorder launch configuration tab. */
    private static final String RECORDER_EXTENSION_LAUNCH_CONFIG_TAB_ATTRIBUTE = "launchConfigTab";

    /**
     * Gets all recorder names that are registered via recorder extension points.
     * 
     * @return the list of recorder names.
     */
    public static List<String> getRecorderNames() {
        return ExtensionHelper.getAttributes(RECORDER_EXTENSION_POINT_ID, RECORDER_EXTENSION_ELEMENT,
                RECORDER_EXTENSION_NAME_ATTRIBUTE);
    }

    /**
     * Gets all launch configuration tabs that are registered via recorder extension points.
     * 
     * @return the list of configuration tabs.
     */
    public static List<ILaunchConfigurationTab> getLaunchConfigTabs() {
        return ExtensionHelper.getExecutableExtensions(RECORDER_EXTENSION_POINT_ID, RECORDER_EXTENSION_ELEMENT,
                RECORDER_EXTENSION_LAUNCH_CONFIG_TAB_ATTRIBUTE);
    }

    /**
     * Gets the recorder configuration factory for the given recorder name via recorder extension
     * points.
     * 
     * @param recorderName
     *            the given recorder name.
     * @return the recorder configuration factory.
     */
    public static IRecorderConfigurationFactory getRecorderConfigurationFactoryForName(final String recorderName) {
        return ExtensionHelper.getExecutableExtension(RECORDER_EXTENSION_POINT_ID, RECORDER_EXTENSION_ELEMENT,
                RECORDER_EXTENSION_CONFIGURATION_FACTORY_ATTRIBUTE, RECORDER_EXTENSION_NAME_ATTRIBUTE, recorderName);
    }

    /**
     * Gets the recorder implementation for the given recorder name via recorder extension points.
     * 
     * @param recorderName
     *            the given recorder name.
     * @return the recorder implementation.
     */
    public static IRecorder instantiateRecorderImplementationForRecorder(final String recorderName) {
        return ExtensionHelper.getExecutableExtension(RECORDER_EXTENSION_POINT_ID, RECORDER_EXTENSION_ELEMENT,
                RECORDER_EXTENSION_RECORDER_IMPLEMENTATION_ATTRIBUTE, RECORDER_EXTENSION_NAME_ATTRIBUTE, recorderName);
    }

}
