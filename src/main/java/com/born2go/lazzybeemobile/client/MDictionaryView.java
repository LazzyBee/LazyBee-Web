package com.born2go.lazzybeemobile.client;

import java.util.ArrayList;
import java.util.List;

import com.born2go.lazzybee.gdatabase.clientapi.DataService;
import com.born2go.lazzybee.gdatabase.clientapi.DataServiceAsync;
import com.born2go.lazzybee.gdatabase.shared.EmployeeObj;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/*
 * MDictionaryView is ui gwt for page mdictionay.html
 * function: search q, show answer to user
 */
public class MDictionaryView extends Widget {
	private TextBox txtSeach;
	private Button btSearch;
	public final DataServiceAsync dataService = GWT.create(DataService.class);
	public static class DefiContainer {
		List<String> types = new ArrayList<String>();
		String txbMeaning_id;
		String txbExplain_id;
		String txbExam_id;
	}

	public MDictionaryView() {
		designView();

	}

	/**
	 * add button gwt search for mdictionary.html add textbox gwt input search
	 * for mdictionary.htlm
	 */
	private void designView() {

		// add textbox input search by element id
		txtSeach = new TextBox();
		txtSeach.getElement().setId("txt_valueSearch");
		RootPanel.get("inputsearch").add(txtSeach);

		txtSeach.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					searchVoca();
				}
			}
		});

		// add button search by element id

		btSearch = new Button();
		btSearch.getElement().setClassName("fa fa-search");
		RootPanel.get("btsearch").add(btSearch);

		// when click button search call method to server and return value for
		// client
		btSearch.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				searchVoca();

			}
		});
	}

	/*
	 * searchVoca in database
	 */
	private void searchVoca() {
		String voca_q = txtSeach.getText();
		dataService.findVoca(voca_q, new AsyncCallback<EmployeeObj>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(EmployeeObj result) {
				RootPanel.get("gwt_contentMdic").clear();
				if (result != null)
					RootPanel.get("gwt_contentMdic").add(
							new MVocaView().setVoca(result));
				else
					notfoundVoca();

			}
		});

	}

	/*
	 * when do not find any question in data, show notification for user
	 */
	private void notfoundVoca() {
		RootPanel.get("notfoundVoca").clear();
		RootPanel.get("notfoundVoca").add(new Label("Không tìm thấy từ"));
	}

}
