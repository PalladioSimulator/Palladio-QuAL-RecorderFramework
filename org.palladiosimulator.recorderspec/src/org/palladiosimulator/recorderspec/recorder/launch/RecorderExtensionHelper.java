package org.palladiosimulator.recorderspec.recorder.launch;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.palladiosimulator.recorderspec.recorder.IRecorderConfigurationFactory;
import org.palladiosimulator.recorderspec.recorder.Recorder;

public class RecorderExtensionHelper {

    public static String[] getRecorderNames() throws CoreException {
        final List<IExtension> recorderExtensions = loadExtensions("org.palladiosimulator.recorderspec.recorder");
        final List<String> names = new ArrayList<String>();
        for (final IExtension extension : recorderExtensions) {
            final IConfigurationElement e = obtainConfigurationElement("recorder",
                    extension);
            if (e != null) {
                names.add(e.getAttribute("name"));
            }
        }
        return names.toArray(new String[names.size()]);
    }

    public static ILaunchConfigurationTab[] getLaunchConfigTabs()
            throws CoreException {
        final List<IExtension> recorderExtensions = loadExtensions("org.palladiosimulator.recorderspec.recorder");
        final List<ILaunchConfigurationTab> tabList = new ArrayList<ILaunchConfigurationTab>();
        for (final IExtension extension : recorderExtensions) {
            final IConfigurationElement e = obtainConfigurationElement("recorder",
                    extension);
            if (e != null) {
                tabList.add((ILaunchConfigurationTab) e
                        .createExecutableExtension("launchConfigTab"));
            }
        }
        return tabList.toArray(new ILaunchConfigurationTab[tabList.size()]);
    }

    public static String getExtensionIdentifierForName(final String recorderName)
            throws CoreException {
        final List<IExtension> recorderExtensions = loadExtensions("org.palladiosimulator.recorderspec.recorder");
        for (final IExtension extension : recorderExtensions) {
            final IConfigurationElement e = obtainConfigurationElement("recorder",
                    extension);
            if (e != null && e.getAttribute("name").equals(recorderName)) {
                return extension.getUniqueIdentifier();
            }
        }
        return null;
    }

    public static IRecorderConfigurationFactory getRecorderConfigurationFactoryForName(
            final String recorderName) throws CoreException {
        final List<IExtension> recorderExtensions = loadExtensions("org.palladiosimulator.recorderspec.recorder");
        for (final IExtension extension : recorderExtensions) {
            final IConfigurationElement e = obtainConfigurationElement("recorder",
                    extension);
            if (e != null && e.getAttribute("name").equals(recorderName)) {
                final Object config = e.createExecutableExtension("configurationFactory");
                if (config != null) {
                    return (IRecorderConfigurationFactory) config;
                }
            }
        }
        return null;
    }

    public static Recorder instantiateWriteStrategyForRecorder(final String recorderName) {
        try {
            return (Recorder) instantiateExecutableExtension(recorderName, "writeStrategy");
        } catch (final CoreException e) {
            throw new RuntimeException("Could not instantiate write strategy for recorder named " + recorderName);
        }
    }

    private static Object instantiateExecutableExtension(final String recorderName, final String attributeName)
            throws CoreException {
        final List<IExtension> recorderExtensions = loadExtensions("org.palladiosimulator.recorderspec.recorder");
        for (final IExtension extension : recorderExtensions) {
            final IConfigurationElement e = obtainConfigurationElement("recorder",
                    extension);
            if (e != null && e.getAttribute("name").equals(recorderName)) {
                return e.createExecutableExtension(attributeName);
            }
        }
        return null;
    }

    public static String getNameForExtensionIdentifier(final String extensionID)
            throws CoreException {
        final IExtension ext = Platform.getExtensionRegistry().getExtensionPoint(
                "org.palladiosimulator.recorderspec.recorder")
                .getExtension(extensionID);
        if (ext != null) {
            final IConfigurationElement e = obtainConfigurationElement("recorder",
                    ext);
            return e.getAttribute("name");
        }
        return null;
    }

    private static IConfigurationElement obtainConfigurationElement(
            final String elementName, final IExtension extension) throws CoreException {
        final IConfigurationElement[] elements = extension.getConfigurationElements();
        for (final IConfigurationElement element : elements) {
            if (element.getName().equals(elementName)) {
                return element;
            }
        }
        return null;
    }

    private static List<IExtension> loadExtensions(final String extensionPointID) {
        final IExtension[] exts = Platform.getExtensionRegistry().getExtensionPoint(
                extensionPointID).getExtensions();
        final List<IExtension> results = new ArrayList<IExtension>();
        for (final IExtension extension : exts) {
            results.add(extension);
        }
        return results;
    }

}
