package com.born2go.lazzybee.client.subpage;

import java.util.ArrayList;
import java.util.List;

import com.born2go.lazzybee.client.LazzyBee;
import com.born2go.lazzybee.gdatabase.shared.GroupVoca;
import com.born2go.lazzybee.gdatabase.shared.nonentity.VocaList;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.google.gwt.view.client.ListDataProvider;

public class GroupVocaList extends Composite {

	private static GroupVocaListUiBinder uiBinder = GWT
			.create(GroupVocaListUiBinder.class);

	interface GroupVocaListUiBinder extends UiBinder<Widget, GroupVocaList> {
	}

	@UiField
	CellTable<GroupVoca> groupTable;
	@UiField
	Anchor btnPreviousPage;
	@UiField
	Anchor btnNextPage;
	@UiField
	Label lbPageNumber;

	private ListDataProvider<GroupVoca> dataProvider = new ListDataProvider<GroupVoca>();

	private String cursorStr = null;
	private int presentIndex = 0;
	private List<GroupVoca> listGroup = new ArrayList<GroupVoca>();
	private List<GroupVoca> listDisplayGroup = new ArrayList<GroupVoca>();

	public GroupVocaList() {
		initWidget(uiBinder.createAndBindUi(this));
		createTable();

	}

	/**
	 * this method create table contain list GroupVoca
	 */
	void createTable() {

		TextColumn<GroupVoca> clId = new TextColumn<GroupVoca>() {
			@Override
			public String getValue(GroupVoca v) {
				return String.valueOf(v.getId());
			}
		};

		TextColumn<GroupVoca> clDes = new TextColumn<GroupVoca>() {
			@Override
			public String getValue(GroupVoca v) {
				String des = v.getDescription();
				if (des.length() <= 200)
					return des;
				else
					return des.substring(0, 200) + "...";
			}
		};

		TextColumn<GroupVoca> clAmount = new TextColumn<GroupVoca>() {
			@Override
			public String getValue(GroupVoca v) {
				return countGroupVoca(v.getListVoca());
			}
		};

		SafeHtmlRenderer<String> anchorRenderer = new AbstractSafeHtmlRenderer<String>() {
			@Override
			public SafeHtml render(String object) {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				sb.appendHtmlConstant(
						"<a href=\"javascript:;\" target=\"_blank\">")
						.appendEscaped(object).appendHtmlConstant("</a>");
				return sb.toSafeHtml();
			}
		};

		Column<GroupVoca, String> clDelete = new Column<GroupVoca, String>(
				new ClickableTextCell(anchorRenderer)) {
			@Override
			public String getValue(GroupVoca object) {
				return "Xóa";
			}
		};
		clDelete.setFieldUpdater(new FieldUpdater<GroupVoca, String>() {
			@Override
			public void update(int index, GroupVoca object, String value) {
				deleteGroup(object);
			}
		});

		groupTable.addCellPreviewHandler(new Handler<GroupVoca>() {
			@Override
			public void onCellPreview(CellPreviewEvent<GroupVoca> event) {
				if (BrowserEvents.CLICK
						.equals(event.getNativeEvent().getType())) {
					if (event.getColumn() != 3) {
						showGroupVoca(event.getValue());
					}
				}
			}
		});

		groupTable.setWidth("100%");
		groupTable.addColumn(clId, "ID");
		groupTable.addColumn(clDes, "Mô tả");
		groupTable.addColumn(clAmount, "Số lượng từ");

		groupTable.addColumn(clDelete);

		groupTable.setColumnWidth(clId, "100px");
		groupTable.setColumnWidth(clDes, "200px");
		groupTable.setColumnWidth(clDelete, "50px");

		groupTable.setRowStyles(new RowStyles<GroupVoca>() {
			@Override
			public String getStyleNames(GroupVoca row, int rowIndex) {
				return "GroupList_Obj3";
			}
		});

		dataProvider.addDataDisplay(groupTable);
		listDisplayGroup = dataProvider.getList();

		btnPreviousPage.addStyleName("GroupList_Obj6_Disable");
		getData();
	}

	/**
	 * this method delete a exist GroupVoca in db
	 * 
	 * @param voca
	 *            : a GroupVoca
	 */
	void deleteGroup(final GroupVoca voca) {
		if (Window.confirm("Bạn muốn xóa từ danh sách từ này không ?")) {
			if (voca != null) {
				LazzyBee.data_service.removeGroup(voca.getId(),
						new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								LazzyBee.noticeBox
										.setNotice("! Đã có lỗi xảy ra khi tải lên");
								LazzyBee.noticeBox.setAutoHide();

							}

							@Override
							public void onSuccess(Void result) {
								listGroup.remove(voca);
								listDisplayGroup.remove(voca);
								LazzyBee.noticeBox
										.setNotice("Danh sách từ đã bị xóa");
								LazzyBee.noticeBox.setAutoHide();
								presentIndex--;
								if (presentIndex != 0)
									lbPageNumber
											.setText((presentIndex + 1 - listDisplayGroup
													.size()) + " - " + presentIndex);
								else
									lbPageNumber.setText("0 - 0");
							}
						});
			}

		}
	}

	void getData() {
		LazzyBee.noticeBox.setNotice("Đang tải...");
		LazzyBee.data_service.getListGroupVoca(cursorStr,
				new AsyncCallback<List<GroupVoca>>() {

					@Override
					public void onFailure(Throwable caught) {
						LazzyBee.noticeBox.hide();

					}

					@Override
					public void onSuccess(List<GroupVoca> result) {
						// get value of cursor
						cursorStr = result.get(0).getCreator();
						result.remove(0);
						listGroup.addAll(result);
						listDisplayGroup.addAll(result);
						presentIndex = presentIndex + listDisplayGroup.size();
						if (presentIndex != 0)
							lbPageNumber
									.setText((presentIndex + 1 - listDisplayGroup
											.size()) + " - " + presentIndex);
						else
							lbPageNumber.setText("0 - 0");
						LazzyBee.noticeBox.hide();

					}
				});
	}

	@UiHandler("btnNextPage")
	void onBtnNextPageClick(ClickEvent e) {
		if (presentIndex >= listGroup.size()) {
			LazzyBee.noticeBox.setNotice("Đang tải...");
			if (!cursorStr.equals("\\0")) {
				LazzyBee.data_service.getListGroupVoca(cursorStr,
						new AsyncCallback<List<GroupVoca>>() {

							@Override
							public void onFailure(Throwable caught) {
								LazzyBee.noticeBox.hide();
							}

							@Override
							public void onSuccess(List<GroupVoca> result) {
								cursorStr = result.get(0).getCreator();
								result.remove(0);
								if (!result.isEmpty()) {
									listGroup.addAll(result);
									listDisplayGroup.clear();
									listDisplayGroup.addAll(result);
									presentIndex = presentIndex
											+ listDisplayGroup.size();
									lbPageNumber
											.setText((presentIndex + 1 - listDisplayGroup
													.size())
													+ " - "
													+ presentIndex);
									// -----
									btnPreviousPage
											.removeStyleName("GroupList_Obj6_Disable");
								} else
									btnNextPage
											.addStyleName("GroupList_Obj6_Disable");

								LazzyBee.noticeBox.hide();

							}
						});
			} else {
				LazzyBee.noticeBox.hide();
				btnNextPage.addStyleName("GroupList_Obj6_Disable");
			}
		} else {
			presentIndex = presentIndex + VocaList.pageSize;
			int mi = 0;
			if (presentIndex >= listGroup.size()) {
				mi = presentIndex - listGroup.size();
				presentIndex = listGroup.size();
			}
			lbPageNumber.setText((presentIndex + mi + 1 - VocaList.pageSize)
					+ " - " + presentIndex);
			listDisplayGroup.clear();
			listDisplayGroup.addAll(listGroup.subList(presentIndex + mi
					- VocaList.pageSize, presentIndex));
			// -----
			btnPreviousPage.removeStyleName("GroupList_Obj6_Disable");

		}

	}

	@UiHandler("btnPreviousPage")
	void onBtnPreviousPageClick(ClickEvent e) {
		if ((presentIndex - listDisplayGroup.size() + 1 - VocaList.pageSize) >= 1) {
			presentIndex = presentIndex - listDisplayGroup.size();
			lbPageNumber.setText((presentIndex + 1 - VocaList.pageSize) + " - "
					+ presentIndex);
			listDisplayGroup.clear();
			listDisplayGroup.addAll(listGroup.subList(presentIndex
					- VocaList.pageSize, presentIndex));
			btnNextPage.removeStyleName("GroupList_Obj6_Disable");
			if ((presentIndex + 1 - VocaList.pageSize) == 1)
				btnPreviousPage.addStyleName("GroupList_Obj6_Disable");
		} else if (presentIndex > 100) {
			presentIndex = 100;
			lbPageNumber.setText((presentIndex + 1 - VocaList.pageSize) + " - "
					+ presentIndex);
			listDisplayGroup.clear();
			listDisplayGroup.addAll(listGroup.subList(presentIndex
					- VocaList.pageSize, presentIndex));
			btnNextPage.removeStyleName("GroupList_Obj6_Disable");
			btnPreviousPage.addStyleName("GroupList_Obj6_Disable");
		}
	}

	/**
	 * 
	 * @param list
	 *            : listVoca in a GroupVoca
	 * @return amount of voca in one listVoca
	 */
	String countGroupVoca(String list) {
		if (list != null && list.length() > 0) {
			list.replaceAll("</div>", "");
			String part[] = list.split("<div>");
			if (part != null && part.length > 0) {
				return String.valueOf(part.length);
			} else
				return "0";
		} else
			return "0";
	}

	/**
	 * this method show dialog a GroupVoca
	 * 
	 * @param g
	 *            : a GroupVoca
	 */
	void showGroupVoca(GroupVoca g) {
		final DialogBox d = new DialogBox();
		d.setStyleName("GroupList_Obj10");
		d.setAutoHideEnabled(true);
		d.setGlassEnabled(true);
		d.setAnimationEnabled(true);
		ScrollPanel sc = new ScrollPanel();
		sc.getElement().setAttribute("style",
				"overflow-x: hidden; padding: 20px 40px; height: 500px");
		GroupEditorTool editor = new GroupEditorTool();
		editor.setGroupVoca(g);
		sc.add(editor);
		d.add(sc);
		d.center();
		editor.onShowGroup();
		// -----
		editor.setListener(new GroupEditorTool.EditorListener() {
			@Override
			public void onClose() {
				d.hide();
			}

			@Override
			public void onUpdateGroup(GroupVoca v) {
				d.hide();
                refreshData(v, true);
			}

			@Override
			public void onDelete(GroupVoca v) {
				d.hide();
                refreshData(v, false);
				
			}

			 
		});
	}
	/**
	 * this method refresh table GroupVoca
	 * @param v: a GroupVoca
	 */
	void refreshData(GroupVoca v, boolean isUpdate) {
		int vindex = listGroup.indexOf(v);
		int dvindex = listDisplayGroup.indexOf(v);
		listGroup.remove(vindex);
		listDisplayGroup.remove(dvindex);
		if(isUpdate == true){
			listGroup.add(vindex, v);
			listDisplayGroup.add(dvindex, v);
		}
		presentIndex--;
		if (presentIndex != 0)
			lbPageNumber
					.setText((presentIndex + 1 - listDisplayGroup
							.size()) + " - " + presentIndex);
		else
			lbPageNumber.setText("0 - 0");
		
	}
	
}
