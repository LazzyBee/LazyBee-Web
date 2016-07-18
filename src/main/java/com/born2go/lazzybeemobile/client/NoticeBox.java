package com.born2go.lazzybeemobile.client;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class NoticeBox {
	public NoticeBox() {

	}

	public void showNotice(String notice) {
		if (RootPanel.get("gwt_notice") != null) {
			RootPanel.get("gwt_notice").clear();
			RootPanel
					.get("gwt_notice")
					.getElement()
					.setAttribute(
							"style",
							"margin-top: 10px;; padding: 10px; background-color: lemonchiffon; line-height: 1.5; text-align: center;");
			RootPanel.get("gwt_notice").add(new Label(notice));
		}
	}

	public void hideNotice() {
		if (RootPanel.get("gwt_notice") != null) {
			RootPanel.get("gwt_notice").clear();
			RootPanel.get("gwt_notice").getElement()
					.setAttribute("style", "display: none;");
		}
	}

	public void showNoticeDialog(String notice) {
		final DialogBox d = new DialogBox();
		d.setStyleName("MTestTool_Obj12");
		d.setAutoHideEnabled(true);
		d.setGlassEnabled(true);
		d.setAnimationEnabled(true);
		Label header = new Label(notice);
		header.getElement()
				.setAttribute(
						"style",
						"color: #0066cc; text-align: center; font-size: 15px; font-weight: bold;  padding: 10px");
		d.add(header);
		d.center();
		
		Timer t = new Timer() {
			
			@Override
			public void run() {
				d.hide();
				
			}
			
		};
		t.schedule(3000);
	}
}
