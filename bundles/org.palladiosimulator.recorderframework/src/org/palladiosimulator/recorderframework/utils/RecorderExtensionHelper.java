package org.palladiosimulator.recorderframework.utils;

import org.palladiosimulator.commons.eclipseutils.ExtensionHelper;
import org.palladiosimulator.recorderframework.core.config.IRecorderConfigurationFactory;

public class RecorderExtensionHelper {

    /** Extension point ID for the recorder framework. */
    protected static final String RECORDER_EXTENSION_POINT_ID = "org.palladiosimulator.recorderframework";
    /** Recorder element name of the extension point of the recorder framework. */
    protected static final String RECORDER_EXTENSION_ELEMENT = "recorder";
    /** Recorder extension attribute for the recorder name. */
    protected static final String RECORDER_EXTENSION_NAME_ATTRIBUTE = "name";

    /** Recorder extension attribute for the recorder configuration factory. */
    private static final String RECORDER_EXTENSION_CONFIGURATION_FACTORY_ATTRIBUTE = "configurationFactory";

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

    public RecorderExtensionHelper() {
        super();
    }

}