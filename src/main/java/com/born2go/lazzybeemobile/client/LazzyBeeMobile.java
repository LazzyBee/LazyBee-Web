package com.born2go.lazzybeemobile.client;

import com.born2go.lazzybee.gdatabase.client.rpc.DataService;
import com.born2go.lazzybee.gdatabase.client.rpc.DataServiceAsync;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

public class LazzyBeeMobile implements EntryPoint {
	public static DataServiceAsync data_service = GWT.create(DataService.class);
	public static NoticeBox noticeBox = new NoticeBox("");
	private final int heightHeader = 44;
	MenuMobile menu = new MenuMobile();

	@Override
	public void onModuleLoad() {
		// add MdictionaryView for mdictionnay.html and show it
		// MGWT.applySettings(MGWTSettings.getAppSetting());

		if (RootPanel.get("gwt_header_mdic") != null) {
			MDictionaryView m = new MDictionaryView();

		} else if (RootPanel.get("gwt_contentMTestTool") != null) {
			RootPanel.get("gwt_contentMTestTool").add(new MTestTool());
		}
		if (RootPanel.get("menuBtn") != null) {
			menu.setHeight("200px");
			Element btMenu = RootPanel.get("menuBtn").getElement();
			Event.sinkEvents(btMenu, Event.ONCLICK);
			Event.setEventListener(btMenu, new EventListener() {

				@Override
				public void onBrowserEvent(Event event) {
					if (Event.ONCLICK == event.getTypeInt()) {
						menu.show();
						DOM.getElementById("menu").setAttribute("style",
								"display:block");
					}

				}
			});
		}
		// DOM.getElementById("main").setAttribute("style",
		// "height:" + (Window.getClientHeight() - heightHeader) + "px");
		//
		// Window.addResizeHandler(new ResizeHandler() {
		//
		// @Override
		// public void onResize(ResizeEvent event) {
		// DOM.getElementById("main").setAttribute("style",
		// "height:" + (Window.getClientHeight() - heightHeader) + "px");
		// }
		// });

	}
}
