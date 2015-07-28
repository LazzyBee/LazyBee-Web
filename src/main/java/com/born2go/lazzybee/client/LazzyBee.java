package com.born2go.lazzybee.client;

import com.born2go.lazzybee.client.widgets.DictionaryTool;
import com.born2go.lazzybee.client.widgets.EditorTool;
import com.born2go.lazzybee.client.widgets.LoginDialog;
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
	
	public static String gClientId = "49467574161-3qq96mv46vpc09671o3cjsbe56vv3455.apps.googleusercontent.com";
	public static String gApiKey = "AIzaSyAFS8HlWEeLQ54lK1ZlOyPnumJly34mg78";
	public static String gScopes = "https://www.googleapis.com/auth/plus.me";
	
	Anchor menuLogin = new Anchor("Đăng nhập");

	@Override
	public void onModuleLoad() {
		menuLogin.setStyleName("header_accPro_item");
		menuLogin.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				new LoginDialog().center();
			}
		});
		RootPanel.get("menu_login").add(menuLogin);
		
		if(RootPanel.get("wt_editor") != null)
			RootPanel.get("wt_editor").add(new EditorTool());
		
		if(RootPanel.get("wt_dictionary") != null)
			RootPanel.get("wt_dictionary").add(new DictionaryTool());
	}

}
