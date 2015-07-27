package com.born2go.lazzybee.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class NoticeBox extends DialogBox {

	private static NoticeBoxUiBinder uiBinder = GWT
			.create(NoticeBoxUiBinder.class);

	interface NoticeBoxUiBinder extends UiBinder<Widget, NoticeBox> {
	}

	@UiField
	Label lbNotice;

	public NoticeBox(String notice) {
		setWidget(uiBinder.createAndBindUi(this));
		this.setStyleName("NoticeBox_clean");

		lbNotice.setText(notice);
		setPopupPosition((Window.getClientWidth() - this.getOffsetWidth()) / 2,
				40);
		show();
	}
	
	public void changeNotice(String notice) {
		lbNotice.setText(notice);
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

}
