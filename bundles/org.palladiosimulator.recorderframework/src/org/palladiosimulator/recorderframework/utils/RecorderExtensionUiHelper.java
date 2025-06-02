package org.palladiosimulator.recorderframework.utils;

import java.util.List;

import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.palladiosimulator.commons.eclipseutils.ExtensionHelper;

/**
 * Helper to load recorder-related extensions.
 * 
 * @author Sebastian Lehrig
 */
public class RecorderExtensionUiHelper {
    /** Extension point ID for the recorder framework. */
    private static final String RECORDER_EXTENSION_POINT_ID = "org.palladiosimulator.recorderframework";
    /** Recorder element name of the extension point of the recorder framework. */
    private static final String RECORDER_EXTENSION_ELEMENT = "recorder";
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

}
