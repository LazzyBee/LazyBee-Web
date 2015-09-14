package com.born2go.lazzybee.client.subpage;

import java.util.List;

import com.born2go.lazzybee.client.LazzyBee;
import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.born2go.lazzybee.gdatabase.shared.VocaList;
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
		HTMLPanel panel1 = new HTMLPanel("");
		Label vocaQ = new Label(v.getQ());
		vocaQ.getElement().setAttribute("style", "float: left; color: white; font-weight: bold; font-size: 15px;");
		Anchor speech = new Anchor("");
		speech.getElement().setInnerHTML("<i class=\"fa fa-volume-up fa-lg\"></i>");
		speech.getElement().setAttribute("style", "float: right; color: white; cusor: pointer");
		panel1.add(vocaQ); panel1.add(speech);
		panel1.getElement().setAttribute("style", "overflow: hidden; margin-bottom: 15px;");
		HTMLPanel panel2 = new HTMLPanel("");
		Label vocaLv = new Label("Level: " + v.getLevel());
		Label pronoun = new Label("");
		JSONValue a = JSONParser.parseStrict(v.getA());
		pronoun.setText(a.isObject().get("pronoun").toString().replaceAll("\"", ""));
		panel2.add(vocaLv); panel2.add(pronoun);
		vocaLv.getElement().setAttribute("style", "float: left; color: white");
		pronoun.getElement().setAttribute("style", "float: right; color: white");
		panel2.getElement().setAttribute("style", "overflow: hidden; margin-bottom: 15px;"); 
		vocaShowPanel.add(panel1);
		vocaShowPanel.add(panel2);
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
		Label nototal = new Label("KB: ");
		total.getElement().setAttribute("style", "float: left");
		yestotal.getElement().setAttribute("style", "float: right; color: green; margin-right: 15px;");
		nototal.getElement().setAttribute("style", "float: right; color: red");
		testInfoPanel.add(total);
		testInfoPanel.add(nototal);
		testInfoPanel.add(yestotal);
		Anchor btnYes = new Anchor("Đã Biết");
		Anchor btnNo = new Anchor("Chưa Biết");
		Anchor btnQuit = new Anchor("Dừng Bài Test");
		controlPanel.add(btnQuit);
		controlPanel.add(btnNo);
		controlPanel.add(btnYes);
		btnYes.setStyleName("TestTool_Obj2");
		btnNo.setStyleName("TestTool_Obj2");
		btnQuit.setStyleName("TestTool_Obj2");
		btnNo.getElement().setAttribute("style", "margin-right: 10px");
		btnYes.getElement().setAttribute("style", "margin-right: 10px");
		vocaShowPanel.getElement().setAttribute("style", "padding: 20px; background: #0e74af; margin-top: 20px; margin-bottom: 10px;");
		//-----
		addTestVoca(vocaShowPanel, listTestVoca.get(0));
	}
	
	@UiHandler("btnStartTesting")
	void onBtnStartTestingClick(ClickEvent e) {
		getListTestVoca();
	}

}
