package com.born2go.lazzybee.client.subpage;

import java.util.List;

import com.born2go.lazzybee.client.LazzyBee;
import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.born2go.lazzybee.gdatabase.shared.nonentity.VocaList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class TestTool extends Composite {

	private static TestToolUiBinder uiBinder = GWT
			.create(TestToolUiBinder.class);

	interface TestToolUiBinder extends UiBinder<Widget, TestTool> {
	}
	
	@UiField HTMLPanel container;

	public TestTool() {
		initWidget(uiBinder.createAndBindUi(this));
		
		DOM.getElementById("wt_search_tool").setAttribute("style", "display: none");
	}
	
	void getListTestVoca() {
		LazzyBee.noticeBox.setLoading();
		LazzyBee.data_service.getListVoca(null, new AsyncCallback<VocaList>() {
			
			@Override
			public void onSuccess(VocaList result) {
				// TODO Auto-generated method stub
				LazzyBee.noticeBox.hide();
				startTesting(result.getListVoca());
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				LazzyBee.noticeBox.hide();
			}
		});
	}
	
	private void addTestVoca(HTMLPanel vocaShowPanel, Voca v) {
		HTMLPanel form = new HTMLPanel("");
		Label vocaQ = new Label(v.getQ());
		Label vocaLv = new Label("Lv: "+v.getLevel());
		form.add(vocaQ);form.add(vocaLv);
		form.getElement().setAttribute("style", "cursor: pointer; padding: 10px 20px 15px 20px; background: gray; margin-top: 20px; margin-bottom: 10px; display: inline-block; margin-right: 15px;");
		vocaQ.getElement().setAttribute("style", "float: left; display: block; font-size: 14px; font-weight: bold; color: aqua");
		vocaLv.getElement().setAttribute("style", "font-weight: bold; color: white; margin-top: 30px");
		vocaShowPanel.add(form);
	}
	
	void startTesting(List<Voca> listTestVoca) {
		container.clear();
		HTMLPanel testInfoPanel = new HTMLPanel("");
		HTMLPanel vocaShowPanel = new HTMLPanel("");
		HTMLPanel controlPanel = new HTMLPanel("");
		container.add(testInfoPanel);
		container.add(vocaShowPanel);
		container.add(controlPanel);
		testInfoPanel.setStyleName("TestTool_Obj1");
		testInfoPanel.getElement().setAttribute("style", "padding: 10px; overflow: hidden;");
		Label total = new Label("Tổng: " + listTestVoca.size());
		Label yestotal = new Label("B: ");
		total.getElement().setAttribute("style", "float: left; font-weight: bold;");
		yestotal.getElement().setAttribute("style", "float: right; font-weight:bold; color: forestgreen; margin-right: 0px;");
		testInfoPanel.add(total);
		testInfoPanel.add(yestotal);
		Anchor btnComplete = new Anchor("Hoàn Thành");
		Anchor btnQuit = new Anchor("Dừng Bài Test");
		controlPanel.add(btnComplete);
		controlPanel.add(btnQuit);
		controlPanel.getElement().setAttribute("style", "text-align: center; white-space: nowrap;");
		btnComplete.setStyleName("TestTool_Obj3");
		btnQuit.setStyleName("TestTool_Obj4");
		vocaShowPanel.getElement().setAttribute("style", "text-align: center; margin-bottom:40px;");
		//-----
		for(Voca v: listTestVoca)
			addTestVoca(vocaShowPanel, v);
	}
	
	@UiHandler("btnStartTesting")
	void onBtnStartTestingClick(ClickEvent e) {
		getListTestVoca();
	}

}
