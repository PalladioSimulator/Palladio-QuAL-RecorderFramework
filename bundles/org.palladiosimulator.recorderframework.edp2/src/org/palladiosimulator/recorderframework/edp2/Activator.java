package org.palladiosimulator.recorderframework.edp2;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin {

    /** The plug-in ID */
    public static final String PLUGIN_ID = "org.palladiosimulator.recorderframework.edp2";

    /** The Recorder name */
    public static final String EDP2_ID = "Experiment Data Persistency & Presentation (EDP2)";

    /** The shared instance */
    private static Activator plugin;

    private static BundleContext currentContext;

    /**
     * The constructor
     */
    public Activator() {
    }

    @Override
    public void start(final BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        currentContext = context;
    }

    @Override
    public void stop(final BundleContext context) throws Exception {
        plugin = null;
        currentContext = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     *
     * @return the shared instance
     */
    public static Activator getDefault() {
        return plugin;
    }
    
    public static BundleContext getCurrentContext() {
        return currentContext;
    }

}
