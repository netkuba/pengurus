package com.pengurus.crm.client.panels.menu;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.pengurus.crm.client.AuthorizationManager;

public class ProjectsMenuPanel extends TabMenuPanel {

	public ProjectsMenuPanel() {
		super("Projects");
	    addButtonAll();
	    addButtonMine();
	    addButtonCreate();
	}
	
	public TabMenuPanel getPanel() {
		return new ProjectsMenuPanel();
	}
	
	private void addButtonCreate() {
		if(AuthorizationManager.hasExecutiveAccess()){ 
			Button b = new Button("Create New");
			b.addSelectionListener(new SelectionListener<ButtonEvent>(){
				@Override
				public void componentSelected(ButtonEvent ce) {
					
				}
			});
		    add(b);
		}
	}

	private void addButtonMine() {
		if(AuthorizationManager.canViewProjects()){ 
			Button b = new Button("Mine");
			b.addSelectionListener(new SelectionListener<ButtonEvent>(){
				@Override
				public void componentSelected(ButtonEvent ce) {
				//	new QuotesListPanelMine();
				}
			});
		    add(b);
		}
	}

	private void addButtonAll() {
		if(AuthorizationManager.canViewAllProjects()){ 
			Button b = new Button("All");
			b.addSelectionListener(new SelectionListener<ButtonEvent>(){
				@Override
				public void componentSelected(ButtonEvent ce) {
				//	new QuotesListPanelAll();
				}
			});
		    add(b);
		}
	}

}
