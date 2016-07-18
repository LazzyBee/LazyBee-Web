package com.born2go.lazzybee.client.subpage;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class DownloadView extends Composite {

	private static DownloadViewUiBinder uiBinder = GWT
			.create(DownloadViewUiBinder.class);

	interface DownloadViewUiBinder extends UiBinder<Widget, DownloadView> {
	}

	@UiField
	HTMLPanel contener;
	List<String> urls = new ArrayList<String>();

	public DownloadView() {
		initWidget(uiBinder.createAndBindUi(this));
		urls.add("http://www.lazzybee.com/vdict/");
		urls.add("http://www.lazzybee.com/vdict/#he");
		urls.add("http://www.lazzybee.com/vdict/#she");
		urls.add("http://www.lazzybee.com/vdict/#me");
		
		for (String url : urls) {
			HTMLPanel htmlU = new HTMLPanel("");
			htmlU.setStyleName("DView_O1");
			Anchor an = new Anchor("Download LazzyBee");
			an.setHref(url);
			htmlU.add(an);
			contener.add(htmlU);
		}

	}

}
