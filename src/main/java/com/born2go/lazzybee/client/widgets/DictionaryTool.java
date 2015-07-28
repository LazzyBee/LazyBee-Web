package com.born2go.lazzybee.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class DictionaryTool extends Composite {

	private static EditorToolUiBinder uiBinder = GWT
			.create(EditorToolUiBinder.class);

	interface EditorToolUiBinder extends UiBinder<Widget, DictionaryTool> {
	}
	
	@UiField HTMLPanel tabPanel;
	@UiField Label trademarkLb;

	public DictionaryTool() {
		initWidget(uiBinder.createAndBindUi(this));
		
		Window.addResizeHandler(new ResizeHandler() {
			  Timer resizeTimer = new Timer() {  
				  @Override
				  public void run() {
					  tabPanel.getElement().setAttribute("style", "height:"+ (Window.getClientHeight()-40)+ "px");
					  DOM.getElementById("editor_content").setAttribute("style", "height:"+ (Window.getClientHeight()-40)+ "px");
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
	}

}
