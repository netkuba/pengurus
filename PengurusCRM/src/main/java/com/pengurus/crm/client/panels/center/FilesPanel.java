package com.pengurus.crm.client.panels.center;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.RowNumberer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.pengurus.crm.client.models.FileModel;
import com.pengurus.crm.client.service.FileService;
import com.pengurus.crm.client.service.FileServiceAsync;

public class FilesPanel extends ContentPanel {

	private int quoteId;
	private int jobId;
	private int taskId;
	private int stateId;

	public FilesPanel(int quoteId, int jobId, int taskId, int stateId) {
		super();
		this.quoteId = quoteId;
		this.jobId = jobId;
		this.taskId = taskId;
		this.stateId = stateId;
		ListStore<FileModel> filesNames = getFilesNames(quoteId, jobId, taskId,
				stateId);
		createGrid(filesNames);

	}

	private void createGrid(final ListStore<FileModel> filesNames) {

		RowNumberer r = new RowNumberer();
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		configs.add(r);

		final CheckBoxSelectionModel<FileModel> sm = new CheckBoxSelectionModel<FileModel>();
		sm.setSelectionMode(SelectionMode.SINGLE);

		ColumnConfig column = new ColumnConfig();
		column.setId("name");
		column.setHeader("File name");
		column.setWidth(80);
		configs.add(sm.getColumn());
		configs.add(column);

		ColumnModel cm = new ColumnModel(configs);

		final Grid<FileModel> grid = new Grid<FileModel>(filesNames, cm);
		grid.addPlugin(r);
		grid.getView().setForceFit(true);
		grid.setSelectionModel(sm);

		setButtonAlign(HorizontalAlignment.CENTER);
		setHeading("Files");
		setLayout(new FitLayout());
		setSize(700, 300);
		add(grid);
		grid.getAriaSupport().setLabelledBy(getHeader().getId() + "-label");

		ToolBar toolBar = new ToolBar();

		Button upload = new Button("upload new file",
				new SelectionListener<ButtonEvent>() {

					@Override
					public void componentSelected(ButtonEvent ce) {
						createWindow();
					}

				});
		toolBar.add(upload);

		final Button remove = new Button("remove selected file",
				new SelectionListener<ButtonEvent>() {

					@Override
					public void componentSelected(ButtonEvent ce) {
						FileModel file = sm.getSelectedItem();
						if (file != null) {
							AsyncCallback<Void> callback = new AsyncCallback<Void>() {

								@Override
								public void onFailure(Throwable caught) {
									MessageBox.info("Succes", "Removing file: "
											+ sm.getSelectedItem().getName()
											+ " has failed", null);
								}

								@Override
								public void onSuccess(Void result) {
									MessageBox.info("Succes",
											"You have deleted file: "
													+ sm.getSelectedItem()
															.getName() + ".",
											null);
									grid.getStore()
											.remove(sm.getSelectedItem());
								}
							};
							FileServiceAsync service = (FileServiceAsync) GWT
									.create(FileService.class);
							service.deleteFile(quoteId, jobId, taskId, stateId,
									file.getName(), callback);

						} else
							MessageBox.info("Failure",
									"You must select a file first.", null);
					}
				});
		remove.disable();
		toolBar.add(remove);

		final Button download = new Button("download selected file",
				new SelectionListener<ButtonEvent>() {

					@Override
					public void componentSelected(ButtonEvent ce) {
						FileModel file = sm.getSelectedItem();
						if (file != null) {
							String url = "/file/download/" + quoteId + "/"
									+ jobId + "/" + taskId + "/" + stateId
									+ "/" + file.getName();
							com.google.gwt.user.client.Window.Location
									.assign(url);
						} else {
							MessageBox.info("Failure",
									"You must select a file first.", null);
						}
					}
				});
		download.disable();
		toolBar.add(download);

		setTopComponent(toolBar);

		sm.addSelectionChangedListener(new SelectionChangedListener<FileModel>() {

			@Override
			public void selectionChanged(SelectionChangedEvent<FileModel> se) {
				if (sm.getSelectedItem() != null) {
					remove.enable();
					download.enable();
				} else {
					remove.disable();
					download.disable();
				}
			}
		});

	}

	private void createWindow() {
		final Window window = new Window();
		window.setPlain(true);
		window.setHeading("Upload a file");
		window.setLayout(new FlowLayout());
		window.setAutoHeight(true);
		window.setAutoWidth(true);
		window.setAutoHide(true);

		final FormPanel form = new FormPanel();
		String url = "/file/upload/" + quoteId + "/" + jobId + "/" + taskId
				+ "/" + stateId;
		form.setAction(url);
		form.setEncoding(Encoding.MULTIPART);
		form.setMethod(Method.POST);
		form.setHeaderVisible(false);

		VerticalPanel verticalPanel = new VerticalPanel();
		form.add(verticalPanel);
		
		final FileUploadField fileUploadField = new FileUploadField();
		fileUploadField.setName("uploadedfile");
		fileUploadField.setAllowBlank(false);
		verticalPanel.add(fileUploadField);
		
		form.addListener(Events.Submit, new Listener<FormEvent>() {

			@Override
			public void handleEvent(FormEvent be) {
				MessageBox.info("Submit response", be.getResultHtml(), null);
			}
		});
		
		Button btn = new Button("Submit", new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (!form.isValid()) {
					return;
				}
				form.submit();
			}
		});
		Button cancelButton = new Button("Cancel",
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						window.hide();
					}
				});
		form.setButtonAlign(HorizontalAlignment.CENTER);
		form.addButton(btn);
		form.addButton(cancelButton);

		window.add(form);
		window.show();
	}

	private ListStore<FileModel> getFilesNames(int quoteId, int jobId,
			int taskId, int stateId) {
		final ListStore<FileModel> filesNames = new ListStore<FileModel>();
		AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				MessageBox.info("Failure",
						"Downloading files names has failed", null);
			}

			@Override
			public void onSuccess(List<String> result) {
				for (String name : result)
					filesNames.add(new FileModel(name));
			}

		};
		FileServiceAsync service = (FileServiceAsync) GWT
				.create(FileService.class);
		service.getFileList(quoteId, jobId, taskId, stateId, callback);
		return filesNames;
	}
}
