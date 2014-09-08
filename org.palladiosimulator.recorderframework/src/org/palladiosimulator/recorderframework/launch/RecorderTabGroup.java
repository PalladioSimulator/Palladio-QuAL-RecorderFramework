package org.palladiosimulator.recorderframework.launch;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

/**
 * Enables the launch configuration tab group for a recorder as registered via the recorder
 * extension point.
 * 
 * @author Sebastian Lehrig
 */
public class RecorderTabGroup extends AbstractLaunchConfigurationTabGroup {

    @Override
    public void createTabs(final ILaunchConfigurationDialog dialog, final String mode) {
        List<ILaunchConfigurationTab> tabs = null;
        try {
            tabs = RecorderExtensionHelper.getLaunchConfigTabs();
        } catch (CoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        setTabs(tabs.toArray(new ILaunchConfigurationTab[tabs.size()]));
    }

}
