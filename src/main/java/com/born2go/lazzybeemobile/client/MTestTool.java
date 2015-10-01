package com.born2go.lazzybeemobile.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.born2go.lazzybee.client.LazzyBee;
import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.born2go.lazzybee.gdatabase.shared.nonentity.VocaList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class MTestTool extends Composite {

	private static TestToolUiBinder uiBinder = GWT
			.create(TestToolUiBinder.class);

	interface TestToolUiBinder extends UiBinder<Widget, MTestTool> {
	}

	@UiField
	HTMLPanel mainMTestTool;
	Label checkTotal;
	
	private Map<Voca, Boolean> result = new HashMap<Voca, Boolean>();
	private int totalCheck = 0;

	public MTestTool() {
		initWidget(uiBinder.createAndBindUi(this));
		mainMTestTool.setStyleName("mainMTestTool");

//		DOM.getElementById("wt_search_tool").setAttribute("style",
//				"display: none");
	}

	void getListTestVoca() {
		LazzyBee.noticeBox.setLoading();
		LazzyBee.data_service.getListVoca(null, new AsyncCallback<VocaList>() {

			@Override
			public void onSuccess(VocaList result) {
				// TODO Auto-generated method stub
				LazzyBee.noticeBox.hide();
				if (result.getListVoca().size() >= 30)
					startTesting(result.getListVoca().subList(0, 29));
				else
					startTesting(result.getListVoca());
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				LazzyBee.noticeBox.hide();
			}
		});
	}

	void addTestVoca(HTMLPanel vocaShowPanel, final Voca v) {
		result.put(v, false);
		final HTMLPanel form = new HTMLPanel("");
		Label vocaQ = new Label(v.getQ());
		Label vocaLv = new Label("Lv: " + v.getLevel());
		form.add(vocaQ);
		form.add(vocaLv);
		form.setStyleName("MTestTool_Obj5");
		vocaQ.getElement()
				.setAttribute("style",
						"float: left; display: block; font-size: 14px; font-weight: bold; color: aqua");
		vocaLv.getElement().setAttribute("style",
				"font-weight: bold; color: white; margin-top: 30px");
		vocaShowPanel.add(form);
		Anchor btnForm = new Anchor();
		btnForm.getElement().setAttribute("style", "position: absolute; top: 0px; left: 0px; width: 100%; height: 100%;");
		form.add(btnForm);
		//-----
		btnForm.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(result.get(v)) {
					form.getElement().setAttribute("style", "background: gray");
					totalCheck--;
				}
				else {
					form.getElement().setAttribute("style", "background: forestgreen");
					totalCheck++;
				}
				result.put(v, !result.get(v));
				checkTotal.setText("B: " + totalCheck);
			}
		});
	}

	void startTesting(List<Voca> listTestVoca) {
		mainMTestTool.clear();
		HTMLPanel testInfoPanel = new HTMLPanel("");
		HTMLPanel vocaShowPanel = new HTMLPanel("");
		HTMLPanel controlPanel = new HTMLPanel("");
		mainMTestTool.add(testInfoPanel);
		mainMTestTool.add(vocaShowPanel);
		mainMTestTool.add(controlPanel);
		testInfoPanel.setStyleName("MTestTool_Obj1");
		testInfoPanel.getElement().setAttribute("style",
				"padding: 10px; overflow: hidden;");
		Label total = new Label("Tổng: " + listTestVoca.size());
		checkTotal = new Label("B: " + totalCheck);
		total.getElement().setAttribute("style",
				"float: left; font-weight: bold;");
		checkTotal.getElement()
				.setAttribute("style",
						"float: right; font-weight:bold; color: forestgreen; margin-right: 0px;");
		testInfoPanel.add(total);
		testInfoPanel.add(checkTotal);
		Anchor btnComplete = new Anchor("Hoàn Thành");
		Anchor btnQuit = new Anchor("Dừng Bài Test");
		controlPanel.add(btnComplete);
		controlPanel.add(btnQuit);
		controlPanel
				.getElement()
				.setAttribute("style",
						"text-align: center; white-space: nowrap; margin-bottom: 60px;");
		btnComplete.setStyleName("MTestTool_Obj3");
		btnQuit.setStyleName("MTestTool_Obj4");
		vocaShowPanel.getElement().setAttribute("style",
				"text-align: center; margin-bottom:40px;");
		// -----
		for (Voca v : listTestVoca)
			addTestVoca(vocaShowPanel, v);
	}

	@UiHandler("btnStartTesting")
	void onBtnStartTestingClick(ClickEvent e) {
		getListTestVoca();
	}

}
