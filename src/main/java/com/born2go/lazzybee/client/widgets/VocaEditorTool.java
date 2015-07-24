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
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
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
	
	class DefiContainer {
		List<String> types = new ArrayList<String>();
		String txbMeaning_id;
		String txbExplain_id;
		String txbExam_id;
	}
	
	private Long voca_gid = null;
	private List<String> packages = new ArrayList<String>();
	private List<DefiContainer> list_defi = new ArrayList<DefiContainer>();
	private List<DefiContainer> list_defitranforms = new ArrayList<DefiContainer>();
	private List<ListBox> listLbType = new ArrayList<ListBox>();
	
	ScrollPanel tabContentScp;

	public void getScrollTabContent(ScrollPanel scp) {
		tabContentScp = scp;
	}
	
	public void replaceEditor() {
		Timer t = new Timer() {
			@Override
			public void run() {
				replaceTxbNote(DEFI_TXBMEANING + 1);
				replaceTxbNote(DEFI_TXBEXPLAIN + 1);
				replaceTxbNote(DEFI_TXBEXAM + 1);
			}
		};
		t.schedule(100);
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
	
	public void setVoca(Voca voca) {
		voca_gid = voca.getGid();
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
					checkTypeListEvent(true, Category.COMMON);
				}
				if(packages[i].equals(Category.BASIC850)) {
					cbType850Basic.setValue(true);
					checkTypeListEvent(true, Category.BASIC850);
				}
				if(packages[i].equals(Category.VOAENGLISH)) {
					cbTypeVoaEnglish.setValue(true);
					checkTypeListEvent(true, Category.VOAENGLISH);
				}
				if(packages[i].equals(Category.IELTS)) {
					cbTypeIelts.setValue(true);
					checkTypeListEvent(true, Category.IELTS);
				}
				if(packages[i].equals(Category.TOEFL)) {
					cbTypeToefl.setValue(true);
					checkTypeListEvent(true, Category.TOEFL);
				}
				if(packages[i].equals(Category.ECONOMIC)) {
					cbTypeEconomic.setValue(true);
					checkTypeListEvent(true, Category.ECONOMIC);
				}
				if(packages[i].equals(Category.IT)) {
					cbTypeIt.setValue(true);
					checkTypeListEvent(true, Category.IT);
				}
				if(packages[i].equals(Category.SCIENCE)) {
					cbTypeScience.setValue(true);
					checkTypeListEvent(true, Category.SCIENCE);
				}
				if(packages[i].equals(Category.MEDICINE)) {
					cbTypeMedicine.setValue(true);
					checkTypeListEvent(true, Category.MEDICINE);
				}
				if(packages[i].equals(Category.OTHER)) {
					cbTypeOther.setValue(true);
					checkTypeListEvent(true, Category.OTHER);
				}
			}
		//-----
		JSONValue a = JSONParser.parseStrict(voca.getA());
		txbPronoun.setText(a.isObject().get("pronoun").toString().replaceAll("\"", ""));
		JSONValue pac = a.isObject().get("packages");
		if(packages.length > 1) {
			for(int i = 1; i < packages.length; i++) {
				if(i == 1) {
					DefiContainer dc = new DefiContainer();
					dc.types.add(packages[i]);
					JSONValue defi = pac.isObject().get(packages[i]);
					dc.txbMeaning_id = defi.isObject().get("meaning").toString();
					dc.txbExplain_id = defi.isObject().get("explain").toString();
					dc.txbExam_id = defi.isObject().get("example").toString();
					list_defitranforms.add(dc);
				}
				else {
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
		Timer t = new Timer() {
			@Override
			public void run() {
				setDataCustomEditor(dc.txbMeaning_id, firstDefi.txbMeaning_id.replaceAll("\"", ""));
				setDataCustomEditor(dc.txbExplain_id, firstDefi.txbExplain_id.replaceAll("\"", ""));
				setDataCustomEditor(dc.txbExam_id, firstDefi.txbExam_id.replaceAll("\"", ""));
			}
		};
		t.schedule(500);
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
	
	private void checkTypeListEvent(boolean isCheck, String type) {
		if(isCheck) {
			lsbType.addItem(type);
			for(ListBox lb: listLbType)
				lb.addItem(type);
			packages.add(type);
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
	
	public static native void setDataCustomEditor(String editorId, String data) /*-{
		var eid = editorId;
		var d = data;
		var editor = $wnd.document.getElementById("cke_"+ eid);
		if(editor != null) {
			$wnd.CKEDITOR.instances[eid].setData(d);
		}
	}-*/;
	
	public static native void cleanCustomEditor(String editorId) /*-{
		var eid = editorId;
		var editor = $wnd.document.getElementById("cke_"+ eid);
		if(editor != null) {
			$wnd.CKEDITOR.instances[eid].setData("");
		}
	}-*/;
	
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
		Label lb2 = new Label("Ý nghĩa");
		lb2.setStyleName("VocaEditorTool_Obj2");
		TextArea txbMeaning = new TextArea();
		txbMeaning.setStyleName("VocaEditorTool_Obj4");
		txbMeaning.getElement().setAttribute("id", DEFI_TXBMEANING + defi_count);
		hor2.add(lb2);
		hor2.add(txbMeaning);
		htmlp.add(hor2);
		//---
		HorizontalPanel hor3 = new HorizontalPanel();
		hor3.setStyleName("VocaEditorTool_Obj3");
		Label lb3 = new Label("Giải thích");
		lb3.setStyleName("VocaEditorTool_Obj2");
		TextArea txbExplain = new TextArea();
		txbExplain.setStyleName("VocaEditorTool_Obj4");
		txbExplain.getElement().setAttribute("id", DEFI_TXBEXPLAIN + defi_count);
		hor3.add(lb3);
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
				replaceTxbNote(dc.txbMeaning_id);
				replaceTxbNote(dc.txbExplain_id);
				replaceTxbNote(dc.txbExam_id);
			}
		};
		t.schedule(100);
		Timer t2 = new Timer() {
			@Override
			public void run() {
				tabContentScp.scrollToBottom();
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
		Label lb2 = new Label("Ý nghĩa");
		lb2.setStyleName("VocaEditorTool_Obj2");
		TextArea txbMeaning = new TextArea();
		txbMeaning.setStyleName("VocaEditorTool_Obj4");
		txbMeaning.getElement().setAttribute("id", DEFI_TXBMEANING + defi_count);
		hor2.add(lb2);
		hor2.add(txbMeaning);
		htmlp.add(hor2);
		//---
		HorizontalPanel hor3 = new HorizontalPanel();
		hor3.setStyleName("VocaEditorTool_Obj3");
		Label lb3 = new Label("Giải thích");
		lb3.setStyleName("VocaEditorTool_Obj2");
		TextArea txbExplain = new TextArea();
		txbExplain.setStyleName("VocaEditorTool_Obj4");
		txbExplain.getElement().setAttribute("id", DEFI_TXBEXPLAIN + defi_count);
		hor3.add(lb3);
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
				replaceTxbNote(dc.txbMeaning_id);
				replaceTxbNote(dc.txbExplain_id);
				replaceTxbNote(dc.txbExam_id);
			}
		};
		t.schedule(100);
		Timer t2 = new Timer() {
			@Override
			public void run() {
				setDataCustomEditor(dc.txbMeaning_id, defiTranforms.txbMeaning_id.replaceAll("\"", ""));
				setDataCustomEditor(dc.txbExplain_id, defiTranforms.txbExplain_id.replaceAll("\"", ""));
				setDataCustomEditor(dc.txbExam_id, defiTranforms.txbExam_id.replaceAll("\"", ""));
			}
		};
		t2.schedule(500);
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
					loadingNotice.hide();
					new NoticeBox("! Đã có lỗi xảy ra khi tải lên").setAutoHide();
				}
			});
		}
	}
	
	private void updateVoca() {
		if(verifyField()) {
			final NoticeBox loadingNotice = new NoticeBox("Đang tải lên... ");
			final Voca v = new Voca();
			v.setGid(voca_gid);
			v.setQ(txbVocaDefi.getText());
			v.setLevel(lsbLevel.getValue(lsbLevel.getSelectedIndex()));
			v.setA(getJsonData());
			v.setPackages(getPackages());
			LazzyBee.data_service.updateVoca(v, new AsyncCallback<Voca>() {
				@Override
				public void onSuccess(Voca result) {
					formClean();
					loadingNotice.hide();
					tabContentScp.scrollToTop();
					new NoticeBox("- "+ v.getQ()+ " - đã được cập nhật").setAutoHide();
				}
				
				@Override
				public void onFailure(Throwable caught) {
					loadingNotice.hide();
					new NoticeBox("! Đã có lỗi xảy ra khi tải lên").setAutoHide();
				}
			});
		}
	}
	
	private void formClean() {
		String newURL = Window.Location.createUrlBuilder().setHash("vocabulary").buildString();
		Window.Location.replace(newURL);
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
		packages.clear();
		lsbType.clear();
		defiTable.clear();
		list_defi.clear();
		listLbType.clear();
		list_defitranforms.clear();
		voca_gid = null;
		//-----startup over-----
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
		if(voca_gid == null)
			saveNewVoca();
		else
			updateVoca();
	}
	
	@UiHandler("btnSaveB")
	void onBtnSaveBClick(ClickEvent e) {
		if(voca_gid == null)
			saveNewVoca();
		else
			updateVoca();
	}

	@UiHandler("btnAddDefi")
	void onBtnAddDefiClick(ClickEvent e) {
		defi_count++;
		addDefiPanel();
	}
	
	@UiHandler("btnGoTop")
	void onBtnGoTopClick(ClickEvent e) {
		tabContentScp.scrollToTop();
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
			tabContentScp.scrollToTop();
		}
		if(txbPronoun.getText().equals("")) {
			verify = false;
			txbPronoun.getElement().setAttribute("style", "border: 1px solid red;");
			tabContentScp.scrollToTop();
		}
		return verify;
	}

}
