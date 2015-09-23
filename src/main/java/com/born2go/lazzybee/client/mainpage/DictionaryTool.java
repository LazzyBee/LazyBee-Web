package com.born2go.lazzybee.client.mainpage;

import com.born2go.lazzybee.client.LazzyBee;
import com.born2go.lazzybee.client.subpage.ListVocaView;
import com.born2go.lazzybee.client.subpage.TestTool;
import com.born2go.lazzybee.client.subpage.VocaPreview;
import com.born2go.lazzybee.client.subpage.VocaView;
import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
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
	
	@UiField Anchor defaultSite;
	@UiField Anchor testSite;
	@UiField Anchor dictionarySite;
	@UiField Anchor previewSite;
	
	String history_token;
	boolean isReload = true;
	
	TextBox searchBox = new TextBox();

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
		historyTokenHandler();
	}
	
	void removeTabStyle() {
		defaultSite.removeStyleName("DictionaryTool_Obj5");
		testSite.removeStyleName("DictionaryTool_Obj5");
		dictionarySite.removeStyleName("DictionaryTool_Obj5");
	}
	
	void historyTokenHandler() {
		history_token = History.getToken();
		if(history_token.isEmpty()) {
//			String newURL = Window.Location.createUrlBuilder().setHash("vocabulary").buildString();
//			Window.Location.replace(newURL);
//			RootPanel.get("wt_dictionary_content").add(new ListVocaView());
			HTMLPanel htmlPanel = new HTMLPanel("<span>Phương pháp học thông qua flashcard chỉ có trên phiên bản mobile. Bạn hãy cài đặt App mobile để học từ vựng tốt hơn.</span>");
			htmlPanel.getElement().setAttribute("style", "margin-top: 20px; padding: 10px; background-color: lemonchiffon; line-height: 1.5;");
			RootPanel.get("wt_dictionary_content").add(htmlPanel);
			defaultSite.addStyleName("DictionaryTool_Obj5");
		} 
		else {
			if(history_token.contains("dictionary")) {
				if(history_token.contains("/")) {
					final String[] sub_token = history_token.split("/");
					if(sub_token[1] == null || sub_token[1].isEmpty()) {
						RootPanel.get("wt_dictionary_content").add(new ListVocaView());
						dictionarySite.addStyleName("DictionaryTool_Obj5");
					}
					else {
						defaultSite.addStyleName("DictionaryTool_Obj5");
						searchBox.setText(sub_token[1]);
						LazzyBee.noticeBox.setNotice("Đang tải...");
						LazzyBee.data_service.findVoca(sub_token[1], new AsyncCallback<Voca>() {					
							@Override
							public void onSuccess(Voca result) {
								if(result == null) {
									LazzyBee.noticeBox.setNotice("Không tìm thấy từ - " + sub_token[1]);
									LazzyBee.noticeBox.setAutoHide();
									HTMLPanel htmlPanel = new HTMLPanel("");
									htmlPanel.getElement().setAttribute("style", "margin-top: 20px; padding: 10px; background-color: lemonchiffon; line-height: 1.5;");
									Label lbNotFound = new Label("Không tìm thấy từ - " + sub_token[1]);
									htmlPanel.add(lbNotFound);
									RootPanel.get("wt_dictionary_content").add(htmlPanel);
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
			else if (history_token.equals("test")) {
				testSite.addStyleName("DictionaryTool_Obj5");
				RootPanel.get("wt_dictionary_content").add(new TestTool());
			}
			else if (history_token.equals("preview")) {
				previewSite.addStyleName("DictionaryTool_Obj5");
				RootPanel.get("wt_dictionary_content").add(new VocaPreview());
			}
		}
		
		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if(isReload)
					Window.Location.reload();
				isReload = true;
			}
		});
	}
	
	void addSearchTool() {
		HTMLPanel hor = new HTMLPanel("");
		hor.getElement().setAttribute("style", "width: 100%; height: 40px; display: inline-flex;"); 
		searchBox.getElement().setAttribute("style", "float: left; width: 100%; height: 70%; padding-left: 10px;");
		searchBox.getElement().setAttribute("placeholder", "Tìm từ vựng");
		Anchor searchButton = new Anchor();
		searchButton.getElement().setInnerHTML("<i class='fa fa-search fa-lg' style='margin-top: 12px; margin-left: 20px;'></i>");
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
                	if(!searchBox.getText().equals(""))
                		Window.Location.assign("/library/#dictionary/" + searchBox.getText());
                }
            }
        });
		searchButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(!searchBox.getText().equals(""))
					Window.Location.assign("/library/#dictionary/" + searchBox.getText());
			}
		});
	}
	
	@UiHandler("defaultSite")
	void onDefaultSiteClick(ClickEvent e) {
		String newURL = "/library/";
		Window.Location.replace(newURL);
//		Window.Location.reload();
	}
	
	@UiHandler("testSite")
	void onTestSiteClick(ClickEvent e) {
		Window.Location.assign("/library/#test");
		Window.Location.reload();
	}
	
	@UiHandler("dictionarySite")
	void onDictionarySiteClick(ClickEvent e) {
		Window.Location.assign("/library/#dictionary");
		Window.Location.reload();
	}
	
	@UiHandler("previewSite")
	void onPreviewSiteClick(ClickEvent e) {
		Window.Location.assign("/library/#preview");
		Window.Location.reload();
	}
	
}
