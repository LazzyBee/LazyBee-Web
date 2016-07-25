package com.born2go.lazzybee.client.mainpage;

import java.util.List;

import com.born2go.lazzybee.client.LazzyBee;
import com.born2go.lazzybee.client.subpage.DownloadView;
import com.born2go.lazzybee.client.subpage.ListVocaView;
import com.born2go.lazzybee.client.subpage.TestTool;
import com.born2go.lazzybee.client.subpage.GroupVocaList;
import com.born2go.lazzybee.client.subpage.VocaPreviewTool;
import com.born2go.lazzybee.client.subpage.VocaView;
import com.born2go.lazzybee.client.widgets.LoginControl;
import com.born2go.lazzybee.gdatabase.shared.Blog;
import com.born2go.lazzybee.gdatabase.shared.Picture;
import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class DictionaryTool extends Composite {

	private static EditorToolUiBinder uiBinder = GWT
			.create(EditorToolUiBinder.class);

	interface EditorToolUiBinder extends UiBinder<Widget, DictionaryTool> {
	}

	@UiField
	HTMLPanel tabPanel;
	@UiField
	Label trademarkLb;

	@UiField
	Anchor defaultSite;
	@UiField
	Anchor testSite;
	@UiField
	Anchor dictionarySite;
	@UiField
	Anchor previewSite;

	TextBox searchBox = new TextBox();

	public DictionaryTool() {
		initWidget(uiBinder.createAndBindUi(this));

		Window.addResizeHandler(new ResizeHandler() {
			Timer resizeTimer = new Timer() {
				@Override
				public void run() {
					tabPanel.getElement().setAttribute("style",
							"height:" + (Window.getClientHeight() - 40) + "px");
					DOM.getElementById("content").setAttribute("style",
							"height:" + (Window.getClientHeight() - 40) + "px");
				}
			};

			@Override
			public void onResize(ResizeEvent event) {
				resizeTimer.cancel();
				resizeTimer.schedule(250);
			}
		});

		tabPanel.getElement().setAttribute("style",
				"height:" + (Window.getClientHeight() - 40) + "px");
		trademarkLb.getElement().setAttribute("style",
				"position: absolute; bottom: 20px");

		addSearchTool();
		historyTokenHandler();
	}

	void removeTabStyle() {
		defaultSite.removeStyleName("DictionaryTool_Obj5");
		testSite.removeStyleName("DictionaryTool_Obj5");
		dictionarySite.removeStyleName("DictionaryTool_Obj5");
	}

	DownloadView download = new DownloadView();

	void historyTokenHandler() {
		String path = Window.Location.getPath();
		if (path.contains("/")) {
			String paths[] = path.split("/");
			path = paths[1];
		}
		if (path.contains("vdict")) {
			HTMLPanel htmlPanel = new HTMLPanel("");
			Label info = new Label(
					"Phương pháp học thông qua flashcard chỉ có trên phiên bản mobile. Bạn hãy cài đặt App mobile để học từ vựng tốt hơn.");
			info.getElement()
					.setAttribute(
							"style",
							"margin-bottom: 20px; margin-top: 20px; padding: 10px; background-color: lemonchiffon; line-height: 1.5;");
			htmlPanel.add(info);
			RootPanel.get("wt_dictionary_content").add(htmlPanel);

			HTMLPanel wt_dictionary_blog = new HTMLPanel("");
			loadBlog(wt_dictionary_blog, true);
			RootPanel.get("wt_dictionary_blog").add(wt_dictionary_blog);

			defaultSite.addStyleName("DictionaryTool_Obj5");
			// -----
			if (!History.getToken().isEmpty()) {
				loadVocaToken(History.getToken());
			} else {
				String token = Window.Location.getPath().split("/")[2];
				if (token != null && !token.isEmpty()) {
					loadVocaToken(token);
				}
			}
		} else if (path.contains("test")) {
			testSite.addStyleName("DictionaryTool_Obj5");
			RootPanel.get("wt_dictionary_content").add(new TestTool());
		} else if (path.contains("dictionary")) {
			RootPanel.get("wt_dictionary_content").add(new ListVocaView());
		} else if (path.contains("preview")) {
			// previewSite.addStyleName("DictionaryTool_Obj5");
			RootPanel.get("wt_dictionary_content").add(new VocaPreviewTool());
		} else if (path.contains("blog")) {
			if (RootPanel.get("wt_bloglist") != null) {
				HTMLPanel wt_dictionary_blog = new HTMLPanel("");
				loadBlog(wt_dictionary_blog, false);
				RootPanel.get("wt_bloglist").add(wt_dictionary_blog);
			}
		}

		else if (path.contains("group")) {
			if (RootPanel.get("wt_grouplist") != null) {
				 GroupVocaList group = new GroupVocaList();
				RootPanel.get("wt_grouplist").add(group);
			}
		} else if (path.contains("downloadVoca")) {
			RootPanel.get("wt_dictionary_content").add(download);
		}

		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {

				loadVocaToken(event.getValue());
			}
		});
	}

	void loadBlog(final HTMLPanel wt_dictionary_blog, boolean isLimited) {
		if (!isLimited) {
			LazzyBee.noticeBox.setLoading();
		}

		LazzyBee.data_service.getListBlog(isLimited,
				new AsyncCallback<List<Blog>>() {
					@Override
					public void onSuccess(List<Blog> result) {
						HTMLPanel blogPanel = new HTMLPanel("");
						blogPanel
								.getElement()
								.setAttribute("style",
										"/* border-top: 1px solid #E6E9EB; */ overflow: hidden; margin-bottom: 30px;");
						wt_dictionary_blog.add(blogPanel);
						// -----
						HTMLPanel faq = new HTMLPanel(
								"------------------------------ FAQ ------------------------------");
						faq.getElement()
								.setAttribute(
										"style",
										"text-align:center; margin-top:15px; margin-bottom:20px; color: #009688; font-weight: bold;");
						blogPanel.add(faq);
						// -----
						for (final Blog blog : result) {
							HTMLPanel blogp = new HTMLPanel("");
							blogp.setStyleName("DictionaryTool_Obj9");
							final Image avatar = new Image();
							avatar.setStyleName("DictionaryTool_Obj8");
							LazzyBee.data_service.findPicture(blog.getAvatar(),
									new AsyncCallback<Picture>() {
										@Override
										public void onSuccess(Picture result) {
											if (result != null)
												avatar.setUrl(result
														.getServeUrl());
											else
												avatar.setUrl("/resources/1435838158_Mushroom - Bee.png");
										}

										@Override
										public void onFailure(Throwable caught) {
										}
									});
							String t = blog.getShowTitle();
							// if(t.length() > 45) {
							// t = t.substring(0, 44);
							// t = t + "...";
							// }
							Anchor title = new Anchor(t);
							title.setHref("/blog/" + blog.getTitle());
							title.setStyleName("DictionaryTool_Obj7");
							blogp.add(avatar);
							blogp.add(title);
							blogPanel.add(blogp);
							avatar.addClickHandler(new ClickHandler() {
								@Override
								public void onClick(ClickEvent event) {
									Window.Location.assign("/blog/"
											+ blog.getTitle());
								}
							});
						}
						LazzyBee.noticeBox.hide();
					}

					@Override
					public void onFailure(Throwable caught) {
						LazzyBee.noticeBox.setNotice("Đã có lỗi xảy ra!");
						LazzyBee.noticeBox.setAutoHide();
					}
				});
	}

	void loadVocaToken(final String history_token) {
		RootPanel.get("wt_dictionary_content").clear();
		// final String history_token = History.getToken();
		searchBox.setText(history_token);
		LazzyBee.noticeBox.setNotice("Đang tải...");
		LazzyBee.data_service.findVoca(history_token,
				new AsyncCallback<Voca>() {
					@Override
					public void onSuccess(Voca result) {
						if (result == null) {
							LazzyBee.noticeBox.setNotice("Không tìm thấy từ - "
									+ history_token);
							LazzyBee.noticeBox.setAutoHide();
							HTMLPanel htmlPanel = new HTMLPanel("");
							htmlPanel
									.getElement()
									.setAttribute(
											"style",
											"margin-top: 20px; padding: 10px; background-color: lemonchiffon; line-height: 1.5;");
							Label lbNotFound = new Label("Không tìm thấy từ - "
									+ history_token);
							htmlPanel.add(lbNotFound);
							RootPanel.get("wt_dictionary_content").add(
									htmlPanel);
						} else {
							LazzyBee.noticeBox.hide();
							RootPanel.get("wt_dictionary_content").add(
									new VocaView().setVoca(result));
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						LazzyBee.noticeBox
								.setNotice("! Đã có lỗi xảy ra trong quá trình tải");
						LazzyBee.noticeBox.setAutoHide();
					}
				});
	}

	void addSearchTool() {
		HTMLPanel hor = new HTMLPanel("");
		hor.getElement().setAttribute("style",
				"width: 100%; height: 40px; display: inline-flex;");
		searchBox.getElement().setAttribute("style",
				"float: left; width: 100%; height: 70%; padding-left: 10px;");
		searchBox.getElement().setAttribute("placeholder", "Tìm từ vựng");
		Anchor searchButton = new Anchor();
		searchButton
				.getElement()
				.setInnerHTML(
						"<i class='fa fa-search fa-lg' style='margin-top: 12px; margin-left: 20px;'></i>");
		searchButton
				.getElement()
				.setAttribute(
						"style",
						"float: left; width: 60px; height: 100%; background: #009688; color: white; cursor: pointer;");
		hor.add(searchBox);
		hor.add(searchButton);
		if (RootPanel.get("wt_search_tool") != null)
			RootPanel.get("wt_search_tool").add(hor);
		// -----
		searchBox.addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event_) {
				boolean enterPressed = KeyCodes.KEY_ENTER == event_
						.getNativeEvent().getKeyCode();
				if (enterPressed) {
					if (!searchBox.getText().equals(""))
						Window.Location.assign("/vdict/#" + searchBox.getText());
				}
			}
		});
		searchButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (!searchBox.getText().equals(""))
					Window.Location.assign("/vdict/#" + searchBox.getText());
			}
		});
	}

	@UiHandler("defaultSite")
	void onDefaultSiteClick(ClickEvent e) {
		String newURL = "/vdict/";
		Window.Location.replace(newURL);
	}

	@UiHandler("testSite")
	void onTestSiteClick(ClickEvent e) {
		String newURL = "/test/";
		Window.Location.replace(newURL);
	}

	// @UiHandler("dictionarySite")
	// void onDictionarySiteClick(ClickEvent e) {
	// Window.Location.assign("/library/#dictionary");
	// Window.Location.reload();
	// }

	// @UiHandler("previewSite")
	// void onPreviewSiteClick(ClickEvent e) {
	// Window.Location.assign("/library/#preview");
	// Window.Location.reload();
	// }

}
