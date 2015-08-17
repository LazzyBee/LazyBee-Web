package com.born2go.lazzybeemobile.client;

import java.util.ArrayList;
import java.util.List;

import com.born2go.lazzybee.gdatabase.clientapi.DataService;
import com.born2go.lazzybee.gdatabase.clientapi.DataServiceAsync;
import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

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

		// add button search by element id

		btSearch = new Button();
		btSearch.getElement().setClassName("fa fa-search");
		RootPanel.get("btsearch").add(btSearch);

		// when click button search call method to server and return value for
		// client
		btSearch.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String voca_q = txtSeach.getText();
				dataService.findVoca(voca_q, new AsyncCallback<Voca>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(Voca result) {
						if (result != null)
							RootPanel.get("gwt_contentMdic").add(
									new MVocaView().setVoca(result));

					}
				});

			}
		});
	}

	
}
