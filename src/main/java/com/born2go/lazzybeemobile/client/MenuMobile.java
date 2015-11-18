package com.born2go.lazzybeemobile.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MenuMobile extends DialogBox {

	private static MenuMobileUiBinder uiBinder = GWT
			.create(MenuMobileUiBinder.class);

	interface MenuMobileUiBinder extends UiBinder<Widget, MenuMobile> {
	}

	@UiField
	VerticalPanel verMenu;
	@UiField
	HTML htmlMenu;

	public HTML getHtmlMenu() {
		return htmlMenu;
	}

	public MenuMobile() {
		setWidget(uiBinder.createAndBindUi(this));
		// Set the dialog box's caption.

		// Enable animation.
		setAnimationEnabled(true);

		 
		setAutoHideEnabled(true);

		setStyleName("cssDialogBox_clean");
		// DialogBox is a SimplePanel, so you have to set its widget property to

		verMenu.setWidth("100%");
		 
	}

}
