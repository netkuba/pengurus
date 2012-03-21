package com.pengurus.crm.client.panels.center.job;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.DomEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.pengurus.crm.client.models.JobModel;
import com.pengurus.crm.shared.dto.QuoteDTO;

public class JobsListPanelQuoteEdit extends JobsListPanelQuote {

	ModelList ml;
	
	public JobsListPanelQuoteEdit(QuoteDTO quoteDTO){
		super(quoteDTO);
	}
	
	public JobsListPanelQuoteEdit getPanel() {
		setHeading("Jobs");
		setCollapsible(true);
		setAnimCollapse(true);
		collapse();
		ml = new ModelList();
		Button createButton = new Button("Create New Job", new SelectionListener<ButtonEvent>(){
			@Override
			public void componentSelected(ButtonEvent ce) {
				final Window w = new Window();
				final JobPanelCreate jobPanel = new JobPanelCreate();
				Listener<DomEvent> listenerCreateJob = new Listener<DomEvent>() { 
					@Override
					public void handleEvent(DomEvent be) {
					    //dodanie nowego JOBA do bazy danych
						if(jobPanel.getJobDTO() != null){
							ml.getGrid().stopEditing();
							ml.getStore().add(new JobModel(jobPanel.getJobDTO()));
							ml.getGrid().startEditing(0, 0);
							quoteDTO.getJobs().add(jobPanel.getJobDTO());
							w.hide();
						}
					}
				};
				Listener<DomEvent> listenerClose = new Listener<DomEvent>() { 
					@Override
					public void handleEvent(DomEvent be) {
						w.hide();
					}
				};
				w.setActive(true);
				w.add(jobPanel.getPanel(listenerClose,listenerCreateJob));
				w.setAutoWidth(true);
				w.setAutoHide(true);
				w.setEnabled(true);
				w.setClosable(false);
				w.setHeaderVisible(true);
				w.setAutoHide(false);
				w.setHeading("Create Job");
				w.show();
			}});
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(createButton);
		hp.add(ml);
		add(hp);
		return this;
	}
	
	

}