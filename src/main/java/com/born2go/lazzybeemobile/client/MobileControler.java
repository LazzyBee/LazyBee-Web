package com.born2go.lazzybeemobile.client;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

// add gwt for file html
public class MobileControler extends Widget {

	// constructor
	public MobileControler() {
		// add MdictionaryView for mdictionnay.html and show it
		if (RootPanel.get("gwt_header_mdic") != null) {
			MDictionaryView m = new MDictionaryView();
		}

	}

}
