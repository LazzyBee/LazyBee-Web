package com.born2go.lazzybeemobile.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
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
	@UiField
	HTMLPanel htmlResultTest;
	@UiField
	HTMLPanel testgood;
	@UiField
	HTMLPanel testmedium;
	@UiField
	HTMLPanel testbad;
	@UiField
	HTMLPanel actionForm;
	@UiField
	HTMLPanel shareForm;
	@UiField
	Label testgoodlv;
	@UiField
	Label testmediumlv;
	@UiField
	Label testbadlv;
	@UiField
	Label lbTest1;
	@UiField
	Label lbTest2;
	@UiField
	Label resultTestGood;
	@UiField
	Label resultTestMedium;
	@UiField
	Label resultTestBad;
	@UiField
	Anchor btnAgainTesting;
	@UiField
	Anchor btnNextTesting;

	Label checkTotal;

	private int totalCheck = 0; // Tong so tu user biet
	private int testLevel = 2; // Level test default khi bat dau
	private Map<String, Boolean> testMap = new HashMap<String, Boolean>();

	Element btnStep_ONE;

	public MTestTool() {
		initWidget(uiBinder.createAndBindUi(this));
		container.setStyleName("mainMTestTool");
		actionForm.setStyleName("actionForm");
		shareForm.setStyleName("actionForm");
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

	int totalVocaTest = 0;

	private void getTestStep_ONE() {
		LazzyBeeMobile.data_service
				.getTestVocaStep_One(new AsyncCallback<List<String>>() {

					@Override
					public void onSuccess(List<String> result) {
						if (result != null && !result.isEmpty()) {
							totalVocaTest = result.size();
							startTest_ONE(result);
						}

					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}
				});
	}

	private void startTest_ONE(List<String> test) {
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
		Label total = new Label("Tổng: " + test.size() + " Từ");
		Label info = new Label(
				"(Đây là bài tự kiểm tra, hãy click để chọn các từ bạn đã biết)");
		checkTotal = new Label("B: " + totalCheck + " / " + test.size());
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
		// -----
		for (String v : test)
			addTestVoca(vocaShowPanel, v);
		// -----
		btnStep_TWO.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// getTestResult();
				getStep_TWO();
			}
		});

	}

	String cookie = null;

	private void getStep_TWO() {
		LazzyBeeMobile.data_service.getTestVocaStep_Two(null,
				new AsyncCallback<List<String>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(List<String> result) {
						if (result != null && !result.isEmpty()) {
							cookie = result.get(0);
							List<String> voca_two = result.subList(1,
									result.size());
							totalVocaTest = totalVocaTest + voca_two.size();
							startTest_TWO(voca_two);

						}
					}
				});
	}

	private void startTest_TWO(List<String> test) {
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
		Label total = new Label("Tổng: " + test.size() + " Từ");
		Label info = new Label(
				"(Đây là bài tự kiểm tra, hãy click để chọn các từ bạn đã biết)");
		checkTotal = new Label("B: " + totalCheck + " / " + test.size());
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
		for (String v : test)
			addTestVoca(vocaShowPanel, v);
		// -----
		btnStep_THREE.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// getTestResult();
				getTest_THREE();
			}
		});

	}

	private void startTest_THREE(List<String> test) {
		container.clear();

		testMap.clear();
		HTMLPanel testInfoPanel = new HTMLPanel("");
		final HTMLPanel vocaShowPanel = new HTMLPanel("");

		HTMLPanel controlPanel = new HTMLPanel("");
		container.add(testInfoPanel);
		container.add(vocaShowPanel);
		container.add(controlPanel);
		testInfoPanel.setStyleName("MTestTool_Obj1");
		testInfoPanel.getElement().setAttribute("style",
				"padding: 10px; overflow: hidden;");
		Label total = new Label(totalVocaTest + " Từ");
		Label info = new Label(
				"Là tổng số từ các bạn đã test, chúc mừng các bạn, muốn xem dự đoán xem các bạn có số vốn từ là bao nhiêu? Hãy chọn KẾT THÚC");

		total.getElement().setAttribute("style",
				"float: left; font-weight: bold;");

		vocaShowPanel.getElement().setAttribute("style",
				"text-align: center; margin-bottom:40px;");

		info.setStyleName("i_testtool_info");

		testInfoPanel.add(total);

		testInfoPanel.add(info);
		Anchor btnStep_FOUR = new Anchor("Kết thúc");

		controlPanel.add(btnStep_FOUR);

		controlPanel.setStyleName("i_testt_controlPanel");
		btnStep_FOUR.setStyleName("MTestTool_Obj3");

		btnStep_FOUR.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// getTestResult();
				getStest_FOUR(vocaShowPanel);
			}
		});

	}

	private void getTest_THREE() {
		String[] path = cookie.split("=");
		LazzyBeeMobile.data_service.getTestVocaStep_Three(null, cookie,
				path[1], new AsyncCallback<List<String>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(List<String> result) {
						if (result != null && !result.isEmpty()) {
							cookie = result.get(0);
							List<String> voca_three = result.subList(1,
									result.size());
							startTest_THREE(voca_three);
						}

					}
				});
	}

	private void addTestVoca(HTMLPanel vocaShowPanel, final String v) {
		testMap.put(v, false);
		final HTMLPanel form = new HTMLPanel("");
		Label vocaQ = new Label(v);
		form.add(vocaQ);
		form.setStyleName("MTestTool_Obj5");
		vocaQ.setStyleName("itesttool_vocaq");
		vocaShowPanel.add(form);
		Anchor btnForm = new Anchor();
		btnForm.setStyleName("i_testtool_btnForm");
		form.add(btnForm);
		// -----
		btnForm.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (testMap.get(v)) {
					form.getElement().setAttribute("style", "background: gray");
					totalCheck--;
				} else {
					form.getElement().setAttribute("style",
							"background: forestgreen");
					totalCheck++;
				}
				testMap.put(v, !testMap.get(v));
				checkTotal.setText("B: " + totalCheck + " / 20");

			}
		});
	}

	private void getStest_FOUR(final HTMLPanel vocaShowPanel) {
		String[] path = cookie.split("=");
		LazzyBeeMobile.data_service.getTestVocaStep_Four(null, cookie, path[1],
				new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(String result) {
						Window.alert("value: " + result);
						vocaShowPanel.setStyleName("MTestTool_box");
						Label lbResult = new Label();
						lbResult.setStyleName("MTestTool_result");
						lbResult.setText(result);
						vocaShowPanel.add(lbResult);

					}
				});
	}

	private void startTesting(List<String> test) {
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

		Label total = new Label("Tổng: " + test.size() + " Từ");
		Label info = new Label(
				"(Đây là bài tự kiểm tra, hãy click để chọn các từ bạn đã biết)");
		checkTotal = new Label("B: " + totalCheck + " / 20");
		total.getElement().setAttribute("style",
				"float: left; font-weight: bold;");
		info.setStyleName("i_testtool_info");
		checkTotal.setStyleName("i_testtool_checkTotal");
		testInfoPanel.add(total);
		testInfoPanel.add(checkTotal);
		testInfoPanel.add(info);
		Anchor btnComplete = new Anchor("Hoàn Thành");
		Anchor btnQuit = new Anchor("Dừng Bài Test");
		controlPanel.add(btnComplete);
		controlPanel.add(btnQuit);
		controlPanel.setStyleName("i_testt_controlPanel");
		btnComplete.setStyleName("MTestTool_Obj3");
		btnQuit.setStyleName("MTestTool_Obj4");
		vocaShowPanel.getElement().setAttribute("style",
				"text-align: center; margin-bottom:40px;");
		// -----
		for (String v : test)
			addTestVoca(vocaShowPanel, v);
		// -----
		btnComplete.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getTestResult();
			}
		});

		btnQuit.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				container.clear();
				DOM.getElementById("htmlIntroTest").setAttribute("style",
						"display:block");
			}
		});
	}

	private void getTestResult() {
		container.clear();
		container.add(htmlResultTest);
		htmlResultTest.setVisible(true);
		DOM.getElementById("table-app").setAttribute("style", "display:block;");

		testgood.setVisible(false);
		testmedium.setVisible(false);
		testbad.setVisible(false);
		if (totalCheck >= 15) {
			testgood.setVisible(true);
			testgoodlv.setText("Level: " + testLevel);
			resultTestGood.setText(totalCheck + " / 20");
			lbTest1.setText("Bạn đã hoàn thành tốt bài kiểm tra!");
			if (testLevel + 1 <= 6) {
				lbTest2.setText("Note: Bạn nên tiếp tục làm bài test Level "
						+ (testLevel + 1) + " hoặc bắt đầu học từ Level "
						+ (testLevel + 1) + ".");
				btnNextTesting.setText("Tiếp Tục - Lv " + (testLevel + 1));
				btnNextTesting.getElement().setAttribute("style", "");
			} else {
				lbTest2.setText("Note: Bạn đã có một vốn tiếng anh rất xuất sắc. Bạn có thể dùng LazzyBee để ôn tập lại vốn từ của mình.");
				btnNextTesting.getElement().setAttribute("style",
						"background: silver");
			}
		} else if (totalCheck < 15 && totalCheck >= 10) {
			testmedium.setVisible(true);
			testmediumlv.setText("Level: " + testLevel);
			resultTestMedium.setText(totalCheck + " / 20");
			lbTest1.setText("Bạn đã hoàn thành bài kiểm tra ở mức trung bình!");
			lbTest2.setText("Note: Bạn nên bắt đầu học từ Level " + testLevel
					+ ".");
			btnNextTesting.setText("Tiếp Tục");
			btnNextTesting.getElement().setAttribute("style",
					"background: silver");
		} else {
			testbad.setVisible(true);
			testbadlv.setText("Level: " + testLevel);
			resultTestBad.setText(totalCheck + " / 20");
			lbTest1.setText("Bạn đã không hoàn thành tốt bài kiểm tra!");
			if (testLevel - 1 != 0) {
				lbTest2.setText("Note: Bạn nên tiếp tục làm bài test Level "
						+ (testLevel - 1) + " hoặc bắt đầu học từ Level "
						+ (testLevel - 1) + ".");
				btnNextTesting.setText("Tiếp Tục - Lv " + (testLevel - 1));
				btnNextTesting.getElement().setAttribute("style", "");
			} else {
				lbTest2.setText("Note: Bạn nên bắt đầu học từ Level 1");
				btnNextTesting.getElement().setAttribute("style",
						"background: silver");
			}
		}
	}

	/*
	 * @UiHandler("btnStartTesting") void onBtnStartTestingClick(ClickEvent e) {
	 * getTestByLevel(testLevel); }
	 */
	@UiHandler("btnAgainTesting")
	void onBtnAgainTestingClick(ClickEvent e) {
		// getTestByLevel(testLevel);
	}

	@UiHandler("btnNextTesting")
	void onBtnNextTestingClick(ClickEvent e) {
		if (totalCheck >= 15) {
			if (testLevel + 1 <= 6) {
				testLevel++;
				// getTestByLevel(testLevel);
			}
		} else if (totalCheck < 15 && totalCheck >= 10) {

		} else {
			if (testLevel - 1 != 0) {
				testLevel--;
				// getTestByLevel(testLevel);
			}
		}
	}

}
