package com.born2go.lazzybeemobile.client;

import java.util.ArrayList;
import java.util.List;

import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.born2go.lazzybeemobile.client.MDictionaryView.DefiContainer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

/*
 * define ui for answer's content
 */
public class MVocaView extends Composite {

	private static Binder uiBinder = GWT.create(Binder.class);

	interface Binder extends UiBinder<Widget, MVocaView> {
	}

	@UiField
	Label lbVocaQ;
	@UiField
	Anchor btnVocaPronounce;
	@UiField
	Label lbVocaLevel;
	@UiField
	Label lbVocaPronoun;
	@UiField
	HTMLPanel htmlDefiTable;
	@UiField
	TabPanel tabPanel;
	@UiField
	HTML l_en;
	@UiField
	HTML l_vn;
	 
	private List<DefiContainer> list_defitranforms = new ArrayList<DefiContainer>();

	public MVocaView() {
		initWidget(uiBinder.createAndBindUi(this));
		tabPanel.selectTab(0);
	}

	// get data from voca
	public MVocaView setVoca(Voca voca) {
		 
		if (!voca.getL_en().equals("")) {
			l_en.setHTML(voca.getL_en());
		} else {
			l_en.setHTML("Từ điển chưa có mục này.");
		}
		if (!voca.getL_vn().equals(""))
			l_vn.setHTML(voca.getL_vn());
		else
			l_vn.setHTML("Vocabulary emtry");
		// -----
		lbVocaQ.setText(voca.getQ());
		lbVocaLevel.setText("Level: " + voca.getLevel());
		// -----
		JSONValue a = JSONParser.parseStrict(voca.getA());
		lbVocaPronoun.setText(a.isObject().get("pronoun").toString()
				.replaceAll("\"", ""));
		JSONValue pac = a.isObject().get("packages");
		String[] packages = voca.getPackages().split(",");
		if (packages.length > 1) {
			for (int i = 1; i < packages.length; i++) {
				if (i == 1) {
					if (pac.isObject().get(packages[i]) == null)
						break;
					DefiContainer dc = new DefiContainer();
					dc.types.add(packages[i]);
					JSONValue defi = pac.isObject().get(packages[i]);
					dc.txbMeaning_id = defi.isObject().get("meaning")
							.toString();
					dc.txbExplain_id = defi.isObject().get("explain")
							.toString();
					dc.txbExam_id = defi.isObject().get("example").toString();
					list_defitranforms.add(dc);
				} else {
					if (pac.isObject().get(packages[i]) == null)
						break;
					boolean isNewDc = true;
					for (int j = i - 1; j > 0; j--) {
						if (pac.isObject()
								.get(packages[i])
								.toString()
								.equals(pac.isObject().get(packages[j])
										.toString())) {
							isNewDc = false;
							for (DefiContainer dc : list_defitranforms) {
								if (dc.types.contains(packages[j])) {
									dc.types.add(packages[i]);
									break;
								}
							}
							break;
						}
					}
					if (isNewDc) {
						DefiContainer dc = new DefiContainer();
						dc.types.add(packages[i]);
						JSONValue defi = pac.isObject().get(packages[i]);
						dc.txbMeaning_id = defi.isObject().get("meaning")
								.toString();
						dc.txbExplain_id = defi.isObject().get("explain")
								.toString();
						dc.txbExam_id = defi.isObject().get("example")
								.toString();
						list_defitranforms.add(dc);
					}
				}
			}
			readDefiTranforms();
		}
		return this;
	}

	// ui for vosao
	private void readDefiTranforms() {
		for (DefiContainer dc : list_defitranforms) {
			HTMLPanel htmlDefi = new HTMLPanel("");
			String dc_pac = "package: ";
			for (String pac : dc.types) {
				dc_pac = dc_pac + pac + ", ";
			}
			Label dcPac = new Label(dc_pac);
			dcPac.setStyleName("MVocaView_Obj5");
			HTML dcContent = new HTML();
			dcContent.getElement().setAttribute("style", "padding-left: 5px;");
			dcContent.setHTML(dc.txbMeaning_id.replaceAll("\"", "")
					+ dc.txbExplain_id.replaceAll("\"", "")
					+ "<span style='color: gray; font-style: italic;'>"
					+ dc.txbExam_id.replaceAll("\"", "") + "</span>");
			// -----
			htmlDefi.add(dcPac);
			htmlDefi.add(dcContent);
			htmlDefiTable.add(htmlDefi);
		}
	}

}
