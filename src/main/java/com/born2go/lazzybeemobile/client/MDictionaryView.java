package com.born2go.lazzybeemobile.client;

import java.util.ArrayList;
import java.util.List;

import com.born2go.lazzybee.gdatabase.client.rpc.DataService;
import com.born2go.lazzybee.gdatabase.client.rpc.DataServiceAsync;
import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.widget.input.MTextBox;

/*
 * MDictionaryView is ui gwt for page mdictionay.html
 * function: search q, show answer to user
 */
public class MDictionaryView extends Widget {
	private MTextBox txtSeach;

	// private Button btSearch;
	public final DataServiceAsync dataService = GWT.create(DataService.class);

	public static class DefiContainer {
		List<String> types = new ArrayList<String>();
		String txbMeaning_id;
		String txbExplain_id;
		String txbExam_id;
	}

	public MDictionaryView() {
		designView();
		historyTokenHandler();

	}

	/**
	 * add button gwt search for mdictionary.html add textbox gwt input search
	 * for mdictionary.htlm
	 */
	int divHeight;
	int heightMdic_intro;
	Element btSearch;

	private void designView() {

		if (RootPanel.get("notice_first") != null) {
			RootPanel.get("notice_first").getElement()
					.setAttribute("style", "display:none");
			;
		}
		// add textbox input search by element id
		txtSeach = new MTextBox();

		txtSeach.getElement().setId("txt_valueSearch");
		txtSeach.setPlaceHolder("Nhập từ muốn tìm...");
		RootPanel.get("inputsearch").add(txtSeach);
		// add button search by element id

		// btSearch = new Button();
		// btSearch.getElement().setClassName("fa fa-search");
		// RootPanel.get("btsearch").add(btSearch);
		txtSeach.setFocus(true);
		txtSeach.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {
				txtSeach.selectAll();

			}
		});
		txtSeach.addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event_) {
				boolean enterPressed = KeyCodes.KEY_ENTER == event_
						.getNativeEvent().getKeyCode();
				if (enterPressed) {
					MGWT.hideKeyBoard();
					if (!txtSeach.getText().equals(""))
						Window.Location.assign("/vdict/#" + txtSeach.getText());
				}
			}
		});
		btSearch = RootPanel.get("btsearch").getElement();
		Event.sinkEvents(btSearch, Event.ONCLICK);
		Event.setEventListener(btSearch, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					MGWT.hideKeyBoard();
					if (!txtSeach.getText().equals(""))
						Window.Location.assign("/vdict/#" + txtSeach.getText());
				}

			}
		});
		// btSearch.addTapHandler(new TapHandler() {
		//
		// @Override
		// public void onTap(TapEvent event) {
		// if (!txtSeach.getText().equals(""))
		// Window.Location.assign("/mvdict/#" + txtSeach.getText());
		// MGWT.hideKeyBoard();
		//
		// }
		// });

	}

	private void historyTokenHandler() {
		String path = Window.Location.getPath();
		if (path.contains("vdict")) {
			// -----
			if (!History.getToken().isEmpty()) {
				loadVocaToken(History.getToken());
			} else {
				String token = Window.Location.getPath().split("/")[2];
				if (token != null && !token.isEmpty()) {
					loadVocaToken(token);
				} else {
					// block element
					DOM.getElementById("mdic_introduction").setAttribute(
							"style", "display:block");
					DOM.getElementById("blogs").setAttribute("style",
							"display:block");
				}

			}

		}
		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				loadVocaToken(event.getValue());
			}
		});
	}

	private void loadVocaToken(final String history_token) {
		LazzyBeeMobile.noticeBox.showNotice("Đang tải...");
		RootPanel.get("gwt_contentMdic").clear();
		DOM.getElementById("mdic_introduction").setAttribute("style",
				"display:none");
		DOM.getElementById("blogs").setAttribute("style", "display:none");
		// final String history_token = History.getToken();
		txtSeach.setText(history_token);

		dataService.findVoca_Web(history_token.trim(), true, new AsyncCallback<Voca>() {
			@Override
			public void onSuccess(Voca result) {
				if (result == null) {
					LazzyBeeMobile.noticeBox.showNotice("Không tìm thấy từ - "
							+ history_token);
				} else {
					LazzyBeeMobile.noticeBox.hideNotice();
					RootPanel.get("gwt_contentMdic").add(
							new MVocaView().setVoca(result));

				}
			}

			@Override
			public void onFailure(Throwable caught) {
				LazzyBeeMobile.noticeBox
						.showNotice("Đã có lỗi xảy ra trong quá trình tải, bấm F5 để thử lại");
				LazzyBeeMobile.noticeBox.hideNotice();
			}
		});
	}

	/*
	 * when do not find any question in data, show notification for user
	 */

}
