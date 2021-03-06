package com.born2go.lazzybee.client.widgets;

import com.born2go.lazzybee.client.LazzyBee;
import com.born2go.lazzybee.gdatabase.shared.User;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class LoginControl extends DialogBox {

	private static LoginDialogUiBinder uiBinder = GWT
			.create(LoginDialogUiBinder.class);

	interface LoginDialogUiBinder extends UiBinder<Widget, LoginControl> {
	}
	
	public static User user = null;

	public LoginControl() {
		setWidget(uiBinder.createAndBindUi(this));
		setAutoHideEnabled(true);
		setGlassEnabled(true);
		setStyleName("LoginControl_clean");
		
		// Init facebook plugin
		facebookInit(LazzyBee.fClientId);
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
	
	private static void saveNewUser(String userId, String userName) {
		LazzyBee.userId = userId;
		LazzyBee.userName = userName;
		User u = new User();
		if(userId.contains("_G")) {
			u.setGoogle_id(userId);
		}
		if(userId.contains("_F")) {
			u.setFacebook_id(userId);
		}
		u.setUserName(userName);
		LazzyBee.data_service.saveUser(u, new AsyncCallback<User>() {
			@Override
			public void onSuccess(User result) {
				user = result;
				if(result.isAdmin()) {
					if(DOM.getElementById("blogEditorTool") != null) {
						DOM.getElementById("blogEditorTool").setAttribute("style", "display:");
						LazzyBee.noticeBox.setAutoHide();
					}
					if(DOM.getElementById("blogViewEdit") != null)
						DOM.getElementById("blogViewEdit").setAttribute("style", "float:right; margin-top: 2px; margin-left: 20px;");
					/*if(DOM.getElementById("groupViewEdit") != null)
						DOM.getElementById("groupViewEdit").setAttribute("style", "float:right; margin-top: 2px; margin-left: 20px;");*/						
				}
			}
			@Override
			public void onFailure(Throwable caught) {}
		});
	}

	public void hideDialog() {
		hide();
	}
	
	public void onGoogleJSLoad() {
		googleLoad();
	}
	
	@UiHandler("googleLogin")
	void onGoogleLoginClick(ClickEvent e) {
		googleLogin(LazzyBee.gClientId, LazzyBee.gScopes, this);
	}
	
	@UiHandler("facebookLogin")
	void onFacebookLoginClick(ClickEvent e) {
		facebookLogin(this);
	}
	
	void googleLoad() {
		googleInit(LazzyBee.gApiKey, LazzyBee.gClientId, LazzyBee.gScopes, this);
	}
	
	native boolean googleInit(String gApiKey, String gClientId, String gScopes, LoginControl c) /*-{
		if(typeof $wnd.gapi.client != 'undefined') {
			var apiKey = gApiKey;
			var clientId = gClientId;
			var scopes = gScopes;
			
			$wnd.gapi.client.setApiKey(apiKey);
			$wnd.window.setTimeout(checkAuth,1);
			
			function checkAuth() {
	  			$wnd.gapi.auth.authorize({client_id: clientId, scope: scopes, immediate: true, cookie_policy: 'single_host_origin'}, handleAuthResult);
			}
			
			 function handleAuthResult(authResult) {
	        	if (authResult && !authResult.error) {
	          		c.@com.born2go.lazzybee.client.widgets.LoginControl::googleApiCall()();
	          		$wnd.document.getElementById("menu_editor").style.display = "";
	          		if($wnd.document.getElementById("wt_editor") != null) {
	          			$wnd.document.getElementById("wt_editor_notauthorize").style.display = "none";
	          			$wnd.document.getElementById("wt_editor").style.display = "";
	          		}
	        	} else {
	        		c.@com.born2go.lazzybee.client.widgets.LoginControl::facebookLoad()();
	        	}
	      	}
	      	return true;
	     }
	     else
	     	return false;
	}-*/;
	
	native void googleLogin(String gClientId, String gScopes, LoginControl c) /*-{
		var clientId = gClientId;	
		var scopes = gScopes;		
		
		$wnd.gapi.auth.authorize({client_id: clientId, scope: scopes, immediate: false, cookie_policy: 'single_host_origin'}, handleAuthResult);
        return false;
        
        function handleAuthResult(authResult) {
        	if (authResult && !authResult.error) {
          		c.@com.born2go.lazzybee.client.widgets.LoginControl::googleApiCall()();
          		c.@com.born2go.lazzybee.client.widgets.LoginControl::hideDialog()();
          		$wnd.document.getElementById("menu_editor").style.display = "";
          		if($wnd.document.getElementById("wt_editor") != null) {
	          			$wnd.document.getElementById("wt_editor_notauthorize").style.display = "none";
	          			$wnd.document.getElementById("wt_editor").style.display = "";
	          	}
        	} else {}
      	}
	}-*/;
	
	native void googleApiCall() /*-{
		$wnd.gapi.client.load('plus', 'v1', function() {
          	var request = $wnd.gapi.client.plus.people.get({
            	'userId': 'me'
          	});
          	request.execute(function(resp) {
          		$wnd.document.getElementById('menu_login').innerHTML = '';
            	var heading = document.createElement('div');
	            var image = document.createElement('img');
	            var span = document.createElement('span');
	            image.src = resp.image.url;
	            image.className = 'header_accPro_img';
	            span.innerHTML = resp.displayName;
	            span.className = 'header_accPro_item';
	            heading.appendChild(image);
	            heading.appendChild(span);
	            $wnd.document.getElementById('menu_login').appendChild(heading);
	            
	            span.onclick = function() {
	            	@com.born2go.lazzybee.client.widgets.LoginControl::addUserPorfile()();
	            }
	            
	            @com.born2go.lazzybee.client.widgets.LoginControl::saveNewUser(Ljava/lang/String;Ljava/lang/String;)(resp.id + "_G",resp.displayName);
          	});
       	});
	}-*/;
	
	native static void googleLogout() /*-{
	   	$wnd.gapi.auth.signOut();
	}-*/;
	
	void facebookLoad() {
		facebookCheckAuth(this);
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
	
	native void facebookCheckAuth(LoginControl c) /*-{
		$wnd.FB.getLoginStatus(function(response) {
		if (response.status === 'connected') {
			var userId = response.authResponse.userID;
		    var accessToken = response.authResponse.accessToken;
		    
		    c.@com.born2go.lazzybee.client.widgets.LoginControl::facebookApiCall()();
		    $wnd.document.getElementById("menu_editor").style.display = "";
		    if($wnd.document.getElementById("wt_editor") != null) {
      			$wnd.document.getElementById("wt_editor_notauthorize").style.display = "none";
      			$wnd.document.getElementById("wt_editor").style.display = "";
      		}
		} 
		else if (response.status === 'not_authorized') {} 
		else {}
		}, true);
	}-*/;
	
	native void facebookLogin(LoginControl c) /*-{
		$wnd.FB.login(function(response) {
			if (response.authResponse) {
				var userId = response.authResponse.userID;
				var accessToken = response.authResponse.accessToken;
					
				c.@com.born2go.lazzybee.client.widgets.LoginControl::facebookApiCall()();
				c.@com.born2go.lazzybee.client.widgets.LoginControl::hideDialog()();
				$wnd.document.getElementById("menu_editor").style.display = "";
				if($wnd.document.getElementById("wt_editor") != null) {
	          			$wnd.document.getElementById("wt_editor_notauthorize").style.display = "none";
	          			$wnd.document.getElementById("wt_editor").style.display = "";
	          	}
			} else {}
		}, {scope: 'public_profile'});
	}-*/;
	
	native void facebookApiCall() /*-{
		$wnd.FB.api('/me', function(response) {
			$wnd.document.getElementById('menu_login').innerHTML = '';
        	var heading = document.createElement('div');
            var image = document.createElement('img');
            var span = document.createElement('span');
			image.src = "https://graph.facebook.com/"+ response.id + "/picture?width=32&height=32";
			image.className = "header_accPro_img";
            span.innerHTML = response.name;
            span.className = 'header_accPro_item';
            heading.appendChild(image);
            heading.appendChild(span);
            $wnd.document.getElementById('menu_login').appendChild(heading);
            
            span.onclick = function() {
	            @com.born2go.lazzybee.client.widgets.LoginControl::addUserPorfile()();
	        }
	        
	        @com.born2go.lazzybee.client.widgets.LoginControl::saveNewUser(Ljava/lang/String;Ljava/lang/String;)(response.id + "_F",response.name);
		});
	}-*/;
	
	native static void facebookLogout() /*-{
		$wnd.FB.logout(function(response) {
  			// user is now logged out
		});
	}-*/;
	
	static void addUserPorfile() {
		DialogBox d = new DialogBox();
		Label logout = new Label("Đăng xuất");
		logout.setStyleName("LoginControl_Obj3");
		d.add(logout);
		d.setAutoHideEnabled(true);
		d.setPopupPosition(Window.getClientWidth() - 200, 40);
		d.setStyleName("LoginControl_clean LoginControl_Obj2");
		d.show();
		
		logout.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				googleLogout();
				facebookLogout();
				Window.Location.reload();
			}
		});
	}
}
