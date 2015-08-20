package com.born2go.lazzybee.client;

import org.timepedia.exporter.client.ExporterUtil;

import com.born2go.lazzybee.client.widgets.DictionaryTool;
import com.born2go.lazzybee.client.widgets.EditorTool;
import com.born2go.lazzybee.client.widgets.LoginControl;
import com.born2go.lazzybee.client.widgets.NoticeBox;
import com.born2go.lazzybee.gdatabase.clientapi.DataService;
import com.born2go.lazzybee.gdatabase.clientapi.DataServiceAsync;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.RootPanel;

public class LazzyBee implements EntryPoint {
	
	public static DataServiceAsync data_service = GWT.create(DataService.class);
	public static LoginControl loginControl = new LoginControl();
	
	//Google app id and api key
	public static String gClientId = "1090254847247-hhq28qf96obdjm7c7pgr2qo2mt2o842l.apps.googleusercontent.com";
	public static String gApiKey = "AIzaSyBJUT9Le2BQ9VQBBWYpMNjKAh6uJVxPseQ";
	public static String gScopes = "https://www.googleapis.com/auth/plus.me";
	public static boolean isGoogleInit = false;
	//Facebook app id
	public static String fCLientId = "754889477966743";
	//App notice
	public static NoticeBox noticeBox = new NoticeBox("");
	
	Anchor menuLogin = new Anchor("Đăng nhập");

	@Override
	public void onModuleLoad() {
		menuLogin.setStyleName("header_accPro_item");
		menuLogin.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				loginControl.center();
			}
		});
		RootPanel.get("menu_login").add(menuLogin);
		
		RootPanel.get("wt_noticebox").add(noticeBox);
		noticeBox.hide();
		
		if(RootPanel.get("wt_editor_slide") != null)
			RootPanel.get("wt_editor_slide").add(new EditorTool());
		
		if(RootPanel.get("wt_dictionary_slide") != null)
			RootPanel.get("wt_dictionary_slide").add(new DictionaryTool());
		
		ExporterUtil.exportAll();
		onLoadImpl();
	}
	
	private native void onLoadImpl() /*-{
    	if ($wnd.exporterOnLoad && typeof $wnd.exporterOnLoad == 'function') $wnd.exporterOnLoad();
  	}-*/;

}
