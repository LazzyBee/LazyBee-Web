package com.born2go.lazzybee.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class NoticeBox extends Composite {

	private static NoticeBoxUiBinder uiBinder = GWT
			.create(NoticeBoxUiBinder.class);

	interface NoticeBoxUiBinder extends UiBinder<Widget, NoticeBox> {
	}

	@UiField
	HTML lbNotice;

	public NoticeBox(String notice) {
		initWidget(uiBinder.createAndBindUi(this));

		lbNotice.setText(notice);
		int left = (Window.getClientWidth() / 2) - (this.getOffsetWidth() / 2);
		this.getElement().setAttribute("style", "left: " + left + "px");
	}
	
	public void setNotice(String notice) {
		lbNotice.setText(notice);
		int left = (Window.getClientWidth() / 2) - (this.getOffsetWidth() / 2);
		this.getElement().setAttribute("style", "left: " + left + "px; display:");
	}

	public void setAutoHide() {
		Timer t = new Timer() {
			@Override
			public void run() {
				hide();
			}
		};
		t.schedule(1500);
	}
	
	public void hide() {
		this.getElement().setAttribute("style", "display: none");
	}

}
