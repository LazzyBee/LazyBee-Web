package com.born2go.lazzybee.client.subpage;

import java.util.ArrayList;
import java.util.List;

import com.born2go.lazzybee.client.subpage.VocaEditorTool.DefiContainer;
import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

public class VocaView extends Composite {

	private static VocaViewUiBinder uiBinder = GWT
			.create(VocaViewUiBinder.class);

	interface VocaViewUiBinder extends UiBinder<Widget, VocaView> {
	}
	
	@UiField Label lbVocaQ;
	@UiField Anchor btnVocaPronounce;
	@UiField Label lbVocaLevel;
	@UiField Label lbVocaPronoun;
	@UiField HTMLPanel htmlDefiTable;
	@UiField HTMLPanel htmlVocaNote;
	
	private Voca voca;
	private List<DefiContainer> list_defitranforms = new ArrayList<DefiContainer>();

	public VocaView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public VocaView setVoca(Voca voca) {
		this.voca = voca;
		//-----
		lbVocaQ.setText(voca.getQ());
		lbVocaLevel.setText("Level: " + voca.getLevel());
		if(!voca.isCheck()) {
			htmlVocaNote.setVisible(true);
			htmlVocaNote.getElement().setInnerHTML("<span style=\"font-weight: bold;\">- "+voca.getQ()+" -</span> chưa được đưa vào từ điển chính thức.");
		}
		//-----
		JSONValue a = JSONParser.parseStrict(voca.getA());
		lbVocaPronoun.setText(a.isObject().get("pronoun").toString().replaceAll("\"", ""));
		JSONValue pac = a.isObject().get("packages");
		String[] packages = voca.getPackages().split(",");
		if(packages.length > 1) {
			for(int i = 1; i < packages.length; i++) {
				if(i == 1) {
					if(pac.isObject().get(packages[i]) == null)
						break;
					DefiContainer dc = new DefiContainer();
					dc.types.add(packages[i]);
					JSONValue defi = pac.isObject().get(packages[i]);
					dc.txbMeaning_id = defi.isObject().get("meaning").toString();
					dc.txbExplain_id = defi.isObject().get("explain").toString();
					dc.txbExam_id = defi.isObject().get("example").toString();
					list_defitranforms.add(dc);
				}
				else {
					if(pac.isObject().get(packages[i]) == null)
						break;
					boolean isNewDc = true;
					for(int j = i-1; j > 0; j--) {
						if(pac.isObject().get(packages[i]).toString().equals(pac.isObject().get(packages[j]).toString())) {
							isNewDc = false;
							for(DefiContainer dc: list_defitranforms) {
								if(dc.types.contains(packages[j])) {
									dc.types.add(packages[i]);
									break;
								}
							}
							break;
						}
					}
					if(isNewDc) {
						DefiContainer dc = new DefiContainer();
						dc.types.add(packages[i]);
						JSONValue defi = pac.isObject().get(packages[i]);
						dc.txbMeaning_id = defi.isObject().get("meaning").toString();
						dc.txbExplain_id = defi.isObject().get("explain").toString();
						dc.txbExam_id = defi.isObject().get("example").toString();
						list_defitranforms.add(dc);
					}
				}
			}
			readDefiTranforms();
		}
		return this;
	}
	
	private void readDefiTranforms() {
		// E-V Tab
		HTML EVContent = new HTML();
		EVContent.getElement().setAttribute("style", "padding-left: 5px; padding-top: 10px; padding-bottom: 20px; width: 588px");
		if(voca.getL_vn() != null && !voca.getL_vn().isEmpty())
			EVContent.setHTML(voca.getL_vn());
		else
			EVContent.setHTML("(Chưa có nội dung)");
		// E-E Tab
		HTML EEContent = new HTML();
		EEContent.getElement().setAttribute("style", "padding-left: 5px; padding-top: 10px; padding-bottom: 20px; width: 588px");
		if(voca.getL_en() != null && !voca.getL_en().isEmpty())
			EEContent.setHTML(voca.getL_en());
		else
			EEContent.setHTML("(Chưa có nội dung)");
		// Lazzybee meaning Tab
		HTML LazzyBeeContent = new HTML();
		LazzyBeeContent.getElement().setAttribute("style", "padding-left: 5px; padding-top: 10px; padding-bottom: 20px; width: 588px");
		for(DefiContainer dc: list_defitranforms) {
//			String dc_pac = "package: ";
//			for(String pac: dc.types) {
//				dc_pac = dc_pac + pac + ", ";
//			}
//			Label dcPac = new Label(dc_pac);
//			dcPac.setStyleName("VocaView_Obj5");
			String meaning = dc.txbMeaning_id.replaceAll("\"", "");
			String explain = dc.txbExplain_id.replaceAll("\"", "");
			String exam = dc.txbExam_id.replaceAll("\"", "").replace("<p>", "").replace("</p>", "");
			if(!meaning.contains("<p>"))
				meaning = "<p> " + meaning + " </p>";
			if(!explain.contains("<p>"))
				explain = "<p> " + explain + " </p>";
			if(!exam.contains("<p>"))
				exam = "<p> VD: " + exam + " </p>";
			LazzyBeeContent.setHTML(meaning + explain + "<span style='color: gray; font-style: italic;'>" + exam + "</span>");
		}
		// -----
		TabPanel tab = new TabPanel();
		tab.add(EVContent, "English - Tiếng Việt");
		tab.add(EEContent, "English - English");
		tab.add(LazzyBeeContent, "LazzyBee Card");
		tab.selectTab(0);
		tab.getElement().setAttribute("style", "margin-bottom: 10px");
		htmlDefiTable.add(tab);
	}
	
	@UiHandler("btnEdit")
	void onBtnEditClick(ClickEvent e) {
		Window.Location.assign("/editor/#vocabulary/" + voca.getQ());
	}

}
