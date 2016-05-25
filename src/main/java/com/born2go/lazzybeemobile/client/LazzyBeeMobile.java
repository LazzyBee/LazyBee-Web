package com.born2go.lazzybeemobile.client;

import org.timepedia.exporter.client.ExporterUtil;

import com.born2go.lazzybee.gdatabase.client.rpc.DataService;
import com.born2go.lazzybee.gdatabase.client.rpc.DataServiceAsync;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

public class LazzyBeeMobile implements EntryPoint {
	public static DataServiceAsync data_service = GWT.create(DataService.class);
	private final int heightHeader = 44;
	MenuMobile menu = new MenuMobile();
	// Facebook app id test lent 1224795884217031
	public static String fClientId = "754889477966743";
	private static LazzyBeeMobile onlyOne;
	public static NoticeBox noticeBox = new NoticeBox();

	@Override
	public void onModuleLoad() {
		// add MdictionaryView for mdictionnay.html and show it
		// MGWT.applySettings(MGWTSettings.getAppSetting());
		onlyOne = this;

		if (RootPanel.get("gwt_header_mdic") != null) {
			MDictionaryView m = new MDictionaryView();

		} else if (RootPanel.get("gwt_contentMTestTool") != null) {
			RootPanel.get("gwt_contentMTestTool").add(new MTestTool());
		}

		if (RootPanel.get("menuBtn") != null) {
			// if url is testvocab?menu=0 hide menu in mobile
			if (RootPanel.get("left_header") != null) {
				String path = Window.Location.getHref();
				if (path.contains("?menu=0")) {
					RootPanel.get("left_header").getElement()
							.setAttribute("style", "visibility: hidden;");
				}

			} else {
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
		// fb-root

		if (RootPanel.get("fb-root") != null) {
			lazyInitJsFaceBook();
			exportGwtClass();

		}

	}

	// export gwt class
	void exportGwtClass() {
		ExporterUtil.exportAll();
	}

	public static void loadFaceBook() {
		Scheduler.get().scheduleDeferred(new Command() {
			public void execute() {
				onlyOne.processingFB();
			}
		});
	}

	void processingFB() {
		facebookInit(fClientId);
	}

	void lazyInitJsFaceBook() {
		// Load facebook api
		ScriptInjector.fromUrl("https://connect.facebook.net/en_US/all.js")
				.setWindow(ScriptInjector.TOP_WINDOW)
				.setCallback(new Callback<Void, Exception>() {
					public void onFailure(Exception reason) {

					}

					public void onSuccess(Void result) {

					}
				}).inject();
	}

	native void facebookInit(String fClientId) /*-{
												var clientId = fClientId;
												$wnd.FB._https = true;
												$wnd.FB.init({
												appId      : clientId,
												cookie	 : true,
												xfbml      : true,
												version    : 'v2.3'
												});
												
												(function(d, s, id){
												var js, fjs = d.getElementsByTagName(s)[0];
												if (d.getElementById(id)) {return;}
												js = d.createElement(s); js.id = id;
												js.src = "//connect.facebook.net/en_US/sdk.js";
												fjs.parentNode.insertBefore(js, fjs);
												}(document, 'script', 'facebook-jssdk'));
												}-*/;

}
