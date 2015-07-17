package com.born2go.lazzybee.client;

import com.born2go.lazzybee.client.widgets.EditorTool;
import com.born2go.lazzybee.gdatabase.clientapi.DataService;
import com.born2go.lazzybee.gdatabase.clientapi.DataServiceAsync;
import com.born2go.lazzybeemobile.client.MobileControler;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.RootPanel;

public class LazzyBee implements EntryPoint {

	public static DataServiceAsync data_service = GWT.create(DataService.class);

	@Override
	public void onModuleLoad() {

		if (RootPanel.get("wt_editor") != null)
			RootPanel.get("wt_editor").add(new EditorTool());
		if (RootPanel.get("version_mobile") != null) {

			MobileControler mobile = new MobileControler();
		}
	}

}
