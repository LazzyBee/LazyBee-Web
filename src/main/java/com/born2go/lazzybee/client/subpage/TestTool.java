package com.born2go.lazzybee.client.subpage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.born2go.lazzybee.gdatabase.shared.nonentity.Test;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class TestTool extends Composite {

	private static TestToolUiBinder uiBinder = GWT
			.create(TestToolUiBinder.class);

	interface TestToolUiBinder extends UiBinder<Widget, TestTool> {
	}

	@UiField
	HTMLPanel container;
	@UiField
	HTMLPanel htmlIntroTest;
	@UiField
	HTMLPanel htmlResultTest;
	@UiField
	HTMLPanel testgood;
	@UiField
	HTMLPanel testmedium;
	@UiField
	HTMLPanel testbad;
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
	@UiField 
	ListBox lsbLevel;

	Label checkTotal;

	private int totalCheck = 0; // Tong so tu user biet
	private int testLevel = 2; // Level test default khi bat dau
	private Map<String, Boolean> testMap = new HashMap<String, Boolean>();

	private int current_random;

	public TestTool() {
		initWidget(uiBinder.createAndBindUi(this));
		
		lsbLevel.addItem("1");
		lsbLevel.addItem("2");
		lsbLevel.addItem("3");
		lsbLevel.addItem("4");
		lsbLevel.addItem("5");
		lsbLevel.addItem("6");
		
		lsbLevel.setSelectedIndex(1);

		DOM.getElementById("wt_search_tool").setAttribute("style",
				"display: none");
	}

	private void getTestByLevel(int level) {
		String test[] = null;
		int test_index = Random.nextInt(10);
		while (test_index == current_random) {
			test_index = Random.nextInt(10);
		}
		current_random = test_index;
		switch (level) {
		case 1:
			test = Test.getTestLv1()[test_index].split(",");
			break;
		case 2:
			test = Test.getTestLv2()[test_index].split(",");
			break;
		case 3:
			test = Test.getTestLv3()[test_index].split(",");
			break;
		case 4:
			test = Test.getTestLv4()[test_index].split(",");
			break;
		case 5:
			test = Test.getTestLv5()[test_index].split(",");
			break;
		case 6:
			test = Test.getTestLv6()[test_index].split(",");
			break;
		default:
			break;
		}

		List<String> ltest = new ArrayList<String>();
		for (int i = 0; i <= 19; i++) {
			ltest.add(test[i]);
		}
		startTesting(ltest);
	}

	private void addTestVoca(HTMLPanel vocaShowPanel, final String v) {
		testMap.put(v, false);
		final HTMLPanel form = new HTMLPanel("");
		Label vocaQ = new Label(v);
		Label vocaLv = new Label("Lv: " + testLevel);
		form.add(vocaQ);
		form.add(vocaLv);
		form.setStyleName("TestTool_Obj5");
		vocaQ.getElement()
				.setAttribute(
						"style",
						"font-size: 14px; font-weight: bold; color: #eafd74");
		vocaLv.getElement().setAttribute("style",
				"font-weight: bold; color: white; margin-top: 10px");
		vocaShowPanel.add(form);
		Anchor btnForm = new Anchor();
		btnForm.getElement()
				.setAttribute("style",
						"position: absolute; top: 0px; left: 0px; width: 100%; height: 100%;");
		form.add(btnForm);
		// -----
		btnForm.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (testMap.get(v)) {
					form.getElement().setAttribute("style", "background: #009688");
					totalCheck--;
				} else {
					form.getElement().setAttribute("style",
							"background: #009688");
					totalCheck++;
				}
				testMap.put(v, !testMap.get(v));
				checkTotal.setText("B: " + totalCheck + " / 20");
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
		testInfoPanel.setStyleName("TestTool_Obj1");
		testInfoPanel.getElement().setAttribute("style",
				"padding: 10px; overflow: hidden;");
		Label total = new Label("Tổng: " + test.size() + " Từ");
		Label info = new Label("(Đây là bài tự kiểm tra, hãy click để chọn các từ bạn đã biết)");
		checkTotal = new Label("B: " + totalCheck + " / 20");
		total.getElement().setAttribute("style",
				"float: left; font-weight: bold;");
		info.getElement()
				.setAttribute("style",
						"float: left;margin-left: 20px;color: #009688;font-weight: bold;");
		checkTotal
				.getElement()
				.setAttribute("style",
						"float: right; font-weight:bold; color: forestgreen; margin-right: 0px;");
		testInfoPanel.add(total);
		testInfoPanel.add(info);
		testInfoPanel.add(checkTotal);
		Anchor btnComplete = new Anchor("Hoàn Thành");
		Anchor btnQuit = new Anchor("Dừng Bài Test");
		controlPanel.add(btnComplete);
		controlPanel.add(btnQuit);
		controlPanel
				.getElement()
				.setAttribute("style",
						"text-align: center; white-space: nowrap; margin-bottom: 60px;");
		btnComplete.setStyleName("TestTool_Obj3");
		btnQuit.setStyleName("TestTool_Obj4");
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
				container.add(htmlIntroTest);
			}
		});
	}

	private void getTestResult() {
		container.clear();
		container.add(htmlResultTest);
		htmlResultTest.setVisible(true);
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

	@UiHandler("btnStartTesting")
	void onBtnStartTestingClick(ClickEvent e) {
		testLevel = lsbLevel.getSelectedIndex() + 1;
		getTestByLevel(testLevel);
	}

	@UiHandler("btnAgainTesting")
	void onBtnAgainTestingClick(ClickEvent e) {
		getTestByLevel(testLevel);
	}

	@UiHandler("btnNextTesting")
	void onBtnNextTestingClick(ClickEvent e) {
		if (totalCheck >= 15) {
			if (testLevel + 1 <= 6) {
				testLevel++;
				getTestByLevel(testLevel);
			}
		} else if (totalCheck < 15 && totalCheck >= 10) {

		} else {
			if (testLevel - 1 != 0) {
				testLevel--;
				getTestByLevel(testLevel);
			}
		}
	}

}
