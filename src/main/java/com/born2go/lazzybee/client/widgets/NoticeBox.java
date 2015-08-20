package com.born2go.lazzybee.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class NoticeBox extends Composite {

	private static NoticeBoxUiBinder uiBinder = GWT
			.create(NoticeBoxUiBinder.class);

	interface NoticeBoxUiBinder extends UiBinder<Widget, NoticeBox> {
	}

	@UiField
	HTMLPanel lbNotice;

	public NoticeBox(String notice) {
		initWidget(uiBinder.createAndBindUi(this));

		Label notice_lb = new Label(notice);
		lbNotice.add(notice_lb);
		int left = (Window.getClientWidth() / 2) - (this.getOffsetWidth() / 2);
		this.getElement().setAttribute("style", "left: " + left + "px");
	}
	
	public void setNotice(String notice) {
		lbNotice.clear();
		Label notice_lb = new Label(notice);
		lbNotice.add(notice_lb);
		int left = (Window.getClientWidth() / 2) - (this.getOffsetWidth() / 2);
		this.getElement().setAttribute("style", "left: " + left + "px; display:");
	}
	
	public void setRichNotice(String notice, String link1, String link2) {
		lbNotice.clear();
		Label notice_lb = new Label(notice);
		Anchor link_1 = new Anchor("View");
		link_1.setHref(link1);
		link_1.getElement().setAttribute("style", "margin-left: 10px");
		Anchor link_2 = new Anchor("Edit");
		link_2.setHref(link2);
		link_2.getElement().setAttribute("style", "margin-left: 10px");
		Anchor close = new Anchor();
		close.getElement().setInnerHTML("<i class='fa fa-times fa-lg'></i>");
		close.setStyleName("NoticeBox_Obj3");
		lbNotice.add(notice_lb);
		lbNotice.add(link_1);
		lbNotice.add(link_2);
		lbNotice.add(close);
		int left = (Window.getClientWidth() / 2) - (this.getOffsetWidth() / 2);
		this.getElement().setAttribute("style", "left: " + left + "px; display:");
		//-----
		close.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});
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
