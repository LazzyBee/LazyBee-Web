package com.born2go.lazzybee.client.widgets;

import com.born2go.lazzybee.client.LazzyBee;
import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
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

	public EditorTool() {
		initWidget(uiBinder.createAndBindUi(this));
		
		tabPanel.getElement().setAttribute("style", "height:"+ (Window.getClientHeight()-40)+ "px");
		trademarkLb.getElement().setAttribute("style", "position: relative; top:"+ (Window.getClientHeight()-200)+ "px");
		
		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				Window.Location.reload();
			}
		});
		
		String history_token = History.getToken();
		
		if(history_token.isEmpty()) {
			String newURL = Window.Location.createUrlBuilder().setHash("vocabulary").buildString();
			Window.Location.replace(newURL);
			RootPanel.get("editor_tool").add(vocaTool);
			vocaTool.replaceEditor();
		}
		else if (history_token.contains("vocabulary")) {
			if(!history_token.contains("/")) {
				RootPanel.get("editor_tool").add(vocaTool);
				vocaTool.replaceEditor();
			}
			else {
				final String[] sub_token = history_token.split("/");
				if(sub_token[1] == null || sub_token[1].isEmpty()) {
					RootPanel.get("editor_tool").add(vocaTool);
					vocaTool.replaceEditor();
				}
				else {
					final NoticeBox ntc = new NoticeBox("Đang tải...");
					LazzyBee.data_service.findVoca(sub_token[1], new AsyncCallback<Voca>() {					
						@Override
						public void onSuccess(Voca result) {
							if(result == null) {
								ntc.changeNotice("Không tìm thấy từ - " + sub_token[1]);
								ntc.setAutoHide();
								RootPanel.get("editor_tool").add(vocaTool);
								vocaTool.replaceEditor();
							}
							else {
								ntc.hide();
								RootPanel.get("editor_tool").add(vocaTool);
								vocaTool.replaceEditor();
								vocaTool.setVoca(result);
							}
						}
						
						@Override
						public void onFailure(Throwable caught) {
							ntc.changeNotice("! Đã có lỗi xảy ra trong quá trình tải");
							ntc.setAutoHide();
							RootPanel.get("editor_tool").add(vocaTool);
						}
					});
				}
			}
		}
	}

}
