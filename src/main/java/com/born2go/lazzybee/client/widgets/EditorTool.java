package com.born2go.lazzybee.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class EditorTool extends Composite {

	private static EditorToolUiBinder uiBinder = GWT
			.create(EditorToolUiBinder.class);

	interface EditorToolUiBinder extends UiBinder<Widget, EditorTool> {
	}
	
	@UiField HTMLPanel tabPanel;
	@UiField Label trademarkLb;
	@UiField ScrollPanel tabContent;
	
	VocaEditorTool vocaTool = new VocaEditorTool();

	public EditorTool() {
		initWidget(uiBinder.createAndBindUi(this));
		
		tabPanel.getElement().setAttribute("style", "height:"+ (Window.getClientHeight()-40)+ "px");
		tabContent.getElement().setAttribute("style", "height:"+ (Window.getClientHeight()-100)+ "px; overflow: auto;");
		trademarkLb.getElement().setAttribute("style", "position: relative; top:"+ (Window.getClientHeight()-200)+ "px");
		
		tabContent.add(vocaTool);
		vocaTool.getScrollTabContent(tabContent);
	}

}
