package org.palladiosimulator.recorderframework.launch;

import java.util.List;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.palladiosimulator.recorderframework.utils.RecorderExtensionHelper;

/**
 * Enables the launch configuration tab group for a recorder as registered via the recorder
 * extension point.
 * 
 * @author Sebastian Lehrig
 */
public class RecorderTabGroup extends AbstractLaunchConfigurationTabGroup {

    @Override
    public void createTabs(final ILaunchConfigurationDialog dialog, final String mode) {
        final List<ILaunchConfigurationTab> tabs = RecorderExtensionHelper.getLaunchConfigTabs();
        setTabs(tabs.toArray(new ILaunchConfigurationTab[tabs.size()]));
    }

}
