package com.born2go.lazzybee.client.widgets;

import com.born2go.lazzybee.client.LazzyBee;
import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.google.gwt.core.client.GWT;
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
import com.google.gwt.user.client.ui.Widget;

public class EditorTool extends Composite {

	private static EditorToolUiBinder uiBinder = GWT
			.create(EditorToolUiBinder.class);

	interface EditorToolUiBinder extends UiBinder<Widget, EditorTool> {
	}
	
	@UiField HTMLPanel tabPanel;
	@UiField Label trademarkLb;
	
	VocaEditorTool vocaTool = new VocaEditorTool();
	
	String history_token;

	public EditorTool() {
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
		
		history_token = History.getToken();
		
		if(history_token.isEmpty()) {
			String newURL = Window.Location.createUrlBuilder().setHash("vocabulary").buildString();
			Window.Location.replace(newURL);
			RootPanel.get("wt_editor").add(vocaTool);
			vocaTool.replaceEditor();
		}
		else if (history_token.contains("vocabulary")) {
			if(!history_token.contains("/")) {
				RootPanel.get("wt_editor").add(vocaTool);
				vocaTool.replaceEditor();
			}
			else {
				final String[] sub_token = history_token.split("/");
				if(sub_token[1] == null || sub_token[1].isEmpty()) {
					RootPanel.get("wt_editor").add(vocaTool);
					vocaTool.replaceEditor();
				}
				else {
					LazzyBee.noticeBox.setNotice("Đang tải...");
					LazzyBee.data_service.findVoca(sub_token[1], new AsyncCallback<Voca>() {					
						@Override
						public void onSuccess(Voca result) {
							if(result == null) {
								LazzyBee.noticeBox.setNotice("Không tìm thấy từ - " + sub_token[1]);
								LazzyBee.noticeBox.setAutoHide();
								RootPanel.get("wt_editor").add(vocaTool);
								vocaTool.replaceEditor();
							}
							else {
								LazzyBee.noticeBox.hide();
								RootPanel.get("wt_editor").add(vocaTool);
								vocaTool.replaceEditor();
								vocaTool.setVoca(result);
							}
						}
						
						@Override
						public void onFailure(Throwable caught) {
							LazzyBee.noticeBox.setNotice("! Đã có lỗi xảy ra trong quá trình tải");
							LazzyBee.noticeBox.setAutoHide();
							RootPanel.get("wt_editor").add(vocaTool);
						}
					});
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

}
