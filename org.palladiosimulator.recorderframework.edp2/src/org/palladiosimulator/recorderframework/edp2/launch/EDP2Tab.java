package org.palladiosimulator.recorderframework.edp2.launch;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.palladiosimulator.edp2.impl.RepositoryManager;
import org.palladiosimulator.edp2.models.Repository.Repository;
import org.palladiosimulator.edp2.ui.dialogs.datasource.ConfigureDatasourceDialog;
import org.palladiosimulator.recorderframework.edp2.EDP2RecorderConfigurationFactory;

public class EDP2Tab extends AbstractLaunchConfigurationTab {

    private Text dataField;

    protected String selectedRepositoryID;

    @Override
    public void createControl(final Composite parent) {
        final Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new GridLayout());
        setControl(container);

        final ModifyListener modifyListener = new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                EDP2Tab.this.setDirty(true);
                EDP2Tab.this.updateLaunchConfigurationDialog();
            }
        };

        final Group dataSetGroup = new Group(container, SWT.NONE);
        dataSetGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                false));
        final GridLayout gridLayout_2 = new GridLayout();
        gridLayout_2.numColumns = 3;
        dataSetGroup.setLayout(gridLayout_2);
        dataSetGroup.setText("Data Set");

        final Label dataSourceLabel = new Label(dataSetGroup, SWT.NONE);
        dataSourceLabel.setText("Data source:");

        dataField = new Text(dataSetGroup, SWT.BORDER | SWT.READ_ONLY);
        dataField
        .setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        dataField.addModifyListener(modifyListener);

        final Button browseButton = new Button(dataSetGroup, SWT.NONE);
        browseButton.setText("Browse...");
        browseButton.addSelectionListener(new SelectionAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see
             * org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse
             * .swt.events.SelectionEvent)
             */
            @Override
            public void widgetSelected(final SelectionEvent e) {
                final ConfigureDatasourceDialog dialog = new ConfigureDatasourceDialog(
                        e.display.getActiveShell(), "Select Datasource...",
                        true);
                if (dialog.open() == Dialog.OK) {
                    final Repository repository = (Repository) dialog.getResult();
                    selectedRepositoryID = repository.getId();
                    dataField.setText(repository.toString());
                }
            }
        });

    }

    @Override
    public String getName() {
        return "EDP2";
    }

    @Override
    public void initializeFrom(final ILaunchConfiguration configuration) {
        try {
            selectedRepositoryID = configuration.getAttribute(
                    EDP2RecorderConfigurationFactory.REPOSITORY_ID, "");
            final Repository repository = RepositoryManager.getRepositoryFromUUID(selectedRepositoryID);
            if(repository == null) {
                dataField.setText("");
            } else {
                dataField.setText(repository.toString());
            }
        } catch (final CoreException e) {
            selectedRepositoryID = "";
            dataField.setText("");
        }
    }

    @Override
    public void performApply(final ILaunchConfigurationWorkingCopy configuration) {
        configuration.setAttribute(EDP2RecorderConfigurationFactory.REPOSITORY_ID,
                selectedRepositoryID);

    }

    @Override
    public void setDefaults(final ILaunchConfigurationWorkingCopy configuration) {
        configuration.setAttribute(EDP2RecorderConfigurationFactory.REPOSITORY_ID, -1);
    }

    @Override
    public boolean isValid(final ILaunchConfiguration launchConfig) {
        final Repository repository = RepositoryManager.getRepositoryFromUUID(selectedRepositoryID);
        if (repository == null) {
            setErrorMessage("Data source is missing!");
            return false;
        }
        return true;
    }

    @Override
    public void activated(final ILaunchConfigurationWorkingCopy workingCopy) {}

    @Override
    public void deactivated(final ILaunchConfigurationWorkingCopy workingCopy) {}

}
