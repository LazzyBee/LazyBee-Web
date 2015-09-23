package com.born2go.lazzybee.client.subpage;

import java.util.ArrayList;
import java.util.List;

import com.born2go.lazzybee.client.LazzyBee;
import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.born2go.lazzybee.gdatabase.shared.nonentity.Category;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class VocaEditorTool extends Composite {

	private static VocaEditorToolUiBinder uiBinder = GWT
			.create(VocaEditorToolUiBinder.class);

	interface VocaEditorToolUiBinder extends UiBinder<Widget, VocaEditorTool> {
	}
	
	VocaEditorTool vet = this;
	
	@UiField HTMLPanel vocaEditorTool;
	@UiField HTMLPanel topToolbar;
	@UiField HTMLPanel htmlVocaNote;
	@UiField TextBox txbVocaDefi;
	@UiField Image checkVocaImg;
	@UiField TextBox txbPronoun;
	@UiField HTMLPanel defiTable;
	@UiField ListBox lsbLevel;
	@UiField ListBox lsbType;
	@UiField HTMLPanel htmlType;
	@UiField TextArea txbMeaning;
	@UiField TextArea txbExplain;
	@UiField TextArea txbExam;
	@UiField Anchor btnDelete;
	@UiField Anchor btnVerifySaveB;
	@UiField Anchor btnGoTop;
	@UiField Anchor btnSaveB;
	@UiField Anchor btnClose;
	
	@UiField CheckBox cbTypeCommon;
	@UiField CheckBox cbType850Basic;
	@UiField CheckBox cbTypeVoaEnglish;
	@UiField CheckBox cbTypeIelts;
	@UiField CheckBox cbTypeToefl;
	@UiField CheckBox cbTypeEconomic;
	@UiField CheckBox cbTypeIt;
	@UiField CheckBox cbTypeScience;
	@UiField CheckBox cbTypeMedicine;
	@UiField CheckBox cbTypeOther;
	
	int defi_count = 1;
	final String DEFI_TXBMEANING = "txbMeaning_";
	final String DEFI_TXBEXPLAIN = "txbExplain_";
	final String DEFI_TXBEXAM = "txbExam_";
	
	public static class DefiContainer {
		List<String> types = new ArrayList<String>();
		String txbMeaning_id;
		String txbExplain_id;
		String txbExam_id;
	}
	
	private Voca voca = null;
	private List<String> packages = new ArrayList<String>();
	private List<DefiContainer> list_defi = new ArrayList<DefiContainer>();
	private List<DefiContainer> list_defitranforms = new ArrayList<DefiContainer>();
	private List<ListBox> listLbType = new ArrayList<ListBox>();
	private boolean isPreviewMode = false;
	
	public void replaceEditor() {
		Timer t = new Timer() {
			@Override
			public void run() {
				replaceTxbNote(DEFI_TXBMEANING + 1, vet);
				replaceTxbNote(DEFI_TXBEXPLAIN + 1, vet);
				replaceTxbNote(DEFI_TXBEXAM + 1, vet);
			}
		};
		t.schedule(100);
	}
	
	public interface EditorListener {
		void onApproval(Voca v);
		void onClose();
	}
	
	private EditorListener listener;
	
	public void setListener(EditorListener listener) {
		this.listener = listener;
	}

	public VocaEditorTool() {
		initWidget(uiBinder.createAndBindUi(this));
		
		lsbType.addItem("- Chọn phân loại -");
		
		lsbLevel.addItem("1");
		lsbLevel.addItem("2");
		lsbLevel.addItem("3");
		lsbLevel.addItem("4");
		lsbLevel.addItem("5");
		lsbLevel.addItem("6");
		lsbLevel.addItem("7");
		lsbLevel.addItem("8");
		
		txbMeaning.getElement().setAttribute("id", DEFI_TXBMEANING + defi_count);
		txbExplain.getElement().setAttribute("id", DEFI_TXBEXPLAIN + defi_count);
		txbExam.getElement().setAttribute("id", DEFI_TXBEXAM + defi_count);
		
		cbTypeCommon.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				checkTypeListEvent(cbTypeCommon.getValue(), Category.COMMON, true);
			}
		});
		cbType850Basic.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				checkTypeListEvent(cbType850Basic.getValue(), Category.BASIC850, true);
			}
		});
		cbTypeVoaEnglish.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				checkTypeListEvent(cbTypeVoaEnglish.getValue(), Category.VOAENGLISH, true);
			}
		});
		cbTypeIelts.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				checkTypeListEvent(cbTypeIelts.getValue(), Category.IELTS, true);
			}
		});
		cbTypeToefl.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				checkTypeListEvent(cbTypeToefl.getValue(), Category.TOEFL, true);
			}
		});
		cbTypeEconomic.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				checkTypeListEvent(cbTypeEconomic.getValue(), Category.ECONOMIC, true);
			}
		});
		cbTypeIt.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				checkTypeListEvent(cbTypeIt.getValue(), Category.IT, true);
			}
		});
		cbTypeScience.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				checkTypeListEvent(cbTypeScience.getValue(), Category.SCIENCE, true);
			}
		});
		cbTypeMedicine.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				checkTypeListEvent(cbTypeMedicine.getValue(), Category.MEDICINE, true);
			}
		});
		cbTypeOther.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				checkTypeListEvent(cbTypeOther.getValue(), Category.OTHER, true);
			}
		});
		
		DefiContainer dc = new DefiContainer();
		dc.txbMeaning_id = DEFI_TXBMEANING + defi_count;
		dc.txbExplain_id = DEFI_TXBEXPLAIN + defi_count;
		dc.txbExam_id = DEFI_TXBEXAM + defi_count;
		list_defi.add(dc);
		
		lsbType.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				if(lsbType.getSelectedIndex() != 0) {
					final String typeValue = lsbType.getItemText(lsbType.getSelectedIndex());
					if(!list_defi.get(0).types.contains(typeValue)) {
						list_defi.get(0).types.add(typeValue);
						final Label type = new Label(typeValue);
						type.setStyleName("VocaEditorTool_Obj11");
						type.setTitle("Xóa phân loại này");
						htmlType.add(type);
						
						type.addClickHandler(new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {
								htmlType.remove(type);
								list_defi.get(0).types.remove(typeValue);
							}
						});
					}
					lsbType.setSelectedIndex(0);
				}
			}
		});		
		
		txbVocaDefi.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				if(!txbVocaDefi.getText().isEmpty()) {
					checkVocaImg.setVisible(true);
					LazzyBee.data_service.verifyVoca(txbVocaDefi.getText(), new AsyncCallback<Boolean>() {
						@Override
						public void onSuccess(Boolean result) {
							checkVocaImg.setVisible(false);
							if(!result) {
								txbVocaDefi.getElement().setAttribute("style", "border: 1px solid red;");
								LazzyBee.noticeBox.setNotice("");
								LazzyBee.noticeBox.setRichNotice("- " + txbVocaDefi.getText() + " - Đã có trong từ điển", "/library/#dictionary/"+txbVocaDefi.getText(), "/editor/#vocabulary/"+txbVocaDefi.getText());
							}
							else {
								txbVocaDefi.getElement().setAttribute("style", "border: 1px solid #b6b6b6;");
								LazzyBee.noticeBox.hide();
							}
						}
						
						@Override
						public void onFailure(Throwable caught) {
							checkVocaImg.setVisible(false);
							txbVocaDefi.getElement().setAttribute("style", "border: 1px solid red;");
							LazzyBee.noticeBox.setNotice("! Đã có lỗi xảy ra khi kiểm tra - " + txbVocaDefi.getText());
							LazzyBee.noticeBox.setAutoHide();
						}
					});
				}
			}
		});
	}
	
	public void setPreviewMode() {
		topToolbar.setVisible(false);
		btnGoTop.setVisible(false);
//		btnSaveB.setVisible(false);
		btnVerifySaveB.setVisible(true);
		btnClose.setVisible(true);
		isPreviewMode = true;
		txbVocaDefi.setEnabled(false);
		htmlVocaNote.setVisible(false);
	}
	
	public void setVoca(Voca voca) {
		this.voca = voca;
		btnDelete.setVisible(true);
		if(!voca.isCheck()) {
			htmlVocaNote.setVisible(true);
			htmlVocaNote.getElement().setInnerHTML("<span style=\"font-weight: bold;\">- "+voca.getQ()+" -</span> chưa được đưa vào từ điển chính thức.");
		}
//		btnVerifySaveB.setVisible(true);
		//-----
		txbVocaDefi.setText(voca.getQ());
		//-----
		lsbLevel.setSelectedIndex(Integer.parseInt(voca.getLevel()) - 1);
		//-----
		String[] packages = voca.getPackages().split(",");
		if(packages.length > 1) 
			for(int i = 1; i < packages.length; i++) {
				if(packages[i].equals(Category.COMMON)) {
					cbTypeCommon.setValue(true);
					checkTypeListEvent(true, Category.COMMON, false);
				}
				if(packages[i].equals(Category.BASIC850)) {
					cbType850Basic.setValue(true);
					checkTypeListEvent(true, Category.BASIC850, false);
				}
				if(packages[i].equals(Category.VOAENGLISH)) {
					cbTypeVoaEnglish.setValue(true);
					checkTypeListEvent(true, Category.VOAENGLISH, false);
				}
				if(packages[i].equals(Category.IELTS)) {
					cbTypeIelts.setValue(true);
					checkTypeListEvent(true, Category.IELTS, false);
				}
				if(packages[i].equals(Category.TOEFL)) {
					cbTypeToefl.setValue(true);
					checkTypeListEvent(true, Category.TOEFL, false);
				}
				if(packages[i].equals(Category.ECONOMIC)) {
					cbTypeEconomic.setValue(true);
					checkTypeListEvent(true, Category.ECONOMIC, false);
				}
				if(packages[i].equals(Category.IT)) {
					cbTypeIt.setValue(true);
					checkTypeListEvent(true, Category.IT, false);
				}
				if(packages[i].equals(Category.SCIENCE)) {
					cbTypeScience.setValue(true);
					checkTypeListEvent(true, Category.SCIENCE, false);
				}
				if(packages[i].equals(Category.MEDICINE)) {
					cbTypeMedicine.setValue(true);
					checkTypeListEvent(true, Category.MEDICINE, false);
				}
				if(packages[i].equals(Category.OTHER)) {
					cbTypeOther.setValue(true);
					checkTypeListEvent(true, Category.OTHER, false);
				}
			}
		//-----
		JSONValue a = JSONParser.parseStrict(voca.getA());
		txbPronoun.setText(a.isObject().get("pronoun").toString().replaceAll("\"", ""));
		JSONValue pac = a.isObject().get("packages");
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
	}
	
	private void readDefiTranforms() {
		list_defi.clear();
		//add first defi
		final DefiContainer dc = new DefiContainer();
		dc.txbMeaning_id = DEFI_TXBMEANING + defi_count;
		dc.txbExplain_id = DEFI_TXBEXPLAIN + defi_count;
		dc.txbExam_id = DEFI_TXBEXAM + defi_count;
		list_defi.add(dc);
		//read form first defi_tranforms
		final DefiContainer firstDefi = list_defitranforms.get(0);
		dc.types = firstDefi.types;
//		Timer t = new Timer() {
//			@Override
//			public void run() {
//				setDataCustomEditor(dc.txbMeaning_id, firstDefi.txbMeaning_id.replaceAll("\"", ""));
//				setDataCustomEditor(dc.txbExplain_id, firstDefi.txbExplain_id.replaceAll("\"", ""));
//				setDataCustomEditor(dc.txbExam_id, firstDefi.txbExam_id.replaceAll("\"", ""));
//			}
//		};
//		t.schedule(800);
		for(String pac: firstDefi.types) {
			final String p = pac;
			final Label type = new Label(p);
			type.setStyleName("VocaEditorTool_Obj11");
			type.setTitle("Xóa phân loại này");
			htmlType.add(type);
			//-----Event handler-----
			type.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					htmlType.remove(type);
					list_defi.get(0).types.remove(p);
				}
			});
		}
		//read all list defi_tranforms
		for(int i = 1; i < list_defitranforms.size(); i++) {
			defi_count++;
			addDefiPanel(list_defitranforms.get(i));
		}
	}
	
	private void checkTypeListEvent(boolean isCheck, final String type, boolean isAutoAddPackage) {
		if(isCheck) {
			lsbType.addItem(type);
			for(ListBox lb: listLbType)
				lb.addItem(type);
			packages.add(type);
			if(isAutoAddPackage && list_defi.size() == 1) {
				list_defi.get(0).types.add(type);
				final Label lbtype = new Label(type);
				lbtype.setStyleName("VocaEditorTool_Obj11");
				lbtype.setTitle("Xóa phân loại này");
				htmlType.add(lbtype);
				
				lbtype.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						htmlType.remove(lbtype);
						list_defi.get(0).types.remove(type);
					}
				});
			}
		}
		else {
			lsbType.removeItem(packages.indexOf(type)+ 1);
			for(ListBox lb: listLbType)
				lb.removeItem(packages.indexOf(type)+ 1);
			packages.remove(type);
			for(DefiContainer dc: list_defi)
				dc.types.remove(type);
		}
	}
	
	public static native void replaceTxbNote(String txbNoteId, VocaEditorTool vet) /*-{
	 	var noteId = txbNoteId;
	  	var editor = $wnd.CKEDITOR.replace( noteId, {
	  		width: '405px',
	  		height: '50px',
	  		contentsCss : 'body {overflow:hidden;}',
	  		autoGrow_minHeight: 10,
	  		toolbarStartupExpanded : false,
	  	});
	  	
	  	editor.on("instanceReady",function() {
  			$wnd.document.getElementById(editor.id+'_top').style.display = "none";
  			vet.@com.born2go.lazzybee.client.subpage.VocaEditorTool::onCkEditorInstanceReady(Ljava/lang/String;)(noteId);
		});
	  	
	  	editor.on('focus', function(){	 
	        $wnd.document.getElementById(editor.id+'_top').style.display = "block";
	    });
	   
	    editor.on('blur', function(){	       
	        $wnd.document.getElementById(editor.id+'_top').style.display = "none";
	    });
	}-*/;
	
	public static native String getDataCustomEditor(String editorId) /*-{
		var eid = editorId;
		var editor = $wnd.document.getElementById("cke_"+ eid);
		if(editor != null) {
			var data = $wnd.CKEDITOR.instances[eid].getData();
			return data;
		}
		else
			return "";
	}-*/;
	
	public static native void setDataCustomEditor(String editorId, String data) /*-{
		var eid = editorId;
		var d = data;
		var editor = $wnd.document.getElementById("cke_"+ eid);
		if(editor != null) {
			$wnd.CKEDITOR.instances[eid].setData(d);
		}
	}-*/;
	
	public static native void destroyCustomEditor(String editorId) /*-{
		var eid = editorId;
		var editor = $wnd.document.getElementById("cke_"+ eid);
		if(editor != null) {
			$wnd.CKEDITOR.instances[eid].setData("");
			$wnd.CKEDITOR.instances[eid].destroy();
		}
	}-*/;
	
	void onCkEditorInstanceReady(String ckId) {
		if(!list_defitranforms.isEmpty()) {
			String ckIds[] = ckId.split("_");
			int index = Integer.valueOf(ckIds[1]) - 1;
			if(index < list_defitranforms.size()) {
				if(ckIds[0].equals("txbMeaning")) {
					DefiContainer dc = list_defitranforms.get(index);
					if(!dc.txbMeaning_id.equals("\"\""))
						setDataCustomEditor(ckId, dc.txbMeaning_id.replaceAll("\"", ""));
				}
				if(ckIds[0].equals("txbExplain")) {
					DefiContainer dc = list_defitranforms.get(index);
					if(!dc.txbExplain_id.equals("\"\""))
						setDataCustomEditor(ckId, dc.txbExplain_id.replaceAll("\"", ""));
				}
				if(ckIds[0].equals("txbExam")) {
					DefiContainer dc = list_defitranforms.get(index);
					if(!dc.txbExam_id.equals("\"\""))
						setDataCustomEditor(ckId, dc.txbExam_id.replaceAll("\"", ""));
				}
			}
		}
	}
	
	private void addDefiPanel( ) {
		final HTML html = new HTML();
		html.setHTML("<br/> <div style='border-top: 1px solid silver; margin-bottom: 25px; margin-top: 10px; width: 575px;'></div>");
		defiTable.add(html);
		final HTMLPanel htmlp = new HTMLPanel("");
		//---
		HorizontalPanel hor1 = new HorizontalPanel();
		hor1.setStyleName("VocaEditorTool_Obj3");
		Label lb1 = new Label("Giải nghĩa cho");
		lb1.setStyleName("VocaEditorTool_Obj2");
		final HTMLPanel htmlType = new HTMLPanel("");
		htmlType.setWidth("400px");
		final ListBox lsbType = new ListBox();
		lsbType.setSize("140px", "30px");
		lsbType.setStyleName("VocaEditorTool_Obj4 VocaEditorTool_Obj10");
		lsbType.addItem("- Chọn phân loại -");
		for(String type: packages)
			lsbType.addItem(type);
		htmlType.add(lsbType);
		hor1.add(lb1);
		hor1.add(htmlType);
		htmlp.add(hor1);
		listLbType.add(lsbType);
		//---
		HorizontalPanel hor2 = new HorizontalPanel();
		hor2.setStyleName("VocaEditorTool_Obj3");
		HTMLPanel html_mean = new HTMLPanel("");
		html_mean.setStyleName("VocaEditorTool_Obj2");
		Label lb2 = new Label("Nghĩa");
		lb2.setStyleName("VocaEditorTool_Obj2");
		Label lb2note = new Label("(Tiếng Việt)");
		lb2note.getElement().setAttribute("style", "font-weight: 500; position: relative; top: 6px;");
		html_mean.add(lb2);
		html_mean.add(lb2note);
		TextArea txbMeaning = new TextArea();
		txbMeaning.setStyleName("VocaEditorTool_Obj4");
		txbMeaning.getElement().setAttribute("id", DEFI_TXBMEANING + defi_count);
		hor2.add(html_mean);
		hor2.add(txbMeaning);
		htmlp.add(hor2);
		//---
		HorizontalPanel hor3 = new HorizontalPanel();
		hor3.setStyleName("VocaEditorTool_Obj3");
		HTMLPanel html_explain = new HTMLPanel("");
		html_explain.setStyleName("VocaEditorTool_Obj2");
		Label lb3 = new Label("Giải thích");
		lb3.setStyleName("VocaEditorTool_Obj2");
		Label lb3note = new Label("(Tiếng Anh)");
		lb3note.getElement().setAttribute("style", "font-weight: 500; position: relative; top: 6px;");
		html_explain.add(lb3);
		html_explain.add(lb3note);
		TextArea txbExplain = new TextArea();
		txbExplain.setStyleName("VocaEditorTool_Obj4");
		txbExplain.getElement().setAttribute("id", DEFI_TXBEXPLAIN + defi_count);
		hor3.add(html_explain);
		hor3.add(txbExplain);
		htmlp.add(hor3);
		//---
		HorizontalPanel hor4 = new HorizontalPanel();
		hor4.setStyleName("VocaEditorTool_Obj3");
		Label lb4 = new Label("Ví dụ");
		lb4.setStyleName("VocaEditorTool_Obj2");
		TextArea txbExam = new TextArea();
		txbExam.setStyleName("VocaEditorTool_Obj4");
		txbExam.getElement().setAttribute("id", DEFI_TXBEXAM + defi_count);
		hor4.add(lb4);
		hor4.add(txbExam);
		htmlp.add(hor4);
		//---
		HorizontalPanel hor5 = new HorizontalPanel();
		hor5.setStyleName("VocaEditorTool_Obj3");
		Label lb5 = new Label("Hình ảnh minh họa");
		lb5.setStyleName("VocaEditorTool_Obj2");
		Anchor ac = new Anchor();
		ac.setTitle("Upload");
		ac.setStyleName("VocaEditorTool_Obj6");
		ac.getElement().setInnerHTML("<i class='fa fa-upload fa-lg' style='color:silver'></i>");
		hor5.add(lb5);
		hor5.add(ac);
		htmlp.add(hor5);
		//---
		Anchor deleteDefi = new Anchor("Xóa giải nghĩa này");
		deleteDefi.setStyleName("VocaEditorTool_Obj5 VocaEditorTool_Obj6");
		deleteDefi.getElement().setAttribute("style", "color:red");
		htmlp.add(deleteDefi);
		//---
		defiTable.add(htmlp);
		//-----Add Defi Container-----
		final DefiContainer dc = new DefiContainer();
		dc.txbMeaning_id = DEFI_TXBMEANING + defi_count;
		dc.txbExplain_id = DEFI_TXBEXPLAIN + defi_count;
		dc.txbExam_id = DEFI_TXBEXAM + defi_count;
		list_defi.add(dc);
		//-----event handler-----
		lsbType.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				if(lsbType.getSelectedIndex() != 0) {
					final String typeValue = lsbType.getItemText(lsbType.getSelectedIndex());
					if(!dc.types.contains(typeValue)) {
						dc.types.add(typeValue);
						final Label type = new Label(typeValue);
						type.setStyleName("VocaEditorTool_Obj11");
						type.setTitle("Xóa phân loại này");
						htmlType.add(type);
						
						type.addClickHandler(new ClickHandler() {
							
							@Override
							public void onClick(ClickEvent event) {
								htmlType.remove(type);
								dc.types.remove(typeValue);
							}
						});
					}
					lsbType.setSelectedIndex(0);
				}
			}
		});	
		deleteDefi.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				defi_count--;
				defiTable.remove(htmlp);
				defiTable.remove(html);
				list_defi.remove(dc);
				listLbType.remove(lsbType);
			}
		});
		//-----timer-----
		Timer t = new Timer() {
			@Override
			public void run() {
				replaceTxbNote(dc.txbMeaning_id, vet);
				replaceTxbNote(dc.txbExplain_id, vet);
				replaceTxbNote(dc.txbExam_id, vet);
			}
		};
		t.schedule(100);
		Timer t2 = new Timer() {
			@Override
			public void run() {
				DOM.getElementById("content").setScrollTop(vocaEditorTool.getOffsetHeight());
			}
		};
		t2.schedule(150);
	}
	
	private void addDefiPanel(final DefiContainer defiTranforms) {
		final HTML html = new HTML();
		html.setHTML("<br/> <div style='border-top: 1px solid silver; margin-bottom: 25px; margin-top: 10px; width: 575px;'></div>");
		defiTable.add(html);
		final HTMLPanel htmlp = new HTMLPanel("");
		//---
		HorizontalPanel hor1 = new HorizontalPanel();
		hor1.setStyleName("VocaEditorTool_Obj3");
		Label lb1 = new Label("Giải nghĩa cho");
		lb1.setStyleName("VocaEditorTool_Obj2");
		final HTMLPanel htmlType = new HTMLPanel("");
		htmlType.setWidth("400px");
		final ListBox lsbType = new ListBox();
		lsbType.setSize("140px", "30px");
		lsbType.setStyleName("VocaEditorTool_Obj4 VocaEditorTool_Obj10");
		lsbType.addItem("- Chọn phân loại -");
		for(String type: packages)
			lsbType.addItem(type);
		htmlType.add(lsbType);
		hor1.add(lb1);
		hor1.add(htmlType);
		htmlp.add(hor1);
		listLbType.add(lsbType);
		//---
		HorizontalPanel hor2 = new HorizontalPanel();
		hor2.setStyleName("VocaEditorTool_Obj3");
		HTMLPanel html_mean = new HTMLPanel("");
		html_mean.setStyleName("VocaEditorTool_Obj2");
		Label lb2 = new Label("Nghĩa");
		lb2.setStyleName("VocaEditorTool_Obj2");
		Label lb2note = new Label("(Tiếng Việt)");
		lb2note.getElement().setAttribute("style", "font-weight: 500; position: relative; top: 6px;");
		html_mean.add(lb2);
		html_mean.add(lb2note);
		TextArea txbMeaning = new TextArea();
		txbMeaning.setStyleName("VocaEditorTool_Obj4");
		txbMeaning.getElement().setAttribute("id", DEFI_TXBMEANING + defi_count);
		hor2.add(html_mean);
		hor2.add(txbMeaning);
		htmlp.add(hor2);
		//---
		HorizontalPanel hor3 = new HorizontalPanel();
		hor3.setStyleName("VocaEditorTool_Obj3");
		HTMLPanel html_explain = new HTMLPanel("");
		html_explain.setStyleName("VocaEditorTool_Obj2");
		Label lb3 = new Label("Giải thích");
		lb3.setStyleName("VocaEditorTool_Obj2");
		Label lb3note = new Label("(Tiếng Anh)");
		lb3note.getElement().setAttribute("style", "font-weight: 500; position: relative; top: 6px;");
		html_explain.add(lb3);
		html_explain.add(lb3note);
		TextArea txbExplain = new TextArea();
		txbExplain.setStyleName("VocaEditorTool_Obj4");
		txbExplain.getElement().setAttribute("id", DEFI_TXBEXPLAIN + defi_count);
		hor3.add(html_explain);
		hor3.add(txbExplain);
		htmlp.add(hor3);
		//---
		HorizontalPanel hor4 = new HorizontalPanel();
		hor4.setStyleName("VocaEditorTool_Obj3");
		Label lb4 = new Label("Ví dụ");
		lb4.setStyleName("VocaEditorTool_Obj2");
		TextArea txbExam = new TextArea();
		txbExam.setStyleName("VocaEditorTool_Obj4");
		txbExam.getElement().setAttribute("id", DEFI_TXBEXAM + defi_count);
		hor4.add(lb4);
		hor4.add(txbExam);
		htmlp.add(hor4);
		//---
		HorizontalPanel hor5 = new HorizontalPanel();
		hor5.setStyleName("VocaEditorTool_Obj3");
		Label lb5 = new Label("Hình ảnh minh họa");
		lb5.setStyleName("VocaEditorTool_Obj2");
		Anchor ac = new Anchor();
		ac.setTitle("Upload");
		ac.setStyleName("VocaEditorTool_Obj6");
		ac.getElement().setInnerHTML("<i class='fa fa-upload fa-lg' style='color:silver'></i>");
		hor5.add(lb5);
		hor5.add(ac);
		htmlp.add(hor5);
		//---
		Anchor deleteDefi = new Anchor("Xóa giải nghĩa này");
		deleteDefi.setStyleName("VocaEditorTool_Obj5 VocaEditorTool_Obj6");
		deleteDefi.getElement().setAttribute("style", "color:red");
		htmlp.add(deleteDefi);
		//---
		defiTable.add(htmlp);
		//-----Add Defi Container-----
		final DefiContainer dc = new DefiContainer();
		dc.txbMeaning_id = DEFI_TXBMEANING + defi_count;
		dc.txbExplain_id = DEFI_TXBEXPLAIN + defi_count;
		dc.txbExam_id = DEFI_TXBEXAM + defi_count;
		list_defi.add(dc);
		dc.types = defiTranforms.types;
		for(String pac: defiTranforms.types) {
			final String p = pac;
			final Label type = new Label(p);
			type.setStyleName("VocaEditorTool_Obj11");
			type.setTitle("Xóa phân loại này");
			htmlType.add(type);
			//-----Event handler-----
			type.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					htmlType.remove(type);
					dc.types.remove(p);
				}
			});
		}
		//-----event handler-----
		lsbType.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				if(lsbType.getSelectedIndex() != 0) {
					final String typeValue = lsbType.getItemText(lsbType.getSelectedIndex());
					if(!dc.types.contains(typeValue)) {
						dc.types.add(typeValue);
						final Label type = new Label(typeValue);
						type.setStyleName("VocaEditorTool_Obj11");
						type.setTitle("Xóa phân loại này");
						htmlType.add(type);
						//-----Event handler-----
						type.addClickHandler(new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {
								htmlType.remove(type);
								dc.types.remove(typeValue);
							}
						});
					}
					lsbType.setSelectedIndex(0);
				}
			}
		});	
		deleteDefi.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				defi_count--;
				defiTable.remove(htmlp);
				defiTable.remove(html);
				list_defi.remove(dc);
				listLbType.remove(lsbType);
			}
		});
		//-----timer-----
		Timer t = new Timer() {
			@Override
			public void run() {
				replaceTxbNote(dc.txbMeaning_id, vet);
				replaceTxbNote(dc.txbExplain_id, vet);
				replaceTxbNote(dc.txbExam_id, vet);
			}
		};
		t.schedule(100);
//		Timer t2 = new Timer() {
//			@Override
//			public void run() {
//				setDataCustomEditor(dc.txbMeaning_id, defiTranforms.txbMeaning_id.replaceAll("\"", ""));
//				setDataCustomEditor(dc.txbExplain_id, defiTranforms.txbExplain_id.replaceAll("\"", ""));
//				setDataCustomEditor(dc.txbExam_id, defiTranforms.txbExam_id.replaceAll("\"", ""));
//			}
//		};
//		t2.schedule(800);
	}
	
	private String getJsonData() {
		String data = "";
		//Define answers
		JSONObject a = new JSONObject();
		a.put("q", new JSONString(txbVocaDefi.getText()));
		a.put("pronoun", new JSONString(txbPronoun.getText()));
		//Define package
		JSONObject pac = new JSONObject();
		for(DefiContainer dc: list_defi) {
			for(String pacName: dc.types) {
				JSONObject category = new JSONObject();
				category.put("meaning", new JSONString(getDataCustomEditor(dc.txbMeaning_id)));
				category.put("explain", new JSONString(getDataCustomEditor(dc.txbExplain_id)));
				category.put("example", new JSONString(getDataCustomEditor(dc.txbExam_id)));
				pac.put(pacName, category);
			}
		}
		a.put("packages", pac);
		data = a.toString();
		return data;
	}
	
	private String getPackages() {
		String spackages = "";
		for(String pac: packages) {
			spackages = spackages + "," + pac;
		}
		spackages = spackages + ",";
		return spackages;
	}
	
	private void saveNewVoca() {
		if(verifyField()) {
			LazzyBee.noticeBox.setNotice("Đang tải lên... ");
			final Voca v = new Voca();
			v.setQ(txbVocaDefi.getText());
			v.setLevel(lsbLevel.getValue(lsbLevel.getSelectedIndex()));
			v.setA(getJsonData());
			v.setPackages(getPackages());
			LazzyBee.data_service.insertVoca(v, new AsyncCallback<Voca>() {
				@Override
				public void onSuccess(Voca result) {
					if(result != null) {
						formClean();
						DOM.getElementById("content").setScrollTop(0);
						LazzyBee.noticeBox.setRichNotice("- "+ v.getQ()+ " - đã được thêm vào từ điển", "/library/#dictionary/" + v.getQ(), "/editor/#vocabulary/" + v.getQ());
					} 
					else {
						LazzyBee.noticeBox.setRichNotice("- "+ v.getQ()+ " - đã có trong từ điển", "/library/#dictionary/" + v.getQ(), "/editor/#vocabulary/" + v.getQ());
					}
				}
				
				@Override
				public void onFailure(Throwable caught) {
					LazzyBee.noticeBox.setNotice("! Đã có lỗi xảy ra khi tải lên");
					LazzyBee.noticeBox.setAutoHide();
				}
			});
		}
	}
	
	private void updateVoca(boolean isCheck) {
		if(verifyField()) {
			LazzyBee.noticeBox.setNotice("Đang tải lên... ");
			final Voca v = voca;
			v.setGid(voca.getGid());
			v.setQ(txbVocaDefi.getText());
			v.setLevel(lsbLevel.getValue(lsbLevel.getSelectedIndex()));
			v.setA(getJsonData());
			v.setPackages(getPackages());
			LazzyBee.data_service.updateVoca(v, isCheck, new AsyncCallback<Voca>() {
				@Override
				public void onSuccess(Voca result) {
					if(result != null) {
						if(!isPreviewMode)
							formClean();
						else
							if(listener != null)
								listener.onApproval(result);
						DOM.getElementById("content").setScrollTop(0);
						LazzyBee.noticeBox.setRichNotice("- "+ v.getQ()+ " - đã được cập nhật", "/library/#dictionary/" + v.getQ(), "/editor/#vocabulary/" + v.getQ());
					}
					else {
						LazzyBee.noticeBox.setRichNotice("- "+ v.getQ()+ " - cập nhật thất bại", "/library/#dictionary/" + v.getQ(), "/editor/#vocabulary/" + v.getQ());
					}
				}
				
				@Override
				public void onFailure(Throwable caught) {
					LazzyBee.noticeBox.setNotice("! Đã có lỗi xảy ra khi tải lên");
					LazzyBee.noticeBox.setAutoHide();
				}
			});
		}
	}
	
	private void formClean() {
		String newURL = Window.Location.createUrlBuilder().setHash("vocabulary").buildString();
		Window.Location.replace(newURL);
		htmlVocaNote.setVisible(false);
		btnDelete.setVisible(false);
		btnVerifySaveB.setVisible(false);
		txbVocaDefi.setText("");
		txbVocaDefi.getElement().setAttribute("style", "");
		txbPronoun.setText("");
		txbPronoun.getElement().setAttribute("style", "");
		lsbType.setSelectedIndex(0);
		destroyCustomEditor(DEFI_TXBMEANING + "1");
		destroyCustomEditor(DEFI_TXBEXPLAIN + "1");
		destroyCustomEditor(DEFI_TXBEXAM + "1");
		cbTypeCommon.setValue(false);
		cbType850Basic.setValue(false);
		cbTypeVoaEnglish.setValue(false);
		cbTypeIelts.setValue(false);
		cbTypeToefl.setValue(false);
		cbTypeEconomic.setValue(false);
		cbTypeIt.setValue(false);
		cbTypeScience.setValue(false);
		cbTypeMedicine.setValue(false);
		cbTypeOther.setValue(false);
		htmlType.clear();
		packages.clear();
		lsbType.clear();
		defiTable.clear();
		list_defi.clear();
		listLbType.clear();
		list_defitranforms.clear();
		voca = null;
		//-----startup over-----
		lsbType.addItem("- Chọn phân loại -");
		htmlType.add(lsbType);
		replaceTxbNote(DEFI_TXBMEANING + 1, vet);
		replaceTxbNote(DEFI_TXBEXPLAIN + 1, vet);
		replaceTxbNote(DEFI_TXBEXAM + 1, vet);
		defi_count = 1;
		DefiContainer dc = new DefiContainer();
		dc.txbMeaning_id = DEFI_TXBMEANING + defi_count;
		dc.txbExplain_id = DEFI_TXBEXPLAIN + defi_count;
		dc.txbExam_id = DEFI_TXBEXAM + defi_count;
		list_defi.add(dc);
	}
	
	@UiHandler("btnSave")
	void onBtnSaveClick(ClickEvent e) {
		if(voca == null)
			saveNewVoca();
		else
			updateVoca(false);
	}
	
	@UiHandler("btnSaveB")
	void onBtnSaveBClick(ClickEvent e) {
		if(voca == null)
			saveNewVoca();
		else
			updateVoca(false);
	}
	
	@UiHandler("btnVerifySaveB")
	void onBtnVerifySaveBClick(ClickEvent e) {
		if(voca != null)
			updateVoca(true);
	}
	
	@UiHandler("btnDelete")
	void onBtnDeleteClick(ClickEvent e) {
		if(Window.confirm("Bạn muốn xóa từ - " + voca.getQ() + " -?")) {
			if(voca != null) {
				final String voca_q = voca.getQ();
				LazzyBee.data_service.removeVoca(voca, new AsyncCallback<Void>() {
					@Override
					public void onSuccess(Void result) {
						formClean();
						LazzyBee.noticeBox.setNotice("- "+ voca_q + " - đã bị xóa");
						LazzyBee.noticeBox.setAutoHide();
					}
					
					@Override
					public void onFailure(Throwable caught) {
						LazzyBee.noticeBox.setNotice("! Đã có lỗi xảy ra khi tải lên");
						LazzyBee.noticeBox.setAutoHide();
					}
				});
			}
		}
	}

	@UiHandler("btnAddDefi")
	void onBtnAddDefiClick(ClickEvent e) {
		defi_count++;
		addDefiPanel();
	}
	
	@UiHandler("btnGoTop")
	void onBtnGoTopClick(ClickEvent e) {
		DOM.getElementById("content").setScrollTop(0);
	}
	
	@UiHandler("btnClear")
	void onBtnClearClick(ClickEvent e) {
		formClean();
	}
	
	@UiHandler("btnClose")
	void onBtnCloseClick(ClickEvent e) {
		if(listener != null)
			listener.onClose();
	}
	
	boolean verifyField() {
		boolean verify = true;
		if(txbVocaDefi.getText().equals("")) {
			verify = false;
			txbVocaDefi.getElement().setAttribute("style", "border: 1px solid red;");
			DOM.getElementById("content").setScrollTop(0);
		}
		if(txbPronoun.getText().equals("")) {
			verify = false;
			txbPronoun.getElement().setAttribute("style", "border: 1px solid red;");
			DOM.getElementById("content").setScrollTop(0);
		}
		return verify;
	}

}
