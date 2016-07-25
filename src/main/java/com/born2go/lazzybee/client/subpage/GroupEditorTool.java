package com.born2go.lazzybee.client.subpage;

import com.born2go.lazzybee.client.LazzyBee;
import com.born2go.lazzybee.gdatabase.shared.GroupVoca;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class GroupEditorTool extends Composite {

	private static GroupEditorToolUiBinder uiBinder = GWT
			.create(GroupEditorToolUiBinder.class);

	interface GroupEditorToolUiBinder extends UiBinder<Widget, GroupEditorTool> {
	}

	@UiField
	RichTextArea txbListVoca;
	@UiField
	TextBox txbDescription;

	private GroupVoca group = null;
	private GroupEditorTool thiz = this;

	public GroupEditorTool() {
		initWidget(uiBinder.createAndBindUi(this));
		DOM.getElementById("right_panel")
				.setAttribute("style", "display: none");
		DOM.getElementById("wt_editor").setAttribute("style",
				"padding: 30px; width: 100%; float: left;");

	}

	 

	@UiField
	Anchor btnDelete;
	/**
	 * this method set VocaGroup
	 * @param g:VocaGroup will update
	 */
	public void setGroupVoca(GroupVoca g) {
		this.group = g;
		txbDescription.setText(group.getDescription());
		txbListVoca.setHTML(group.getListVoca());
		btnDelete.setVisible(true);
	}

	 
	/**
	 * this method clean form inphut data
	 */
	private void formClean() {
		String newURL = Window.Location.createUrlBuilder().setHash("group")
				.buildString();
		Window.Location.replace(newURL);
		group = null;
		txbDescription.setText("");
		 txbListVoca.setText("");

	}
 
	/**
	 * this method save a new GroupVoca
	 */
	private void saveNewGroup() {
		GroupVoca g = new GroupVoca();
		g.setCreator(LazzyBee.userName);
		g.setDescription(txbDescription.getValue());
		g.setListVoca(txbListVoca.getHTML());
		LazzyBee.data_service.insertGroupVoca(g,
				new AsyncCallback<GroupVoca>() {

					@Override
					public void onSuccess(GroupVoca result) {
						if (result != null) {
							formClean();
							DOM.getElementById("content").setScrollTop(0);
							LazzyBee.noticeBox.setRichNotice(
									"Danh sách từ đã được lưu!", "/group/"
											+ result.getId(), "/editor/#group/"
											+ result.getId());
						} else {
							showError();
						}

					}

					@Override
					public void onFailure(Throwable caught) {
						showError();

					}
				});

	}
	
	/**
	 * this method will update a exist GroupVoca in db
	 */
	private void updateGroupVoca() {
		LazzyBee.noticeBox.setNotice("Đang tải lên... ");
		group.setCreator(LazzyBee.userName);
		group.setDescription(txbDescription.getValue());
		group.setListVoca(txbListVoca.getHTML());
		LazzyBee.data_service.updateGroupVoca(group,
				new AsyncCallback<GroupVoca>() {

					@Override
					public void onFailure(Throwable caught) {
						showError();

					}

					@Override
					public void onSuccess(GroupVoca result) {
						if (result != null) {
							formClean();
							DOM.getElementById("content").setScrollTop(0);
							LazzyBee.noticeBox.setRichNotice(
									"Danh sách từ đã được sửa thành công!",
									"/group/" + result.getId(),
									"/editor/#group/" + result.getId());
						} else {
							showError();
						}

					}
				});
	}

	 

	/*
	 * this method show error when user update to server
	 */
	private void showError() {
		LazzyBee.noticeBox.setNotice("! Đã có lỗi xảy ra khi tải lên");
		LazzyBee.noticeBox.setAutoHide();

	}

	@UiHandler("btnClear")
	void onBtnClearClick(ClickEvent e) {
		formClean();
	}

	@UiHandler("btnSave")
	void onBtnSaveClick(ClickEvent e) {
		if (group == null)
			saveNewGroup();
		else
			updateGroupVoca();

	}

	@UiHandler("btnSaveB")
	void onBtnSaveBClick(ClickEvent e) {
		if (group == null)
			saveNewGroup();
		else
			updateGroupVoca();
	}

	@UiHandler("btnDelete")
	void onbtnDelete(ClickEvent e){
		if(Window.confirm("Bạn muốn xóa danh sách từ ? ")) {
			if(group != null) {
				LazzyBee.data_service.removeGroup(group.getId(), new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						LazzyBee.noticeBox.setNotice("! Đã có lỗi khi tải lên");
						LazzyBee.noticeBox.setAutoHide();
						
					}

					@Override
					public void onSuccess(Void result) {
						 formClean();
						 LazzyBee.noticeBox.setNotice("Danh sách từ đã bị xóa");
						 LazzyBee.noticeBox.setAutoHide();
						
					}
				});
			}
		}
	}
	@UiHandler("btnGoTop")
	void onBtnGoTopClick(ClickEvent e) {
		DOM.getElementById("content").setScrollTop(0);
	}
}
