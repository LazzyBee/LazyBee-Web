package com.born2go.lazzybee.client.widgets;

import com.born2go.lazzybee.client.LazzyBee;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;

public class LoginDialog extends DialogBox {

	private static LoginDialogUiBinder uiBinder = GWT
			.create(LoginDialogUiBinder.class);

	interface LoginDialogUiBinder extends UiBinder<Widget, LoginDialog> {
	}

	public LoginDialog() {
		setWidget(uiBinder.createAndBindUi(this));
		setAutoHideEnabled(true);
		setGlassEnabled(true);
	}
	
	@Override
	protected void onPreviewNativeEvent(NativePreviewEvent event) {
		super.onPreviewNativeEvent(event);
		switch (event.getTypeInt()) {
		case Event.ONKEYDOWN:
			if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
				hide();
			}
			break;
		}
	}
	
	@UiHandler("googleLogin")
	void onGoogleLoginClick(ClickEvent e) {
		googleLogin(LazzyBee.gClientId, LazzyBee.gApiKey, LazzyBee.gScopes);
	}
	
	@UiHandler("facebookLogin")
	void onFacebookLoginClick(ClickEvent e) {
		Window.alert("facebook login");
	}
	
	public static native void checkGoogleLoginStatus(String gClientId, String gApiKey, String gScopes) /*-{
		var clientId = gClientId;
		var apiKey = gApiKey;
		var scopes = gScopes;
		$wnd.gapi.client.setApiKey(apiKey);
        window.setTimeout(checkAuth,1);
        
        function checkAuth() {
        	$wnd.gapi.auth.authorize({client_id: clientId, scope: scopes, immediate: true}, handleAuthResult);
      	}
      	
      	 function handleAuthResult(authResult) {
      	 }
	}-*/;
	
	native void googleLogin(String gClientId, String gApiKey, String gScopes) /*-{
		var clientId = gClientId;
		var apiKey = gApiKey;
		var scopes = gScopes;
		$wnd.gapi.auth.authorize({client_id: clientId, scope: scopes, immediate: false}, handleAuthResult);
        return false;
        
        function handleAuthResult(authResult) {
//        	var authorizeButton = document.getElementById('authorize-button');
//        	if (authResult && !authResult.error) {
//          		authorizeButton.style.visibility = 'hidden';
//          		makeApiCall();
//        	} else {
//          		authorizeButton.style.visibility = '';
//          		authorizeButton.onclick = handleAuthClick;
//        	}
      	}
	}-*/;
	
}
