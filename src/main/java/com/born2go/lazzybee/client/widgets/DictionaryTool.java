package com.born2go.lazzybee.client.widgets;

import com.born2go.lazzybee.client.LazzyBee;
import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class DictionaryTool extends Composite {

	private static EditorToolUiBinder uiBinder = GWT
			.create(EditorToolUiBinder.class);

	interface EditorToolUiBinder extends UiBinder<Widget, DictionaryTool> {
	}
	
	@UiField HTMLPanel tabPanel;
	@UiField Label trademarkLb;
	
	String history_token;

	public DictionaryTool() {
		initWidget(uiBinder.createAndBindUi(this));
		
		Window.addResizeHandler(new ResizeHandler() {
			  Timer resizeTimer = new Timer() {  
				  @Override
				  public void run() {
					  tabPanel.getElement().setAttribute("style", "height:"+ (Window.getClientHeight()-40)+ "px");
					  DOM.getElementById("content").setAttribute("style", "height:"+ (Window.getClientHeight()-40)+ "px");
				  }
			  };

			  @Override
			  public void onResize(ResizeEvent event) {
				  resizeTimer.cancel();
				  resizeTimer.schedule(250);
			  }
		});
		
		tabPanel.getElement().setAttribute("style", "height:"+ (Window.getClientHeight()-40)+ "px");
		trademarkLb.getElement().setAttribute("style", "position: absolute; bottom: 20px");
		
		addSearchTool();
		
		history_token = History.getToken();
		if(history_token.isEmpty()) {
			String newURL = Window.Location.createUrlBuilder().setHash("vocabulary").buildString();
			Window.Location.replace(newURL);
			RootPanel.get("wt_dictionary_content").add(new ListVocaView());
		} 
		else {
			if(history_token.contains("vocabulary")) {
				if(history_token.contains("/")) {
					final String[] sub_token = history_token.split("/");
					if(sub_token[1] == null || sub_token[1].isEmpty()) {
						RootPanel.get("wt_dictionary_content").add(new ListVocaView());
					}
					else {
						LazzyBee.noticeBox.setNotice("Đang tải...");
						LazzyBee.data_service.findVoca(sub_token[1], new AsyncCallback<Voca>() {					
							@Override
							public void onSuccess(Voca result) {
								if(result == null) {
									LazzyBee.noticeBox.setNotice("Không tìm thấy từ - " + sub_token[1]);
									LazzyBee.noticeBox.setAutoHide();
									Label lbNotFound = new Label("Không tìm thấy từ - " + sub_token[1]);
									lbNotFound.getElement().setAttribute("style", "font-weight: bold; margin-top: 30px; font-size: 16px;");
									RootPanel.get("wt_dictionary_content").add(lbNotFound);
								}
								else {
									LazzyBee.noticeBox.hide();
									RootPanel.get("wt_dictionary_content").add(new VocaView().setVoca(result));
								}
							}
							
							@Override
							public void onFailure(Throwable caught) {
								LazzyBee.noticeBox.setNotice("! Đã có lỗi xảy ra trong quá trình tải");
								LazzyBee.noticeBox.setAutoHide();
							}
						});
					}
				} 
				else {
					RootPanel.get("wt_dictionary_content").add(new ListVocaView());
				}
			}
		}
		
		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if(History.getToken().contains("/"))
					Window.Location.reload();
			}
		});
	}
	
	void addSearchTool() {
		HTMLPanel hor = new HTMLPanel("");
		hor.getElement().setAttribute("style", "width: 100%; height: 40px; display: inline-flex;"); 
		final TextBox searchBox = new TextBox();
		searchBox.getElement().setAttribute("style", "float: left; width: 100%; height: 70%; padding-left: 10px;");
		searchBox.getElement().setAttribute("placeholder", "Tìm từ vựng");
		HTMLPanel searchButton = new HTMLPanel("<i class='fa fa-search fa-lg' style='margin-top: 12px; margin-left: 20px;'></i>");
		searchButton.getElement().setAttribute("style", "float: left; width: 60px; height: 100%; background: #0e74af; color: white; cursor: pointer;");
		hor.add(searchBox);
		hor.add(searchButton);
		RootPanel.get("wt_search_tool").add(hor);
		//-----
		searchBox.addKeyPressHandler(new KeyPressHandler()
        {
            @Override
            public void onKeyPress(KeyPressEvent event_)
            {
                boolean enterPressed = KeyCodes.KEY_ENTER == event_
                        .getNativeEvent().getKeyCode();
                if (enterPressed)
                {
                    Window.Location.assign("/dictionary/#vocabulary/" + searchBox.getText());
                }
            }
        });
	}

}
