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
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
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
		RootPanel.get("right_panel").getElement()
				.setAttribute("style", "display:none");
		RootPanel
				.get("wt_grouplist")
				.getElement()
				.setAttribute("style",
						"padding: 20px 30px 30px 30px; width: 100%");
		createTable();

	}

	/**
	 * this method create table contain list GroupVoca
	 */
	void createTable() {

		TextColumn<GroupVoca> clCreator = new TextColumn<GroupVoca>() {
			@Override
			public String getValue(GroupVoca v) {
				return v.getCreator();
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

		TextColumn<GroupVoca> clList = new TextColumn<GroupVoca>() {
			@Override
			public String getValue(GroupVoca v) {
				String l = v.getListVoca();
				if (l.length() <= 200)
					return l;
				else
					return l.substring(0, 200) + "...";
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

		Column<GroupVoca, String> clView = new Column<GroupVoca, String>(
				new ClickableTextCell(anchorRenderer)) {
			@Override
			public String getValue(GroupVoca object) {
				return "Xem";
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
					if (event.getColumn() == 3) {
						Window.Location.assign("/group/"
								+ event.getValue().getId());
					}
				}
			}
		});

		groupTable.setWidth("100%");
		groupTable.addColumn(clCreator, "Người tạo");
		groupTable.addColumn(clDes, "Mô tả");
		groupTable.addColumn(clList, "Danh sách từ");
		groupTable.addColumn(clView);
		groupTable.addColumn(clDelete);

		groupTable.setColumnWidth(clCreator, "150px");
		groupTable.setColumnWidth(clDes, "250px");
		groupTable.setColumnWidth(clView, "50px");
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
								// TODO Auto-generated method stub

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
			}
			else {
				LazzyBee.noticeBox.hide();
				btnNextPage.addStyleName("GroupList_Obj6_Disable");
			}
		}
		else {
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
		if((presentIndex - listDisplayGroup.size() + 1 - VocaList.pageSize) >= 1) {
			presentIndex = presentIndex - listDisplayGroup.size();
			lbPageNumber.setText((presentIndex + 1 - VocaList.pageSize) + " - " + presentIndex);
			listDisplayGroup.clear();
			listDisplayGroup.addAll(listGroup.subList(presentIndex - VocaList.pageSize, presentIndex));
			btnNextPage.removeStyleName("GroupList_Obj6_Disable");
			if((presentIndex + 1 - VocaList.pageSize) == 1)
				btnPreviousPage.addStyleName("GroupList_Obj6_Disable");
		}
		else
			if(presentIndex > 100) {
				presentIndex = 100;
				lbPageNumber.setText((presentIndex + 1 - VocaList.pageSize) + " - " + presentIndex);
				listDisplayGroup.clear();
				listDisplayGroup.addAll(listGroup.subList(presentIndex - VocaList.pageSize, presentIndex));
				btnNextPage.removeStyleName("GroupList_Obj6_Disable");
				btnPreviousPage.addStyleName("GroupList_Obj6_Disable");
			}
	}

}
