package com.pengurus.crm.client.panels.center.user.worker;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.DomEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.BoxComponent;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.pengurus.crm.client.models.WorkerModel;
import com.pengurus.crm.client.panels.center.user.BaseUsersListPanel;
import com.pengurus.crm.shared.dto.WorkerDTO;

public abstract class WorkersListPanel extends BaseUsersListPanel<WorkerModel>{

	protected WorkerDTO chosen;
	protected Listener<DomEvent> listenerChangeWorker;
	protected Listener<DomEvent> listenerCloseTab;

	public WorkerDTO getChosenWorker() {
		return chosen;
	}
	
	@Override
	protected String getName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected GridCellRenderer<WorkerModel> getButtonRenderer() {
		GridCellRenderer<WorkerModel> buttonRenderer = new GridCellRenderer<WorkerModel>() {

			private boolean init;

			public Object render(final WorkerModel model, String property,
					ColumnData config, final int rowIndex, final int colIndex,
					ListStore<WorkerModel> store, Grid<WorkerModel> grid) {
				if (!init) {
					init = true;
					grid.addListener(Events.OnClick,
							new Listener<GridEvent<WorkerModel>>() {

								public void handleEvent(
										GridEvent<WorkerModel> be) {
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

				Button b = new Button("Choose",
						new SelectionListener<ButtonEvent>() {
							@Override
							public void componentSelected(ButtonEvent ce) {
								chosen = model.getWorkerDTO();
							}
						});

				b.addListener(Events.OnClick, listenerCloseTab);
				b.addListener(Events.OnClick, listenerChangeWorker);
				b.setToolTip("Click to see");

				return b;
			}
		};
		return buttonRenderer;
	}
}
