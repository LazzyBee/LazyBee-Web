package com.born2go.lazzybee.client.widgets;

import java.util.ArrayList;
import java.util.List;

import com.born2go.lazzybee.client.LazzyBee;
import com.born2go.lazzybee.gdatabase.shared.Category;
import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class VocaEditorTool extends Composite {

	private static VocaEditorToolUiBinder uiBinder = GWT
			.create(VocaEditorToolUiBinder.class);

	interface VocaEditorToolUiBinder extends UiBinder<Widget, VocaEditorTool> {
	}
	
	@UiField TextBox txbVocaDefi;
	@UiField TextBox txbPronoun;
	@UiField HTMLPanel defiTable;
	@UiField ListBox lsbLevel;
	@UiField ListBox lsbType;
	@UiField HTMLPanel htmlType;
	@UiField TextArea txbMeaning;
	@UiField TextArea txbExplain;
	@UiField TextArea txbExam;
	
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
	final String DEFI_TXBMEANING = "VocaEditorTool_txbMeaning";
	final String DEFI_TXBEXPLAIN = "VocaEditorTool_txbExplain";
	final String DEFI_TXBEXAM = "VocaEditorTool_txbExam";
	
	ScrollPanel tabContentScp;
	
	class DefiContainer {
		List<String> types = new ArrayList<String>();
		String txbMeaning_id;
		String txbExplain_id;
		String txbExam_id;
	}
	
	private List<String> packages = new ArrayList<String>();
	private List<DefiContainer> list_defi = new ArrayList<DefiContainer>();

	public VocaEditorTool() {
		initWidget(uiBinder.createAndBindUi(this));
		
		lsbType.addItem("- Chọn phân loại -");
//		lsbType.addItem(Category.ECONOMIC);
//		lsbType.addItem(Category.IT);
//		lsbType.addItem(Category.MEDICINE);
//		lsbType.addItem(Category.SCIENCE);
//		lsbType.addItem(Category.BASIC850);
//		lsbType.addItem(Category.VOAENGLISH);
//		lsbType.addItem(Category.IELTS);
//		lsbType.addItem(Category.TOEFL);
//		lsbType.addItem(Category.OTHER);
		
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
		Timer t = new Timer() {
			@Override
			public void run() {
				replaceTxbNote(DEFI_TXBMEANING + defi_count);
				replaceTxbNote(DEFI_TXBEXPLAIN + defi_count);
				replaceTxbNote(DEFI_TXBEXAM + defi_count);
			}
		};
		t.schedule(200);
		
		cbTypeCommon.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				checkTypeListEvent(cbTypeCommon.getValue(), Category.COMMON);
			}
		});
		cbType850Basic.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				checkTypeListEvent(cbType850Basic.getValue(), Category.BASIC850);
			}
		});
		cbTypeVoaEnglish.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				checkTypeListEvent(cbTypeVoaEnglish.getValue(), Category.VOAENGLISH);
			}
		});
		cbTypeIelts.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				checkTypeListEvent(cbTypeIelts.getValue(), Category.IELTS);
			}
		});
		cbTypeToefl.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				checkTypeListEvent(cbTypeToefl.getValue(), Category.TOEFL);
			}
		});
		cbTypeEconomic.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				checkTypeListEvent(cbTypeEconomic.getValue(), Category.ECONOMIC);
			}
		});
		cbTypeIt.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				checkTypeListEvent(cbTypeIt.getValue(), Category.IT);
			}
		});
		cbTypeScience.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				checkTypeListEvent(cbTypeScience.getValue(), Category.SCIENCE);
			}
		});
		cbTypeMedicine.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				checkTypeListEvent(cbTypeMedicine.getValue(), Category.MEDICINE);
			}
		});
		cbTypeOther.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				checkTypeListEvent(cbTypeOther.getValue(), Category.OTHER);
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
	}
	
	void checkTypeListEvent(boolean isCheck, String type) {
		if(isCheck) {
			packages.add(type);
			lsbType.addItem(type);
		}
		else {
			packages.remove(type);
			lsbType.removeItem(packages.indexOf(type)+ 1);
		}
	}
	
	public void getScrollTabContent(ScrollPanel scp) {
		tabContentScp = scp;
	}
	
	public static native void replaceTxbNote(String txbNoteId) /*-{
	 	var noteId = txbNoteId;
	  	var editor = $wnd.CKEDITOR.replace( noteId, {
	  		width: '405px',
	  		height: '28px',
	  		contentsCss : 'body {overflow:hidden;}',
	  		autoGrow_minHeight: 10,
	  		toolbarStartupExpanded : false,
	  	});
	  	
	  	editor.on("instanceReady",function() {
  			$wnd.document.getElementById(editor.id+'_top').style.display = "none";
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
	
	public static native void cleanCustomEditor(String editorId) /*-{
		var eid = editorId;
		var editor = $wnd.document.getElementById("cke_"+ eid);
		if(editor != null) {
			$wnd.CKEDITOR.instances[eid].setData("");
		}
	}-*/;
	
//	void addDefiPanel() {
//		final HTML html = new HTML();
//		html.setHTML("<br/> <div style='border-top: 1px solid silver; margin-bottom: 25px; margin-top: 10px; width: 575px;'></div>");
//		defiTable.add(html);
//		final HTMLPanel htmlp = new HTMLPanel("");
//		//---
//		HorizontalPanel hor1 = new HorizontalPanel();
//		hor1.setStyleName("VocaEditorTool_Obj3");
//		Label lb1 = new Label("Phân loại");
//		lb1.setStyleName("VocaEditorTool_Obj2");
//		ListBox lsbType = new ListBox();
//		lsbType.setSize("407px", "30px");
//		lsbType.setStyleName("VocaEditorTool_Obj4");
//		lsbType.addItem(Category.COMMON);
//		lsbType.addItem(Category.ECONOMIC);
//		lsbType.addItem(Category.IT);
//		lsbType.addItem(Category.MEDICINE);
//		lsbType.addItem(Category.SCIENCE);
//		lsbType.addItem(Category.BASIC850);
//		lsbType.addItem(Category.VOAENGLISH);
//		lsbType.addItem(Category.IELTS);
//		lsbType.addItem(Category.TOEFL);
//		lsbType.addItem(Category.OTHER);
//		hor1.add(lb1);
//		hor1.add(lsbType);
//		htmlp.add(hor1);
//		//---
//		HorizontalPanel hor2 = new HorizontalPanel();
//		hor2.setStyleName("VocaEditorTool_Obj3");
//		Label lb2 = new Label("Ý nghĩa");
//		lb2.setStyleName("VocaEditorTool_Obj2");
//		TextArea txbMeaning = new TextArea();
//		txbMeaning.setStyleName("VocaEditorTool_Obj4");
//		txbMeaning.getElement().setAttribute("id", DEFI_TXBMEANING + defi_count);
//		hor2.add(lb2);
//		hor2.add(txbMeaning);
//		htmlp.add(hor2);
//		//---
//		HorizontalPanel hor3 = new HorizontalPanel();
//		hor3.setStyleName("VocaEditorTool_Obj3");
//		Label lb3 = new Label("Giải thích");
//		lb3.setStyleName("VocaEditorTool_Obj2");
//		TextArea txbExplain = new TextArea();
//		txbExplain.setStyleName("VocaEditorTool_Obj4");
//		txbExplain.getElement().setAttribute("id", DEFI_TXBEXPLAIN + defi_count);
//		hor3.add(lb3);
//		hor3.add(txbExplain);
//		htmlp.add(hor3);
//		//---
//		HorizontalPanel hor4 = new HorizontalPanel();
//		hor4.setStyleName("VocaEditorTool_Obj3");
//		Label lb4 = new Label("Ví dụ");
//		lb4.setStyleName("VocaEditorTool_Obj2");
//		TextArea txbExam = new TextArea();
//		txbExam.setStyleName("VocaEditorTool_Obj4");
//		txbExam.getElement().setAttribute("id", DEFI_TXBEXAM + defi_count);
//		hor4.add(lb4);
//		hor4.add(txbExam);
//		htmlp.add(hor4);
//		//---
//		HorizontalPanel hor5 = new HorizontalPanel();
//		hor5.setStyleName("VocaEditorTool_Obj3");
//		Label lb5 = new Label("Hình ảnh minh họa");
//		lb5.setStyleName("VocaEditorTool_Obj2");
//		Anchor ac = new Anchor();
//		ac.setTitle("Upload");
//		ac.setStyleName("VocaEditorTool_Obj6");
//		ac.getElement().setInnerHTML("<i class='fa fa-upload fa-lg' style='color:silver'></i>");
//		hor5.add(lb5);
//		hor5.add(ac);
//		htmlp.add(hor5);
//		//---
//		Anchor deleteDefi = new Anchor("Xóa giải nghĩa này");
//		deleteDefi.setStyleName("VocaEditorTool_Obj5 VocaEditorTool_Obj6");
//		deleteDefi.getElement().setAttribute("style", "color:red");
//		htmlp.add(deleteDefi);
//		//---
//		defiTable.add(htmlp);
//		
//		//Add Defi Container
//		final DefiContainer dc = new DefiContainer();
//		dc.type = lsbType;
//		dc.txbMeaning_id = DEFI_TXBMEANING + defi_count;
//		dc.txbExplain_id = DEFI_TXBEXPLAIN + defi_count;
//		dc.txbExam_id = DEFI_TXBEXAM + defi_count;
//		list_defi.add(dc);
//		
//		//---
//		deleteDefi.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				defiTable.remove(htmlp);
//				defiTable.remove(html);
//				list_defi.remove(dc);
//			}
//		});
//		
//		Timer t = new Timer() {
//			@Override
//			public void run() {
//				replaceTxbNote(DEFI_TXBMEANING + defi_count);
//				replaceTxbNote(DEFI_TXBEXPLAIN + defi_count);
//				replaceTxbNote(DEFI_TXBEXAM + defi_count);
//			}
//		};
//		t.schedule(200);
//		Timer t2 = new Timer() {
//			@Override
//			public void run() {
//				tabContentScp.scrollToBottom();
//			}
//		};
//		t2.schedule(250);
//	}
	
	String getJsonData() {
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
	
	String getPackages() {
		String spackages = "";
		for(String pac: packages) {
			spackages = spackages + "," + pac;
		}
		spackages = spackages + ",";
		return spackages;
	}
	
	void saveNewVoca() {
		if(verifyField()) {
			final NoticeBox loadingNotice = new NoticeBox("Đang tải lên... ");
			final Voca v = new Voca();
			v.setQ(txbVocaDefi.getText());
			v.setLevel(lsbLevel.getValue(lsbLevel.getSelectedIndex()));
			v.setA(getJsonData());
			v.setPackages(getPackages());
			LazzyBee.data_service.insertVoca(v, new AsyncCallback<Voca>() {
				@Override
				public void onSuccess(Voca result) {
					formClean();
					loadingNotice.hide();
					tabContentScp.scrollToTop();
					new NoticeBox("- "+ v.getQ()+ " - đã được thêm vào từ điển").setAutoHide();
				}
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
				}
			});
		}
	}
	
	void formClean() {
		txbVocaDefi.setText("");
		txbVocaDefi.getElement().setAttribute("style", "");
		txbPronoun.setText("");
		txbPronoun.getElement().setAttribute("style", "");
		lsbType.setSelectedIndex(0);
		cleanCustomEditor(DEFI_TXBMEANING + "1");
		cleanCustomEditor(DEFI_TXBEXPLAIN + "1");
		cleanCustomEditor(DEFI_TXBEXAM + "1");
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
		lsbType.clear();
		defiTable.clear();
		list_defi.clear();
		
		lsbType.addItem("- Chọn phân loại -");
		htmlType.add(lsbType);
		defi_count = 1;
		DefiContainer dc = new DefiContainer();
		dc.txbMeaning_id = DEFI_TXBMEANING + defi_count;
		dc.txbExplain_id = DEFI_TXBEXPLAIN + defi_count;
		dc.txbExam_id = DEFI_TXBEXAM + defi_count;
		list_defi.add(dc);
	}
	
	@UiHandler("btnSave")
	void onBtnSaveClick(ClickEvent e) {
		saveNewVoca();
	}
	
//	@UiHandler("btnAddDefi")
//	void onBtnAddDefiClick(ClickEvent e) {
//		defi_count++;
//		addDefiPanel();
//	}
	
	@UiHandler("btnGoTop")
	void onBtnGoTopClick(ClickEvent e) {
		tabContentScp.scrollToTop();
	}
	
	@UiHandler("btnSaveB")
	void onBtnSaveBClick(ClickEvent e) {
		saveNewVoca();
	}
	
	@UiHandler("btnClear")
	void onBtnClearClick(ClickEvent e) {
		formClean();
	}
	
	boolean verifyField() {
		boolean verify = true;
		if(txbVocaDefi.getText().equals("")) {
			verify = false;
			txbVocaDefi.getElement().setAttribute("style", "border: 1px solid red;");
		}
		if(txbPronoun.getText().equals("")) {
			verify = false;
			txbPronoun.getElement().setAttribute("style", "border: 1px solid red;");
		}
		return verify;
	}

}
