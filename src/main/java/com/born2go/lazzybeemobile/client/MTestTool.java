package com.born2go.lazzybeemobile.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.born2go.lazzybeemobile.shared.Common;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class MTestTool extends Composite {

	private static TestToolUiBinder uiBinder = GWT
			.create(TestToolUiBinder.class);

	interface TestToolUiBinder extends UiBinder<Widget, MTestTool> {
	}

	@UiField
	HTMLPanel container;

	Label checkTotal;

	private int totalCheck = 0; // Tong so tu user biet

	private Map<String, Boolean> testMap = new HashMap<String, Boolean>();

	Element btnStep_ONE;

	public MTestTool() {
		initWidget(uiBinder.createAndBindUi(this));
		container.setStyleName("mainMTestTool");
		btnStep_ONE = RootPanel.get("btnStartTesting").getElement();
		Event.sinkEvents(btnStep_ONE, Event.ONCLICK);
		Event.setEventListener(btnStep_ONE, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					DOM.getElementById("htmlIntroTest").setAttribute("style",
							"display:none");
					getTestStep_ONE();
				}

			}
		});

	}

	int totalVocaTest_ONE = 0;

	private void getTestStep_ONE() {
		LazzyBeeMobile.data_service
				.getTestVocaStep_One(new AsyncCallback<HashMap<String, String>>() {

					@Override
					public void onSuccess(HashMap<String, String> result) {
						if (result != null && !result.isEmpty()) {
							totalVocaTest_ONE = result.size();
							startTest_ONE(result);
						}

					}

					@Override
					public void onFailure(Throwable caught) {

					}
				});
	}

	int totalCheck_ONE = 0;

	private void startTest_ONE(HashMap<String, String> hmap) {
		container.clear();
		totalCheck = 0;
		testMap.clear();
		HTMLPanel testInfoPanel = new HTMLPanel("");
		HTMLPanel vocaShowPanel = new HTMLPanel("");
		HTMLPanel controlPanel = new HTMLPanel("");
		container.add(testInfoPanel);
		container.add(vocaShowPanel);
		container.add(controlPanel);
		testInfoPanel.setStyleName("MTestTool_Obj1");
		testInfoPanel.getElement().setAttribute("style",
				"padding: 10px; overflow: hidden;");
		Label total = new Label("Tổng: " + hmap.size() + " Từ");
		Label info = new Label(
				"(Đây là bài tự kiểm tra, hãy click để chọn các từ bạn đã biết)");
		checkTotal = new Label("B: " + totalCheck + " / " + hmap.size());
		total.getElement().setAttribute("style",
				"float: left; font-weight: bold;");
		info.setStyleName("i_testtool_info");
		checkTotal.setStyleName("i_testtool_checkTotal");
		testInfoPanel.add(total);
		testInfoPanel.add(checkTotal);
		testInfoPanel.add(info);
		Anchor btnStep_TWO = new Anchor("Tiếp tục");
		controlPanel.add(btnStep_TWO);
		controlPanel.setStyleName("i_testt_controlPanel");
		btnStep_TWO.setStyleName("MTestTool_Obj3");

		vocaShowPanel.getElement().setAttribute("style",
				"text-align: center; margin-bottom:40px;");
		hmapToServer.clear();
		// Get a set of the entries
		Set set = hmap.entrySet();
		// Get an iterator
		Iterator i = set.iterator();
		// Display elements
		while (i.hasNext()) {
			Map.Entry me = (Map.Entry) i.next();
			addTestVoca(vocaShowPanel, me.getValue().toString(), me.getKey()
					.toString(), hmap.size());
		}

		// -----
		btnStep_TWO.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getStep_TWO();
				totalCheck_ONE = totalCheck;
			}
		});

	}

	String cookie = null;
	String user_id = Common.USER_ID;
	int totalVocaTest_TWO = 0;

	private void getStep_TWO() {
		LazzyBeeMobile.data_service.getTestVocaStep_Two(hmapToServer,
				new AsyncCallback<HashMap<String, String>>() {

					@Override
					public void onFailure(Throwable caught) {

					}

					@Override
					public void onSuccess(HashMap<String, String> result) {
						if (result != null && !result.isEmpty()) {
							cookie = result.get(user_id);
							result.remove(user_id);
							startTest_TWO(result);
							totalVocaTest_TWO = result.size();
						}
					}
				});
	}

	int totalCheck_TWO = 0;

	private void startTest_TWO(HashMap<String, String> hmap) {
		container.clear();
		totalCheck = 0;
		testMap.clear();
		HTMLPanel testInfoPanel = new HTMLPanel("");
		HTMLPanel vocaShowPanel = new HTMLPanel("");
		HTMLPanel controlPanel = new HTMLPanel("");
		container.add(testInfoPanel);
		container.add(vocaShowPanel);
		container.add(controlPanel);
		testInfoPanel.setStyleName("MTestTool_Obj1");
		testInfoPanel.getElement().setAttribute("style",
				"padding: 10px; overflow: hidden;");
		Label total = new Label("Tổng: " + hmap.size() + " Từ");
		Label info = new Label(
				"(Đây là bài tự kiểm tra, hãy click để chọn các từ bạn đã biết)");
		checkTotal = new Label("B: " + totalCheck + " / " + hmap.size());
		total.getElement().setAttribute("style",
				"float: left; font-weight: bold;");
		info.setStyleName("i_testtool_info");
		checkTotal.setStyleName("i_testtool_checkTotal");
		testInfoPanel.add(total);
		testInfoPanel.add(checkTotal);
		testInfoPanel.add(info);
		Anchor btnStep_THREE = new Anchor("Tiếp tục");

		controlPanel.add(btnStep_THREE);

		controlPanel.setStyleName("i_testt_controlPanel");
		btnStep_THREE.setStyleName("MTestTool_Obj3");

		vocaShowPanel.getElement().setAttribute("style",
				"text-align: center; margin-bottom:40px;");
		// -----

		hmapToServer.clear();
		Set set = hmap.entrySet();
		Iterator i = set.iterator();
		while (i.hasNext()) {
			Map.Entry me = (Map.Entry) i.next();
			addTestVoca(vocaShowPanel, me.getValue().toString(), me.getKey()
					.toString(), hmap.size());

		}
		// -----
		btnStep_THREE.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getTest_THREE();
				totalCheck_TWO = totalCheck;
			}
		});

	}

	private void startTest_THREE() {
		container.clear();
		HTMLPanel testInfoPanel = new HTMLPanel("");
		final HTMLPanel vocaShowPanel = new HTMLPanel("");

		HTMLPanel controlPanel = new HTMLPanel("");
		container.add(testInfoPanel);
		container.add(vocaShowPanel);
		container.add(controlPanel);
		testInfoPanel.setStyleName("MTestTool_Obj1");
		testInfoPanel.getElement().setAttribute("style",
				"padding: 10px; overflow: hidden;");
		int totalTest = totalVocaTest_ONE + totalVocaTest_TWO;
		int totalCheck_TwoStep = totalCheck_ONE + totalCheck_TWO;
		Label total = new Label(totalCheck_TwoStep + " / " + totalTest + " Từ");
		Label info = new Label(
				"Là tổng số từ các bạn đã biết / tổng số từ các bạn đã test, chúc mừng các bạn, muốn xem dự đoán xem các bạn có số vốn từ là bao nhiêu? Hãy chọn XEM KẾT QUẢ");

		total.getElement().setAttribute("style",
				"float: left; font-weight: bold;");

		vocaShowPanel.getElement().setAttribute("style",
				"text-align: center; margin-bottom:40px;");

		info.setStyleName("i_testtool_info");

		testInfoPanel.add(total);

		testInfoPanel.add(info);
		final Anchor btnStep_FOUR = new Anchor("Xem kết quả");

		controlPanel.add(btnStep_FOUR);

		controlPanel.setStyleName("i_testt_controlPanel");
		btnStep_FOUR.setStyleName("MTestTool_Obj3");

		btnStep_FOUR.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getStest_FOUR(vocaShowPanel);
				btnStep_FOUR.setVisible(false);
				
			}
		});

	}

	String action = Common.ACTION;

	private void getTest_THREE() {
		String[] path = cookie.split("=");
		hmapToServer.put(user_id, path[1]);
		hmapToServer.put(action, "step_two");
		// result.put("", value);
		LazzyBeeMobile.data_service.getTestVocaStep_Three(hmapToServer, cookie,
				path[1], new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {

					}

					@Override
					public void onSuccess(String value) {
						if (value.length() >= 0) {
							cookie = value;
							startTest_THREE();
						}

					}
				});
	}

	HashMap<String, String> hmapToServer = new HashMap<String, String>();

	private void addTestVoca(HTMLPanel vocaShowPanel, final String v,
			final String key, final int size) {
		testMap.put(v, false);
		hmapToServer.put(key, "0");
		final HTMLPanel form = new HTMLPanel("");
		Label vocaQ = new Label(v);
		form.add(vocaQ);
		form.setStyleName("MTestTool_Obj5");
		vocaQ.setStyleName("itesttool_vocaq");
		vocaQ.getElement().setClassName("vocaq");
		vocaQ.getElement().setAttribute("style",
				"font-size: 14px; font-weight: bold; color: #eafd74");
		vocaShowPanel.add(form);
		Anchor btnForm = new Anchor();
		btnForm.setStyleName("i_testtool_btnForm");
		form.add(btnForm);
		// -----
		btnForm.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				if (testMap.get(v)) {
					form.getElement().setAttribute("style",
							"background:  #5A5A5A");
					totalCheck--;
					hmapToServer.put(key, "0");
				} else {
					form.getElement().setAttribute("style",
							"background: #009688");
					totalCheck++;
					hmapToServer.put(key, "1");

				}
				testMap.put(v, !testMap.get(v));
				checkTotal.setText("B: " + totalCheck + " / " + size);

			}
		});

	}

	private void getStest_FOUR(final HTMLPanel vocaShowPanel) {
		String[] path = cookie.split("=");
		LazzyBeeMobile.data_service.getTestVocaStep_Four(null, cookie, path[1],
				new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {

					}

					@Override
					public void onSuccess(String result) {

						if (result != null && !result.isEmpty())
							showResultTest(result);
					}
				});
	}

	private void showResultTest(String result) {
		container.clear();
		HTMLPanel testInfoPanel = new HTMLPanel("");
		testInfoPanel.setStyleName("MTestTool_Obj1");
		testInfoPanel.getElement().setAttribute("style",
				"padding: 10px; overflow: hidden;");
		container.add(testInfoPanel);

		Label info = new Label("Tổng số từ vựng của bạn được tính toán là:");
		info.setStyleName("i_testtool_info");
		info.getElement().setAttribute("style", "margin-top: 5px;");
		testInfoPanel.add(info);

		HTMLPanel vocaShowPanel = new HTMLPanel("");
		vocaShowPanel.getElement().setAttribute("style",
				"text-align: center; margin-bottom:40px;");
		container.add(vocaShowPanel);

		HTMLPanel htmlResult = new HTMLPanel("");
		vocaShowPanel.add(htmlResult);
		htmlResult.setStyleName("MTestTool_box");
		Label lbResult = new Label();
		lbResult.setStyleName("MTestTool_result");
		lbResult.setText(result);
		htmlResult.add(lbResult);
	}

}
