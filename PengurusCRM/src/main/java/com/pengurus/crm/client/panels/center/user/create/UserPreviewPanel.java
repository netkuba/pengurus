package com.pengurus.crm.client.panels.center.user.create;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.BoxComponent;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.pengurus.crm.client.models.PersonalDataModel;
import com.pengurus.crm.client.panels.center.MainPanel;
import com.pengurus.crm.shared.dto.BusinessClientDTO;
import com.pengurus.crm.shared.dto.ClientDTO;
import com.pengurus.crm.shared.dto.IndividualClientDTO;
import com.pengurus.crm.shared.dto.PersonalDataDTO;
import com.pengurus.crm.shared.dto.UserDTO;
import com.pengurus.crm.shared.dto.UserRoleDTO;
import com.pengurus.crm.shared.dto.WorkerDTO;

public class UserPreviewPanel extends MainPanel {

    private HorizontalPanel horizontalPanel;
    private PersonalDataPreview personalDataPreview;
    private ContentPanel agentsGridPanel;
    private FormData formData;
    private ListStore<PersonalDataModel> personalDataStore = new ListStore<PersonalDataModel>();
    private Grid<PersonalDataModel> agentsGrid;
    private FormPanel bussinesClientForm = new FormPanel();
    private FormData addFormData;
    private TextField<String> companyName, usersRole;
    final Window window = new Window();

    public UserPreviewPanel(UserDTO user) {
        createPersonalDataForm();
        createBussinesClientForm();
        createCompanyNameField();
        createUsersRoleField();
        createAgentGrid();
        createWindow();
        initUser(user);
        addVerticalPanel();
    }

    private void initUser(UserDTO user) {
        hideElements();
        if (user instanceof WorkerDTO) {
            initWorker((WorkerDTO) user);
        } else if (user instanceof ClientDTO) {
            initClient((ClientDTO) user);
        }
    }

    private void initClient(ClientDTO client) {
        if (client instanceof IndividualClientDTO) {
            initIndividualClient((IndividualClientDTO) client);
        } else {
            initBusinessClient((BusinessClientDTO) client);
        }
    }

    private void initWorker(WorkerDTO worker) {
        usersRole.setValue(getRoles(worker));
        personalDataPreview.add(usersRole);
        personalDataPreview.show();
        personalDataPreview.fromPersonalData(worker.getPersonalData());

    }

    private void initIndividualClient(IndividualClientDTO client) {
        personalDataPreview.show();
        usersRole.setValue(getRoles(client));
        personalDataPreview.add(usersRole);
        personalDataPreview.fromPersonalData(client.getPersonalData());
    }

    private void initBusinessClient(BusinessClientDTO client) {
        for (PersonalDataDTO personalDataDTO : client.getAgents()) {
            personalDataStore.insert(new PersonalDataModel(personalDataDTO),
                    personalDataStore.getCount());
        }
        bussinesClientForm.show();
        usersRole.setValue(getRoles(client));
        bussinesClientForm.add(usersRole);
        agentsGridPanel.show();
        companyName.setValue(client.getFullName());
    }

    protected void createWindow() {
        window.setPlain(true);
        window.setHeading("Agent personal data");
        window.setLayout(new FlowLayout());
        window.setAutoHeight(true);
        window.setAutoWidth(true);
        window.setAutoHide(true);
    }

    private void personalDataPopup(PersonalDataPreview personalDataForm) {
        window.removeAll();
        window.add(personalDataForm);
        Button cancelButton = new Button("Cancel",
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        window.hide();
                    }
                });
        personalDataForm.addButton(cancelButton);
        window.show();
    }

    protected void createAgentGrid() {
        GridCellRenderer<PersonalDataModel> buttonRenderer = new GridCellRenderer<PersonalDataModel>() {

            private boolean init;

            public Object render(final PersonalDataModel model,
                    String property, ColumnData config, final int rowIndex,
                    final int colIndex,
                    final ListStore<PersonalDataModel> store,
                    Grid<PersonalDataModel> grid) {
                if (!init) {
                    init = true;
                    grid.addListener(Events.ColumnResize,
                            new Listener<GridEvent<PersonalDataModel>>() {

                                public void handleEvent(
                                        GridEvent<PersonalDataModel> be) {
                                    for (int i = 0; i < be.getGrid().getStore()
                                            .getCount(); i++) {
                                        if (be.getGrid().getView()
                                                .getWidget(i, be.getColIndex()) != null
                                                && be.getGrid()
                                                        .getView()
                                                        .getWidget(
                                                                i,
                                                                be.getColIndex()) instanceof BoxComponent) {
                                            ((BoxComponent) be
                                                    .getGrid()
                                                    .getView()
                                                    .getWidget(i,
                                                            be.getColIndex()))
                                                    .setWidth(be.getWidth() - 10);
                                        }
                                    }
                                }
                            });
                }

                Button previewButton = new Button("Preview",
                        new SelectionListener<ButtonEvent>() {
                            @Override
                            public void componentSelected(ButtonEvent ce) {
                                final PersonalDataPreview personalDataForm = new PersonalDataPreview();
                                personalDataForm.fromPersonalData(model
                                        .getPersonalDataDTO());
                                personalDataPopup(personalDataForm);
                            }
                        });
                previewButton.setWidth(grid.getColumnModel().getColumnWidth(
                        colIndex) - 10);
                previewButton.setToolTip("Click to see details");

                return previewButton;
            }
        };

        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        ColumnConfig column = new ColumnConfig();
        column.setId("firstName");
        column.setHeader("First name");
        column.setWidth(100);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("lastName");
        column.setHeader("Last name");
        column.setWidth(100);
        configs.add(column);

        column = new ColumnConfig();
        column.setId("edit");
        column.setHeader("Details");
        column.setWidth(70);
        column.setRenderer(buttonRenderer);
        configs.add(column);

        personalDataStore = new ListStore<PersonalDataModel>();

        ColumnModel cm = new ColumnModel(configs);

        agentsGridPanel = new ContentPanel();
        agentsGridPanel.setBodyBorder(false);
        agentsGridPanel.setHeading("Agents");
        agentsGridPanel.setButtonAlign(HorizontalAlignment.CENTER);
        agentsGridPanel.setLayout(new FitLayout());
        agentsGridPanel.setSize(280, 200);

        agentsGrid = new Grid<PersonalDataModel>(personalDataStore, cm);
        agentsGrid.setStyleAttribute("borderTop", "none");
        agentsGrid.setAutoExpandColumn("firstName");
        agentsGrid.setBorders(true);
        agentsGridPanel.add(agentsGrid);
        agentsGridPanel.hide();

        bussinesClientForm.add(agentsGridPanel, addFormData);
    }

    private void createPersonalDataForm() {
        personalDataPreview = new PersonalDataPreview();
        personalDataPreview.setHeading("Personal data");
        personalDataPreview.setFrame(true);
        personalDataPreview.setLabelAlign(LabelAlign.TOP);
        personalDataPreview.hide();
    }

    private void addVerticalPanel() {
        horizontalPanel = new HorizontalPanel();
        horizontalPanel.setSpacing(20);
        horizontalPanel.add(personalDataPreview);
        horizontalPanel.add(bussinesClientForm);
        add(horizontalPanel);
    }

    private void createBussinesClientForm() {
        bussinesClientForm = new FormPanel();
        bussinesClientForm.setHeading("Company data");
        bussinesClientForm.setFrame(true);
        bussinesClientForm.setLabelAlign(LabelAlign.TOP);
        bussinesClientForm.hide();
    }

    private void createCompanyNameField() {
        companyName = new TextField<String>();
        companyName.setFieldLabel("Company name");
        companyName.setReadOnly(true);
        bussinesClientForm.add(companyName, formData);
    }

    private void createUsersRoleField() {
        usersRole = new TextField<String>();
        usersRole.setFieldLabel("User's roles");
        usersRole.setReadOnly(true);
    }

    private void hideElements() {
        bussinesClientForm.hide();
        window.hide();
        personalDataPreview.hide();
    }

    private String getRoles(UserDTO user){
        String result = "";
        ArrayList<String> list = new ArrayList<String>();
        for(UserRoleDTO role : user.getAuthorities())
            list.add(role.getName());
        Collections.sort(list);
        int size = list.size();
        for(String s : list){
            if(size > 1)
                result += s + ", ";
            else
                result += s;
            size--;
        }
        return result;
    }

}
