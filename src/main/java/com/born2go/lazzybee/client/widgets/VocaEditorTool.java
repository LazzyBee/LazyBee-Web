package com.born2go.lazzybee.client.widgets;

import com.born2go.lazzybee.client.LazzyBee;
import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class VocaEditorTool extends Composite {

	private static VocaEditorToolUiBinder uiBinder = GWT
			.create(VocaEditorToolUiBinder.class);

	interface VocaEditorToolUiBinder extends UiBinder<Widget, VocaEditorTool> {
	}
	
	@UiField TextBox txbVocaDefi;
	@UiField HTMLPanel defiTable;

	public VocaEditorTool() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	void addDefiPanel() {
		final HTML html = new HTML();
		html.setHTML("<br/> <div style='border-top: 1px solid silver; margin-bottom: 25px; margin-top: 10px;'></div>");
		defiTable.add(html);
		final HTMLPanel htmlp = new HTMLPanel("");
		//---
		HorizontalPanel hor1 = new HorizontalPanel();
		hor1.setStyleName("VocaEditorTool_Obj3");
		Label lb1 = new Label("Phân loại");
		lb1.setStyleName("VocaEditorTool_Obj2");
		ListBox lsb = new ListBox();
		lsb.setSize("409px", "28px");
		lsb.setStyleName("VocaEditorTool_Obj4");
		hor1.add(lb1);
		hor1.add(lsb);
		htmlp.add(hor1);
		//---
		HorizontalPanel hor2 = new HorizontalPanel();
		hor2.setStyleName("VocaEditorTool_Obj3");
		Label lb2 = new Label("Giải nghĩa");
		lb2.setStyleName("VocaEditorTool_Obj2");
		TextBox txb = new TextBox();
		txb.setStyleName("VocaEditorTool_Obj4");
		hor2.add(lb2);
		hor2.add(txb);
		htmlp.add(hor2);
		//---
		HorizontalPanel hor3 = new HorizontalPanel();
		hor3.setStyleName("VocaEditorTool_Obj3");
		Label lb3 = new Label("Ví dụ");
		lb3.setStyleName("VocaEditorTool_Obj2");
		TextBox txb2 = new TextBox();
		txb2.setStyleName("VocaEditorTool_Obj4");
		hor3.add(lb3);
		hor3.add(txb2);
		htmlp.add(hor3);
		//---
		HorizontalPanel hor4 = new HorizontalPanel();
		hor4.setStyleName("VocaEditorTool_Obj3");
		Label lb4 = new Label("Hình ảnh minh họa");
		lb4.setStyleName("VocaEditorTool_Obj2");
		Anchor ac = new Anchor();
		ac.setTitle("Upload");
		ac.setStyleName("VocaEditorTool_Obj6");
		ac.getElement().setInnerHTML("<i class='fa fa-upload fa-lg'></i>");
		hor4.add(lb4);
		hor4.add(ac);
		htmlp.add(hor4);
		//---
		Anchor deleteDefi = new Anchor("Xóa giải nghĩa này");
		deleteDefi.setStyleName("VocaEditorTool_Obj5 VocaEditorTool_Obj6");
		htmlp.add(deleteDefi);
		//---
		defiTable.add(htmlp);
		//---
		deleteDefi.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				defiTable.remove(htmlp);
				defiTable.remove(html);
			}
		});
	}
	
	@UiHandler("btnSave")
	void onBtnSaveClick(ClickEvent e) {
		Voca v = new Voca();
		v.setVoca(txbVocaDefi.getText());
		LazzyBee.data_service.insertVoca(v, new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				// TODO Auto-generated method stub
				Window.alert("insert complete!");
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	@UiHandler("btnAddDefi")
	void onBtnAddDefiClick(ClickEvent e) {
		addDefiPanel();
	}
	
	@UiHandler("btnGoTop")
	void onBtnGoTopClick(ClickEvent e) {
		Window.scrollTo(0, 0);
	}

}
