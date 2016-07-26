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
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
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
	@UiField
	HTMLPanel topToolbar;

	private GroupVoca group = null;
	private GroupEditorTool thiz = this;

	public GroupEditorTool() {
		initWidget(uiBinder.createAndBindUi(this));
		if (DOM.getElementById("right_panel") != null)
			DOM.getElementById("right_panel").setAttribute("style", "display:");
	}

	public interface EditorListener {
		void onUpdateGroup(GroupVoca v);

		void onDelete(GroupVoca v);

		void onClose();
	}

	private EditorListener listener;

	public void setListener(EditorListener listener) {
		this.listener = listener;
	}

	@UiField
	Anchor btnDelete;

	/**
	 * this method set VocaGroup
	 * 
	 * @param g
	 *            :VocaGroup will update
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
							LazzyBee.noticeBox.setRichNotice(
									"Danh sách từ đã được cập nhật !", "/group/"
											+ result.getId(), "/editor/#group/"
											+ result.getId());
							if (!isShowGroup)
								formClean();
							else if (listener != null)
								listener.onUpdateGroup(result);

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
	void onbtnDelete(ClickEvent e) {
		deleteGroupVoca();
	}

	void deleteGroupVoca() {
		if (Window.confirm("Bạn muốn xóa danh sách từ ? ")) {
			if (group != null) {
				LazzyBee.data_service.removeGroup(group.getId(),
						new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								LazzyBee.noticeBox
										.setNotice("! Đã có lỗi khi tải lên");
								LazzyBee.noticeBox.setAutoHide();

							}

							@Override
							public void onSuccess(Void result) {
								if (!isShowGroup)
									formClean();
								else if (listener != null)
									listener.onDelete(group);
								LazzyBee.noticeBox
										.setNotice("Danh sách từ đã bị xóa");
								LazzyBee.noticeBox.setAutoHide();

							}
						});
			}
		}
	}

//	@UiHandler("btnGoTop")
//	void onBtnGoTopClick(ClickEvent e) {
//		DOM.getElementById("content").setScrollTop(0);
//	}

	@UiField
	Anchor btnClose;

	@UiHandler("btnClose")
	void onBtnClose(ClickEvent e) {
		if (listener != null)
			listener.onClose();
	}

	@UiField
	Anchor btnDeleteD;

	@UiHandler("btnDeleteD")
	void onBtnDeleteD(ClickEvent e) {
		deleteGroupVoca();
	}

//	@UiField
//	Anchor btnGoTop;

	boolean isShowGroup = false;

	public void onShowGroup() {
		isShowGroup = true;
		Label header = new Label("---------- Chi tiết nhóm từ ----------");
		header.getElement()
				.setAttribute("style",
						"color: #0066cc; text-align: center; font-size: 20px; font-weight: bold;");
		topToolbar.clear();
		topToolbar.add(header);
		// topToolbar.getElement().setAttribute("style", "margin-top: 25px");
		topToolbar.setVisible(true);
	//	btnGoTop.setVisible(false);
		btnClose.setVisible(true);
		btnDeleteD.setVisible(true);
	}
}
