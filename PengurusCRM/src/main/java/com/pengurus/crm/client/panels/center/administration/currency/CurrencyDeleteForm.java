package com.pengurus.crm.client.panels.center.administration.currency;

import java.util.Set;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.pengurus.crm.client.models.CurrencyModel;
import com.pengurus.crm.client.service.AdministrationService;
import com.pengurus.crm.client.service.AdministrationServiceAsync;
import com.pengurus.crm.shared.dto.CurrencyTypeDTO;

public class CurrencyDeleteForm extends FormPanel {

    private ComboBox<CurrencyModel> combo;
    private Button deleteButton;

    public CurrencyDeleteForm() {
        createCombo();
        createButton();
    }

    private void createCombo() {
        combo = new ComboBox<CurrencyModel>();
        combo.setFieldLabel("Currency");
        combo.setEmptyText("Select a currency");

        final ListStore<CurrencyModel> list = new ListStore<CurrencyModel>();
        AsyncCallback<Set<CurrencyTypeDTO>> callback = new AsyncCallback<Set<CurrencyTypeDTO>>() {

            @Override
            public void onSuccess(Set<CurrencyTypeDTO> result) {
                for (CurrencyTypeDTO currency : result)
                    list.add(new CurrencyModel(currency));
            }

            @Override
            public void onFailure(Throwable caught) {
                MessageBox.info("Failure", "Uploading currencies has "
                        + "failed.", null);
            }
        };

        AdministrationServiceAsync service = (AdministrationServiceAsync) GWT
                .create(AdministrationService.class);
        service.getCurrency(callback);

        combo.setDisplayField("currency");
        list.sort("currency", SortDir.ASC);
        combo.setStore(list);
        combo.setTypeAhead(true);
        combo.setTriggerAction(TriggerAction.ALL);
        combo.setAllowBlank(false);
        add(combo);
    }

    private void createButton() {
        deleteButton = new Button("Delete",
                new SelectionListener<ButtonEvent>() {

                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        AsyncCallback<CurrencyTypeDTO> callback = new AsyncCallback<CurrencyTypeDTO>() {

                            @Override
                            public void onSuccess(CurrencyTypeDTO result) {
                                CurrencyModel mm = null;
                                for (CurrencyModel model : combo.getStore()
                                        .getModels())
                                    if (model.getCurrency().equals(
                                            result.getCurrency()))
                                        mm = model;
                                if (mm != null)
                                    combo.getStore().remove(mm);

                                MessageBox.info(
                                        "Success",
                                        "You have succesfully deleted "
                                                + mm.getCurrency()
                                                + " currency.", null);
                            }

                            @Override
                            public void onFailure(Throwable caught) {
                                MessageBox.info("Failure",
                                        "Deleting currency has failed.", null);
                            }
                        };

                        AdministrationServiceAsync service = (AdministrationServiceAsync) GWT
                                .create(AdministrationService.class);
                        service.deleteCurrency(combo.getValue()
                                .getCurrencyDTO(), callback);
                    }
                });

        addButton(deleteButton);
        setButtonAlign(HorizontalAlignment.CENTER);
        FormButtonBinding binding = new FormButtonBinding(this);
        binding.addButton(deleteButton);

    }

    public void addComboField(CurrencyTypeDTO currency) {
        combo.getStore().add(new CurrencyModel(currency));
    }
}