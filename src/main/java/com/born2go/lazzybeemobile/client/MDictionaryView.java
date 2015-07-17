package com.born2go.lazzybeemobile.client;

import com.born2go.lazzybee.gdatabase.clientapi.DataService;
import com.born2go.lazzybee.gdatabase.clientapi.DataServiceAsync;
import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class MDictionaryView extends Widget {

	private TextBox txtSeach;
	private Button btSearch;
	public final DataServiceAsync dataService = GWT.create(DataService.class);

	public MDictionaryView() {
		designView();
		setValue(null);
	}

	private void designView() {
		txtSeach = new TextBox();
		txtSeach.getElement().setId("txt_valueSearch");
		RootPanel.get("inputsearch").add(txtSeach);

		// add button search

		btSearch = new Button();
		btSearch.getElement().setClassName("fa fa-search");
		RootPanel.get("btsearch").add(btSearch);

		btSearch.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String value = txtSeach.getText();
				dataService.findVoca(value, new AsyncCallback<Voca>() {
					
					@Override
					public void onSuccess(Voca result) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}
				});

			}
		});
	}

	private void setValue(Voca voca) {
		// set text for name vocabulary
		DOM.getElementById("voca").setInnerHTML("People");

	}
}
