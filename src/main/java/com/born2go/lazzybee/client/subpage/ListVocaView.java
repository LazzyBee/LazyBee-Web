package com.born2go.lazzybee.client.subpage;

import java.util.ArrayList;
import java.util.List;

import com.born2go.lazzybee.client.LazzyBee;
import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.born2go.lazzybee.gdatabase.shared.nonentity.VocaList;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
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
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

public class ListVocaView extends Composite {

	private static ListVocaViewUiBinder uiBinder = GWT
			.create(ListVocaViewUiBinder.class);

	interface ListVocaViewUiBinder extends UiBinder<Widget, ListVocaView> {
	}

	@UiField CellTable<Voca> vocaTable;
//	@UiField Label lbTotal;
	@UiField Anchor btnPreviousPage;
	@UiField Anchor btnNextPage;
	@UiField Label lbPageNumber;

	private ListDataProvider<Voca> dataProvider = new ListDataProvider<Voca>();
	
	private String cursorStr = null;
//	private int totalVoca = 0;
	private int presentIndex = 0;
	private List<Voca> listVoca = new ArrayList<Voca>();
	private List<Voca> listDisplayVoca = new ArrayList<Voca>();

	public ListVocaView() {
		initWidget(uiBinder.createAndBindUi(this));
		DOM.getElementById("right_panel").setAttribute("style", "display: none");
		DOM.getElementById("wt_dictionary").setAttribute("style", "padding: 30px; width: 100%; float: left;");

		TextColumn<Voca> vocaQColumn = new TextColumn<Voca>() {
			@Override
			public String getValue(Voca v) {
				return v.getQ();
			}
		};

		TextColumn<Voca> vocaLevelColumn = new TextColumn<Voca>() {
			@Override
			public String getValue(Voca v) {
				return v.getLevel();
			}
		};

		TextColumn<Voca> vocaPronounColumn = new TextColumn<Voca>() {
			@Override
			public String getValue(Voca v) {
				JSONValue a = JSONParser.parseStrict(v.getA());
				return a.isObject().get("pronoun").toString().replaceAll("\"", "");
			}
		};
		
		Column<Voca, SafeHtml> vocaMeaningColumn = new Column<Voca, SafeHtml>(new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(Voca v) {
				JSONValue a = JSONParser.parseStrict(v.getA());
				JSONValue pac = a.isObject().get("packages");
				String[] packages = v.getPackages().split(",");
				if(packages.length > 1) {	
					if(pac.isObject().get(packages[1]) == null) {
						return SafeHtmlUtils.fromString("");
					}
					else {
						JSONValue defi = pac.isObject().get(packages[1]);
						String value = defi.isObject().get("meaning").toString().replaceAll("\"", "");
						if(value.length() >= 121)
							value = value.substring(0, 120) + "<br/>...";
						return SafeHtmlUtils.fromSafeConstant(value);
					}
				}	
				else
					return SafeHtmlUtils.fromString("");
			}
		};

		SafeHtmlRenderer<String> anchorRenderer = new AbstractSafeHtmlRenderer<String>() {
			@Override
			public SafeHtml render(String object) {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				sb.appendHtmlConstant("<a href=\"javascript:;\" target=\"_blank\">")
						.appendEscaped(object).appendHtmlConstant("</a>");
				return sb.toSafeHtml();
			}
		};

		Column<Voca, String> viewVocaColumn = new Column<Voca, String>(
				new ClickableTextCell(anchorRenderer)) {
			@Override
			public String getValue(Voca object) {
				return "Tra";
			}
		};
		viewVocaColumn.setFieldUpdater(new FieldUpdater<Voca, String>() {
			@Override
			public void update(int index, Voca object, String value) {
				Window.open("/vdict/#" + object.getQ(), "_blank", "");
			}
		});
		
		Column<Voca, String> editVocaColumn = new Column<Voca, String>(
				new ClickableTextCell(anchorRenderer)) {
			@Override
			public String getValue(Voca object) {
				return "Sửa";
			}
		};
		editVocaColumn.setFieldUpdater(new FieldUpdater<Voca, String>() {
			@Override
			public void update(int index, Voca object, String value) {
				Window.open("/editor/#vocabulary/" + object.getQ(), "_blank", "");
			}
		});

		vocaTable.setWidth("100%");
		vocaTable.addColumn(vocaQColumn, "Từ, cụm từ");
		vocaTable.addColumn(vocaLevelColumn, "Cấp độ");
		vocaTable.addColumn(vocaPronounColumn, "Phiên âm");
		vocaTable.addColumn(vocaMeaningColumn, "Nghĩa");
		vocaTable.addColumn(viewVocaColumn, "");
		vocaTable.addColumn(editVocaColumn, "");
		
		vocaTable.setColumnWidth(vocaLevelColumn, "80px");
		vocaTable.setColumnWidth(viewVocaColumn, "50px");
		vocaTable.setColumnWidth(editVocaColumn, "50px");
		
		vocaTable.setRowStyles(new RowStyles<Voca>() {
			@Override
			public String getStyleNames(Voca row, int rowIndex) {
				return "ListVocaView_Obj3";
			}
		});

		dataProvider.addDataDisplay(vocaTable);
		listDisplayVoca = dataProvider.getList();
		
		btnPreviousPage.addStyleName("ListVocaView_Obj6_Disable");
//		getTotal();
		getData();
	}

//	void getTotal() {
//		LazzyBee.data_service.getTotalVoca(new AsyncCallback<Integer>() {
//			@Override
//			public void onSuccess(Integer result) {
//				lbTotal.setText("Total: " + result);
//				totalVoca = result;
//				getData();
//			}
//			
//			@Override
//			public void onFailure(Throwable caught) {
//				LazzyBee.noticeBox.setNotice("! Đã có lỗi xảy ra trong quá trình tải");
//				LazzyBee.noticeBox.setAutoHide();
//			}
//		});
//	}
	
	void getData() {
		LazzyBee.noticeBox.setNotice("Đang tải...");
		LazzyBee.data_service.getListVoca(cursorStr, new AsyncCallback<VocaList>() {
			@Override
			public void onSuccess(VocaList result) {
				listVoca.addAll(result.getListVoca());
				listDisplayVoca.addAll(result.getListVoca());
				presentIndex = presentIndex + listDisplayVoca.size();
				if(presentIndex != 0)
					lbPageNumber.setText((presentIndex + 1 - listDisplayVoca.size()) + " - " + presentIndex);
				else
					lbPageNumber.setText("0 - 0");
				cursorStr = result.getCursorStr();
				LazzyBee.noticeBox.hide();
//				if(presentIndex == totalVoca)
//					btnNextPage.addStyleName("ListVocaView_Obj6_Disable");
			}

			@Override
			public void onFailure(Throwable caught) {
				LazzyBee.noticeBox.hide();
			}
		});
	}
	
	@UiHandler("btnNextPage")
	void onBtnNextPageClick(ClickEvent e) {
		if(presentIndex >= listVoca.size()) {
			LazzyBee.noticeBox.setNotice("Đang tải...");
			if(!cursorStr.equals("\\0")) {
				LazzyBee.data_service.getListVoca(cursorStr, new AsyncCallback<VocaList>() {
					@Override
					public void onSuccess(VocaList result) {
						if(!result.getListVoca().isEmpty()) {
							listVoca.addAll(result.getListVoca());
							listDisplayVoca.clear();
							listDisplayVoca.addAll(result.getListVoca());
							presentIndex = presentIndex + listDisplayVoca.size();
							lbPageNumber.setText((presentIndex + 1 - listDisplayVoca.size()) + " - " + presentIndex);
							//-----
							btnPreviousPage.removeStyleName("ListVocaView_Obj6_Disable");
//							if(presentIndex == totalVoca)
//								btnNextPage.addStyleName("ListVocaView_Obj6_Disable");
						}
						else
							btnNextPage.addStyleName("ListVocaView_Obj6_Disable");
						cursorStr = result.getCursorStr();
						LazzyBee.noticeBox.hide();
					}
		
					@Override
					public void onFailure(Throwable caught) {
						LazzyBee.noticeBox.hide();
					}
				});
			} else {
				LazzyBee.noticeBox.hide();
				btnNextPage.addStyleName("ListVocaView_Obj6_Disable");
			}
		} 
		else {
			presentIndex = presentIndex + VocaList.pageSize;
			int mi = 0;
			if(presentIndex >= listVoca.size()) {
				mi = presentIndex - listVoca.size();
				presentIndex = listVoca.size();
			}
			lbPageNumber.setText((presentIndex + mi + 1 - VocaList.pageSize) + " - " + presentIndex);
			listDisplayVoca.clear();
			listDisplayVoca.addAll(listVoca.subList(presentIndex + mi - VocaList.pageSize, presentIndex));
			//-----
			btnPreviousPage.removeStyleName("ListVocaView_Obj6_Disable");
//			if(presentIndex == totalVoca)
//				btnNextPage.addStyleName("ListVocaView_Obj6_Disable");
		}
	}
	
	@UiHandler("btnPreviousPage") 
	void onBtnPreviousPageClick(ClickEvent e) {
		if((presentIndex - listDisplayVoca.size() + 1 - VocaList.pageSize) >= 1) {
			presentIndex = presentIndex - listDisplayVoca.size();
			lbPageNumber.setText((presentIndex + 1 - VocaList.pageSize) + " - " + presentIndex);
			listDisplayVoca.clear();
			listDisplayVoca.addAll(listVoca.subList(presentIndex - VocaList.pageSize, presentIndex));
			btnNextPage.removeStyleName("ListVocaView_Obj6_Disable");
			if((presentIndex + 1 - VocaList.pageSize) == 1)
				btnPreviousPage.addStyleName("ListVocaView_Obj6_Disable");
		}
	}

}
