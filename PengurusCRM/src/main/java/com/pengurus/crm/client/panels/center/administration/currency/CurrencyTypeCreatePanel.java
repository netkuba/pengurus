package com.pengurus.crm.client.panels.center.administration.currency;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.pengurus.crm.client.panels.center.MainPanel;
import com.pengurus.crm.client.service.AdministrationService;
import com.pengurus.crm.client.service.AdministrationServiceAsync;
import com.pengurus.crm.shared.dto.CurrencyTypeDTO;

public class CurrencyTypeCreatePanel extends MainPanel {

    private HorizontalPanel horizontalPanel;
    private FormPanel form;
    private TextField<String> currencyType;
    private FormData formData;
    private Button createButton;
    private CurrencyDeleteForm deleteForm;

    public CurrencyTypeCreatePanel() {
        createForm();
        createCurrencyField();
        createButton();
        createDeleteForm();
        addVerticalPanel();
    }

    private void createForm() {
        form = new FormPanel();
        form.setHeading("Create currency");
        form.setFrame(true);
        form.setPadding(25);
        form.setLabelAlign(LabelAlign.TOP);
    }

    private void createCurrencyField() {
        currencyType = new TextField<String>();
        currencyType.setFieldLabel("Currency");
        currencyType.setAllowBlank(false);
        form.add(currencyType, formData);
    }

    private void addVerticalPanel() {
        horizontalPanel = new HorizontalPanel();
        horizontalPanel.setSpacing(20);
        horizontalPanel.add(form);
        horizontalPanel.add(deleteForm);
        add(horizontalPanel);
    }

    private void createButton() {
        createButton = new Button("Create",
                new SelectionListener<ButtonEvent>() {

                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        AsyncCallback<CurrencyTypeDTO> callback = new AsyncCallback<CurrencyTypeDTO>() {

                            @Override
                            public void onSuccess(CurrencyTypeDTO result) {
                                deleteForm.addComboField(result);
                                MessageBox
                                        .info("Success",
                                                "You have succesfully created new currency.",
                                                null);
                            }

                            @Override
                            public void onFailure(Throwable caught) {
                                MessageBox
                                        .info("Failure",
                                                "Creating new currency has "
                                                        + "failed. The currency is"
                                                        + " already on the list.",
                                                null);
                            }
                        };

                        CurrencyTypeDTO currency = new CurrencyTypeDTO(
                                currencyType.getValue());
                        AdministrationServiceAsync service = (AdministrationServiceAsync) GWT
                                .create(AdministrationService.class);
                        service.createCurrency(currency, callback);
                    }
                });

        form.addButton(createButton);
        form.setButtonAlign(HorizontalAlignment.CENTER);
        FormButtonBinding binding = new FormButtonBinding(form);
        binding.addButton(createButton);
    }

    private void createDeleteForm() {
        deleteForm = new CurrencyDeleteForm();
        deleteForm.setHeading("Delete currency");
        deleteForm.setFrame(true);
        deleteForm.setPadding(25);
        deleteForm.setLabelAlign(LabelAlign.TOP);
    }

}