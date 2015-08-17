package com.born2go.lazzybee.client.widgets;

import java.util.ArrayList;
import java.util.List;

import com.born2go.lazzybee.client.LazzyBee;
import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

public class ListVocaView extends Composite {

	private static ListVocaViewUiBinder uiBinder = GWT
			.create(ListVocaViewUiBinder.class);

	interface ListVocaViewUiBinder extends UiBinder<Widget, ListVocaView> {
	}

	@UiField
	CellTable<Voca> vocaTable;
	@UiField Label lbTotal;

	private ListDataProvider<Voca> dataProvider = new ListDataProvider<Voca>();
	private List<Voca> listVoca = new ArrayList<Voca>();

	public ListVocaView() {
		initWidget(uiBinder.createAndBindUi(this));

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

		TextColumn<Voca> vocaPackageColumn = new TextColumn<Voca>() {
			@Override
			public String getValue(Voca v) {
				return v.getPackages();
			}
		};

		SafeHtmlRenderer<String> anchorRenderer = new AbstractSafeHtmlRenderer<String>() {
			@Override
			public SafeHtml render(String object) {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				sb.appendHtmlConstant("<a href=\"javascript:;\">")
						.appendEscaped(object).appendHtmlConstant("</a>");
				return sb.toSafeHtml();
			}
		};

		Column<Voca, String> viewVocaColumn = new Column<Voca, String>(
				new ClickableTextCell(anchorRenderer)) {
			@Override
			public String getValue(Voca object) {
				return "View";
			}
		};
		viewVocaColumn.setFieldUpdater(new FieldUpdater<Voca, String>() {
			@Override
			public void update(int index, Voca object, String value) {
				Window.Location.assign("/dictionary/#vocabulary/" + object.getQ());
			}
		});
		
		Column<Voca, String> editVocaColumn = new Column<Voca, String>(
				new ClickableTextCell(anchorRenderer)) {
			@Override
			public String getValue(Voca object) {
				return "Edit";
			}
		};
		editVocaColumn.setFieldUpdater(new FieldUpdater<Voca, String>() {
			@Override
			public void update(int index, Voca object, String value) {
				Window.Location.assign("/editor/#vocabulary/" + object.getQ());
			}
		});

		vocaTable.setWidth("100%");
		vocaTable.addColumn(vocaQColumn, "Vocabulary");
		vocaTable.addColumn(vocaLevelColumn, "Level");
		vocaTable.addColumn(vocaPackageColumn, "Package");
		vocaTable.addColumn(viewVocaColumn, "");
		vocaTable.addColumn(editVocaColumn, "");
		
		vocaTable.setRowStyles(new RowStyles<Voca>() {
			@Override
			public String getStyleNames(Voca row, int rowIndex) {
				return "ListVocaView_Obj3";
			}
		});

		dataProvider.addDataDisplay(vocaTable);
		listVoca = dataProvider.getList();

		getData();
	}

	void getData() {
		LazzyBee.data_service.getListVoca(new AsyncCallback<List<Voca>>() {
			@Override
			public void onSuccess(List<Voca> result) {
				listVoca.addAll(result);
				lbTotal.setText("Total: " + result.size());
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		});
	}

}
