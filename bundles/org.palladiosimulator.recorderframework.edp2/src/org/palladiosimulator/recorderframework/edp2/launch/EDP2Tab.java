package org.palladiosimulator.recorderframework.edp2.launch;

import java.util.Optional;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.jface.databinding.viewers.ObservableSetContentProvider;
import org.eclipse.jface.databinding.viewers.typed.ViewerProperties;
import org.eclipse.jface.resource.ResourceLocator;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.palladiosimulator.commons.ui.launch.AbstractDataBindingLaunchConfigurationTab;
import org.palladiosimulator.commons.ui.launch.ObservableLaunchConfigurationAttributeFactory;
import org.palladiosimulator.edp2.RepositoryAccess;
import org.palladiosimulator.edp2.models.Repository.Repository;
import org.palladiosimulator.edp2.ui.EDP2UIPlugin;
import org.palladiosimulator.edp2.ui.commands.AddDataSourceHandler;
import org.palladiosimulator.edp2.ui.commands.OpenDataSourceHandler;
import org.palladiosimulator.edp2.ui.dialogs.datasource.ParameterizedCommandTriggerMenu;
import org.palladiosimulator.recorderframework.edp2.Activator;
import org.palladiosimulator.recorderframework.edp2.config.EDP2RecorderConfigurationFactory;

/**
 * Configures an EDP2-specific launch configuration tab. This tab allows for adding and selecting
 * EDP2 repositories.
 *
 * @author Sebastian Lehrig, Sebastian Krach
 */
public class EDP2Tab extends AbstractDataBindingLaunchConfigurationTab {

    private ComboViewer dataField;

    protected RepositoryAccess repositoryAccess;
    protected WritableSet<Repository> repositorySet = WritableSet.withElementType(Repository.class);
    protected IObservableValue<Repository> selectedRepository;

    @Override
    protected void registerDataBindings(ObservableLaunchConfigurationAttributeFactory attributeFactory, DataBindingContext dbc) {
        updateRepositoryList();
        selectedRepository = attributeFactory.createFromStringAttribute(EDP2RecorderConfigurationFactory.REPOSITORY_ID,
                getRepositoryAccess().getAnyRepository().orElse(null), id -> getRepositoryAccess().getRepository(id).orElse(null),
                repo -> Optional.ofNullable(repo).map(Repository::getId).orElse(""));
    }
    
    @Override
    public void createControlInternal(final Composite parent, DataBindingContext dbc) {
        final Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new GridLayout());
        setControl(container);

        final Group dataSetGroup = new Group(container, SWT.NONE);
        dataSetGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        final GridLayout gridLayout2 = new GridLayout();
        gridLayout2.numColumns = 5;
        dataSetGroup.setLayout(gridLayout2);
        dataSetGroup.setText("Data Set");

        final Label dataSourceLabel = new Label(dataSetGroup, SWT.NONE);
        dataSourceLabel.setText("Data source:");

        dataField = new ComboViewer(dataSetGroup, SWT.BORDER | SWT.READ_ONLY);
        dataField.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        dataField.setContentProvider(new ObservableSetContentProvider<Repository>());
        
        dataField.setLabelProvider(new LabelProvider() {
            AdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
            @Override
            public String getText(Object element) {
                var labelProvider = (IItemLabelProvider)adapterFactory.adapt(element, IItemLabelProvider.class);
                if (labelProvider != null) {
                    return labelProvider.getText(element);
                }
                return element.toString();
            }
        });
        
        final Button addButton = new Button(dataSetGroup, SWT.NONE);
        ResourceLocator.imageDescriptorFromBundle(EDP2UIPlugin.class, "icons/add_datasource.gif").ifPresent(
                desc -> addButton.setImage(desc.createImage()));
        addButton.setToolTipText("Create data source");
        var addCommand = PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow()
                .getService(ICommandService.class)
                .getCommand(AddDataSourceHandler.COMMAND_ID);
        var menu = new ParameterizedCommandTriggerMenu(addCommand);
        menu.setCommandExecutedCallback(this::updateRepositoryList);
        menu.setItemTemplate("new %s");
        menu.registerWith(addButton, addButton::addSelectionListener);
        
        
        final Button openButton = new Button(dataSetGroup, SWT.NONE);
        ResourceLocator.imageDescriptorFromBundle(EDP2UIPlugin.class, "icons/data_source_open.gif").ifPresent(
                desc -> openButton.setImage(desc.createImage()));
        openButton.setToolTipText("Open data source");
        var openCommand = PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow()
                .getService(ICommandService.class)
                .getCommand(OpenDataSourceHandler.COMMAND_ID);
        var openMenu = new ParameterizedCommandTriggerMenu(openCommand);
        openMenu.registerWith(openButton, openButton::addSelectionListener);
        openMenu.setCommandExecutedCallback(this::updateRepositoryList);
        
        // Setup viewer data bindings
        dataField.setInput(repositorySet);
        dbc.bindValue(ViewerProperties.singleSelection(Repository.class).observe(dataField), selectedRepository);
    }

    @Override
    public String getName() {
        return "EDP2";
    }
    
    @Override
    public void initializeFrom(ILaunchConfiguration configuration) {
        updateRepositoryList();
        
        super.initializeFrom(configuration);
    }

    @Override
    public boolean isValid(final ILaunchConfiguration launchConfig) {
        setErrorMessage(null);
        
        if (selectedRepository.getValue() == null) {
            setErrorMessage("Data source is missing!");
            return false;
        }
        
        return super.isValid(launchConfig);
    }
    
    protected void updateRepositoryList() {
        var diff = Diffs.computeSetDiff(repositorySet, getRepositoryAccess().availableRepositories());
        diff.applyTo(repositorySet);
        if (diff.getAdditions().size() > 0) {
            var addition = diff.getAdditions().iterator().next();
            if (selectedRepository != null) {
                selectedRepository.setValue(addition);
            }
        }
    }
    
    protected RepositoryAccess getRepositoryAccess() {
        if (repositoryAccess == null) {
            var serviceRef = Activator.getCurrentContext().getServiceReference(RepositoryAccess.class);
            repositoryAccess = Activator.getCurrentContext().getService(serviceRef);    
        }
        return repositoryAccess;
    }

}
