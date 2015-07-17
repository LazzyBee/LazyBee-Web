package com.born2go.lazzybeemobile.client;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class MobileControler extends Widget {
	 

	public MobileControler() {

		if (RootPanel.get("gwt_header_mdic") != null) {
			MDictionaryView m = new MDictionaryView();
		}

	}

}
