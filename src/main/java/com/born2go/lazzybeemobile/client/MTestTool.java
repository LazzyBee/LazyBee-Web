package com.born2go.lazzybeemobile.client;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.born2go.lazzybee.client.subpage.VocaEditorTool;
import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.born2go.lazzybeemobile.shared.Common;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
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

	private LinkedHashMap<String, Boolean> testMap = new LinkedHashMap<String, Boolean>();

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

	private void getTestStep_ONE() {
		LazzyBeeMobile.data_service
				.getTestVocaStep_One(new AsyncCallback<LinkedHashMap<String, String>>() {

					@Override
					public void onSuccess(LinkedHashMap<String, String> result) {
						if (result != null && !result.isEmpty()) {
							startTest_ONE(result);
						}

					}

					@Override
					public void onFailure(Throwable caught) {

					}
				});
	}

	private void startTest_ONE(LinkedHashMap<String, String> hmap) {
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
				"Bước 1: Đánh giá sơ bộ vốn từ vựng của bạn. Hãy chọn các từ mà bạn đã biết nghĩa.");
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

			}
		});

	}

	String cookie = null;
	String user_id = Common.USER_ID;

	private void getStep_TWO() {
		LazzyBeeMobile.data_service.getTestVocaStep_Two(hmapToServer,
				new AsyncCallback<LinkedHashMap<String, String>>() {

					@Override
					public void onFailure(Throwable caught) {

					}

					@Override
					public void onSuccess(LinkedHashMap<String, String> result) {
						if (result != null && !result.isEmpty()) {
							cookie = result.get(user_id);
							result.remove(user_id);
							startTest_TWO(result);
						}
					}
				});
	}

	private void startTest_TWO(LinkedHashMap<String, String> hmap) {
		Window.scrollTo(0, 0);
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
				"Bước 2: Trong bước này, chúng tôi sẽ cố gắng ước lượng chính xác hơn vốn từ của bạn dựa vào kết quả đã thu được từ bước 1");
		checkTotal = new Label("B: " + totalCheck + " / " + hmap.size());
		total.getElement().setAttribute("style",
				"float: left; font-weight: bold;");
		info.setStyleName("i_testtool_info");
		checkTotal.setStyleName("i_testtool_checkTotal");
		testInfoPanel.add(total);
		testInfoPanel.add(checkTotal);
		testInfoPanel.add(info);
		Anchor btnStep_THREE = new Anchor("Kết thúc");

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
			}
		});

	}

	String action = Common.ACTION;

	private void getTest_THREE() {
		String[] path = cookie.split("=");
		hmapToServer.put(user_id, path[1]);
		hmapToServer.put(action, "step_two");
		LazzyBeeMobile.data_service.getTestVocaStep_Three(hmapToServer, cookie,
				path[1], new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {

					}

					@Override
					public void onSuccess(String value) {
						if (value.length() >= 0) {
							// showResultTest(value);
							Window.Location.replace(value);

						}

					}
				});
	}

	LinkedHashMap<String, String> hmapToServer = new LinkedHashMap<String, String>();
	String lazzybee_seperate = Common.lazzybee_seperate;

	private void addTestVoca(HTMLPanel vocaShowPanel, final String v,
			final String key, final int size) {

		if (key.equals("0") && v.equals(lazzybee_seperate)) {
			HTML htmlSeperate = new HTML();
			htmlSeperate.getElement().setAttribute("style",
					"width: 100%; margin: 10px;");
			vocaShowPanel.add(htmlSeperate);

		} else if (key.equals("1") && v.equals(lazzybee_seperate)) {
			HTML htmlSeperate = new HTML();
			htmlSeperate.getElement().setAttribute("style",
					"width: 100%; margin: 10px;");
			vocaShowPanel.add(htmlSeperate);

		} else if (key.equals("2") && v.equals(lazzybee_seperate)) {
			HTML htmlSeperate = new HTML();
			htmlSeperate.getElement().setAttribute("style",
					"width: 100%; margin: 10px;");
			vocaShowPanel.add(htmlSeperate);

		} else if (key.equals("3") && v.equals(lazzybee_seperate)) {
			HTML htmlSeperate = new HTML();
			htmlSeperate.getElement().setAttribute("style",
					"width: 100%; margin: 10px;");
			vocaShowPanel.add(htmlSeperate);

		} else {

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

			Anchor btnVocaAn = new Anchor("?");
			btnVocaAn.setStyleName("quest_v");
			/*
			 * btnVocaAn .getElement() .setAttribute("style",
			 * "font-size: 14px;font-weight: bold;color: #eafd74;float: right;"
			 * );
			 */
			form.add(btnVocaAn);
			btnVocaAn.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {

					findVoca(v);

				}
			});
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

	void findVoca(String voca_q) {
		LazzyBeeMobile.data_service.findVoca(voca_q, new AsyncCallback<Voca>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(Voca result) {
				if (result != null)
					onVocaView_EV(result);
				else {
					Window.alert("Từ này chưa có trong hệ thống từ điển !");
				}
			}
		});
	}

	void onVocaView_EV(Voca v) {
		final DialogBox d = new DialogBox();
		d.setStyleName("MTestTool_Obj12");
		d.setAutoHideEnabled(true);
		d.setGlassEnabled(true);
		d.setAnimationEnabled(true);
		ScrollPanel sc = new ScrollPanel();
		sc.getElement()
				.setAttribute(
						"style",
						"overflow-x: hidden; padding: 20px; height: 200px; padding-top: 20px;width: 100%; ");
		VerticalPanel ver = new VerticalPanel();
		ver.setWidth("100%");
		ver.getElement().setAttribute("style", "width: 100%;");

		Label header = new Label("Giải nghĩa tiếng việt");
		header.getElement()
				.setAttribute(
						"style",
						"color: #0066cc; text-align: center; font-size: 20px; font-weight: bold;  padding-bottom: 20px;");
		ver.add(header);
		HTML l_vn = new HTML();
		if (v.getL_vn() != null && !v.getL_vn().isEmpty())
			l_vn.setHTML(v.getL_vn());
		else
			l_vn.setHTML("Vocabulary emtry");

		ver.add(l_vn);
		sc.add(ver);
		d.add(sc);
		d.center();

		// -----

	}

}
