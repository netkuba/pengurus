package com.pengurus.crm.client.panels.center.user;

import java.util.HashSet;
import java.util.Set;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.pengurus.crm.client.AuthorizationManager;
import com.pengurus.crm.client.models.UserModel;
import com.pengurus.crm.client.panels.ListPagination;
import com.pengurus.crm.client.panels.PaginationRpcProxy;
import com.pengurus.crm.client.panels.center.user.create.UserEditPanel;
import com.pengurus.crm.client.panels.center.user.create.UserPreviewPanel;
import com.pengurus.crm.client.service.PaginationService;
import com.pengurus.crm.client.service.PaginationServiceAsync;
import com.pengurus.crm.shared.dto.UserRoleDTO;
import com.pengurus.crm.shared.pagination.PagingCallbackWrapper;
import com.pengurus.crm.shared.pagination.PagingLoadConfigHelper;

public class UserListPanel extends BaseUsersListPanel<UserModel> {

	static UserListPanel instance;

	private LayoutContainer sideOptions;
	private CheckBoxGroup userRoles;
	private CheckBox allBox;

	private ListPagination<UserModel> listPagination;

	public static UserListPanel getIntance() {
		if (instance == null) {
			instance = new UserListPanel();
		}
		return instance;
	}

	public UserListPanel() {
		super(360);
		initPaging();
		modelList = new ModelList();
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSpacing(10);
		horizontalPanel.add(modelList);
		sideOptions = new LayoutContainer();
		createRoleCheckBoxGroup();
		horizontalPanel.add(sideOptions);
		add(horizontalPanel);
	}

	private void initPaging() {
		listPagination = new ListPagination<UserModel>(
				new PaginationRpcProxy<UserModel>() {
					@Override
					protected void load(PagingLoadConfigHelper loadConfig,
							AsyncCallback<PagingLoadResult<UserModel>> callback) {
						if (allBox.getValue()) {

							PaginationServiceAsync service = (PaginationServiceAsync) GWT
									.create(PaginationService.class);
							service.getPaginatedAllUsers(loadConfig,
									new PagingCallbackWrapper<UserModel>(
											callback));

						} else {

							Set<UserRoleDTO> roles = new HashSet<UserRoleDTO>();
							for (Field<?> field : userRoles.getAll()) {
								if (field instanceof UserRoleBox) {
									UserRoleBox roleBox = (UserRoleBox) field;
									if (roleBox.getValue()) {
										roles.add(roleBox.getUserRole());
									}
								}
							}
							PaginationServiceAsync service = (PaginationServiceAsync) GWT
									.create(PaginationService.class);
							service.getPaginatedUsersByRoles(loadConfig, roles,
									new PagingCallbackWrapper<UserModel>(
											callback));

						}
					}
				});
	}

	private class UserRoleBox extends CheckBox {
		private UserRoleDTO userRole;

		public UserRoleBox(String label, UserRoleDTO userRole) {
			super();
			setBoxLabel(label);
			addListener(Events.OnClick, new Listener<BaseEvent>() {
				@Override
				public void handleEvent(BaseEvent be) {
					refreshList();
				}
			});
			this.userRole = userRole;
		}

		public UserRoleDTO getUserRole() {
			return userRole;
		}
	}

	private void createRoleCheckBoxGroup() {
		userRoles = new CheckBoxGroup();
		userRoles.setOrientation(Orientation.VERTICAL);

		allBox = new CheckBox();
		allBox.setBoxLabel("All users");
		allBox.addListener(Events.OnClick, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				refreshList();
			}
		});

		userRoles.add(allBox);
		userRoles.add(new UserRoleBox("Role user", UserRoleDTO.ROLE_USER));
		userRoles.add(new UserRoleBox("Role client", UserRoleDTO.ROLE_CLIENT));
		userRoles.add(new UserRoleBox("Role expert", UserRoleDTO.ROLE_EXPERT));
		userRoles.add(new UserRoleBox("Role accountant",
				UserRoleDTO.ROLE_ACCOUNTANT));
		userRoles.add(new UserRoleBox("Role executive",
				UserRoleDTO.ROLE_EXECUTIVE));
		userRoles.add(new UserRoleBox("Role freelancer",
				UserRoleDTO.ROLE_FREELANCER));
		userRoles.add(new UserRoleBox("Role project manager",
				UserRoleDTO.ROLE_PROJECTMANAGER));
		userRoles.add(new UserRoleBox("Role verificator",
				UserRoleDTO.ROLE_VERIFICATOR));

		FieldSet fieldSet = new FieldSet();
		fieldSet.setHeading("User roles");
		fieldSet.add(userRoles);
		sideOptions.add(fieldSet);
	}

	private void deselectAllRoles() {
		for (Field<?> field : userRoles.getAll()) {
			if (field instanceof CheckBox) {
				CheckBox checkBox = (CheckBox) field;
				checkBox.setValue(false);
			}
		}
	}

	public void selectAll() {
		deselectAllRoles();
		allBox.setValue(true);
	}

	public void selectRole(UserRoleDTO role) {
		deselectAllRoles();
		for (Field<?> field : userRoles.getAll()) {
			if (field instanceof UserRoleBox) {
				UserRoleBox userRoleBox = (UserRoleBox) field;
				if (userRoleBox.getUserRole().equals(role)) {
					userRoleBox.setValue(true);
				}
			}
		}
	}

	@Override
	protected void addGridPaging(ContentPanel cp,
			final EditorGrid<UserModel> grid) {
		listPagination.addToGrid(cp, grid);
	}

	public void refreshList() {
		listPagination.getToolBar().refresh();
	}

	@Override
	protected GridCellRenderer<UserModel> getButtonRenderer() {
		GridCellRenderer<UserModel> buttonRenderer = new GridCellRenderer<UserModel>() {

			private boolean init;

			public Object render(final UserModel model, String property,
					ColumnData config, final int rowIndex, final int colIndex,
					ListStore<UserModel> store, Grid<UserModel> grid) {
				if (!init) {
					init = true;
				}

				ButtonBar buttonBar = new ButtonBar();
				
				Button previewButton = new Button(myConstants.previewButton(),
						new SelectionListener<ButtonEvent>() {
							@Override
							public void componentSelected(ButtonEvent ce) {
								new UserPreviewPanel(model.getUserDTO())
										.setAsMain();
							}
						});

				previewButton.setWidth((grid.getColumnModel().getColumnWidth(
						colIndex) - 22) / 2);
				previewButton.setToolTip("Click to see details");
				buttonBar.add(previewButton);

				if (AuthorizationManager.hasExecutiveAccess()) {
					Button editButton = new Button(myConstants.Edit(),
							new SelectionListener<ButtonEvent>() {
								@Override
								public void componentSelected(ButtonEvent ce) {
									new UserEditPanel(model.getUserDTO())
											.setAsMain();
								}
							});

					editButton.setWidth((grid.getColumnModel().getColumnWidth(
							colIndex) - 22) / 2);
					editButton.setToolTip("Click to edit");
					buttonBar.add(editButton);
				}

				return buttonBar;
			}
		};
		return buttonRenderer;
	}

	@Override
	protected String getName() {
		return "User list";
	}

	@Override
	protected ListStore<UserModel> getList() {
		return listPagination.getStore();
	}

}
