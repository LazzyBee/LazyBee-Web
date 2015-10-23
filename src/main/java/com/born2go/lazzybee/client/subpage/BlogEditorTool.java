package com.born2go.lazzybee.client.subpage;

import com.born2go.lazzybee.client.LazzyBee;
import com.born2go.lazzybee.gdatabase.shared.Blog;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class BlogEditorTool extends Composite {

	private static BlogEditorToolUiBinder uiBinder = GWT
			.create(BlogEditorToolUiBinder.class);

	interface BlogEditorToolUiBinder extends UiBinder<Widget, BlogEditorTool> {
	}
	
	@UiField TextArea blogContent;
	@UiField HTMLPanel container;
	@UiField Anchor pick_files;
	
	@UiField TextBox blogTitleBox;

	public BlogEditorTool() {
		initWidget(uiBinder.createAndBindUi(this));
		DOM.getElementById("right_panel").setAttribute("style", "display: none");
		DOM.getElementById("wt_editor").setAttribute("style", "padding: 30px; width: 100%; float: left;");
		
		blogContent.getElement().setAttribute("id", "blogContent");
		
		container.getElement().setAttribute("id", "container");
		pick_files.getElement().setAttribute("id", "pick_files");
	}
	
	public void replaceEditor() {
		Timer t = new Timer() {
			@Override
			public void run() {
				replaceTxbNote("blogContent");
			}
		};
		t.schedule(100);
	}
	
	public void handlerUploadEvent() {
		getPlupLoad(this);
	}
	
	public static native void replaceTxbNote(String txbNoteId) /*-{
	 	var noteId = txbNoteId;
	  	var editor = $wnd.CKEDITOR.replace( noteId, {
	  		height: '300px',
	  		contentsCss : 'body {overflow:hidden;maxwidth:655px;}',
	  		autoGrow_minHeight: 300,
	  		toolbarStartupExpanded : false,
	  		extraPlugins: 'autogrow,image,colorbutton',
	  		removeButtons: 'Subscript,Superscript,About',
	  	});
	  	
	  	editor.on("instanceReady",function() {
//			$wnd.document.getElementById(editor.id+'_top').style.display = "none";
//			vet.@com.born2go.lazzybee.client.subpage.VocaEditorTool::onCkEditorInstanceReady(Ljava/lang/String;)(noteId);
		});
	  	
	  	editor.on('focus', function(){	 
//        	$wnd.document.getElementById(editor.id+'_top').style.display = "block";
	    });
	   
	    editor.on('blur', function(){	       
//        	$wnd.document.getElementById(editor.id+'_top').style.display = "none";
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

	public static native void getPlupLoad(BlogEditorTool instance) /*-{
		var files_remaining;          
	  	$wnd.uploader = new $wnd.plupload.Uploader({
		    runtimes : 'flash',
		    container: $wnd.document.getElementById('container'),
		    browse_button : 'pick_files',
	    	drop_element: 'container',
		    url : '/',
		    use_query_string: false,
		   	dragdrop: true,
		    multipart : true,
		    multi_selection: false,
		    
		    //Enable resize
		    resize: {
				width: 1366,
				height: 768
			},
		    
		    //Enable params
		    multipart_params : {
				blogId: ''
			},
		     
		    //Enable filter files
		    filters : {
		        max_file_size : '5mb',
		        mime_types: [
		            {title : "Image files", extensions : "jpg,png"}  
		        ]
		    },
		 
		    // Flash settings
		    flash_swf_url : '/resources/plupload/Moxie.swf',
		    
		    // PreInit events, bound before any internal events
	        preinit : {
	            Init: function(up, info) {
	            },
	 
	            UploadFile: function(up, file) {
//	            	if(files_remaining > 1)
//			    		@com.born2go.client.widgets.PhotoUpload::updateUploaderUrl()();
	            }
	        },
		 
		    init: {
		        PostInit: function() {			 
//		            $wnd.document.getElementById('imageTable').innerHTML = '';
		        },
		        
		        QueueChanged: function(up) {
//	            	files_remaining = $wnd.uploader.files.length;
//	            	$wnd.document.getElementById('lbPhotosCount').innerHTML = files_remaining + " / Photos";
	        	},
		 
		        FilesAdded: function(up, files) {
		        	if($wnd.uploader.files.length > 1)
		        		$wnd.uploader.removeFile($wnd.uploader.files[0].id);
		        	
				   	$wnd.plupload.each(files, function(file) {
						var img = new $wnd.o.Image();
						                  
	                    img.onload = function() {
	                        // create a thumb placeholder
	                        var span = document.createElement('div');
	                        span.id = this.uid;	                          
	                        $wnd.document.getElementById('blogAvatar').innerHTML = '';                
	                        $wnd.document.getElementById('blogAvatar').appendChild(span);
	                        	                     	                  
	                        // embed the actual thumbnail
	                        var widthcrop = 348;
	                        var heightcrop = 218;
	                        this.embed(span.id, {	  
	                            width: widthcrop,
	                            height: heightcrop,
	                            crop: true
	                        });	                                               	                       	                  
	                    };
                     
                    	img.load(file.getSource());
				  	});
		        },		      	
		 
		        UploadProgress: function(up, file) {
//		        	var total_files =  $wnd.uploader.files.length;
//		        	var files_uploaded = total_files - files_remaining;
//		        	var total_percent = ((file.percent/100 * 1/total_files)*100) + ((files_uploaded * 1/total_files)*100);
//		            $wnd.document.getElementById('lbUploadProgress').innerHTML = '<span>' + Math.ceil(total_percent) + "%</span>";
		        },
		        
		        FileUploaded: function(up, file, info) {
//	                files_remaining--;
	            },
	            
	            UploadComplete: function(up, files) {          	
//	         		instance.@com.born2go.client.widgets.PhotoUpload::cancelUpload()();	
	            },
		 
		        Error: function(up, err) {
		            $wnd.document.getElementById('console').innerHTML += "\nError #" + err.code + ": " + err.message;
		        }
		    }
		});
		 
		$wnd.uploader.init();
	}-*/;
	
	public static native void updatePlupLoad(String par1) /*-{
		var blog_id = par1;
		if($wnd.uploader != null) {
			$wnd.uploader.settings.multipart_params.blogId = blog_id;
		}
	}-*/;
	
	public static native void startPlupLoad(String uploadUrl) /*-{
		var upload_url = uploadUrl;
		if($wnd.uploader != null) {
			$wnd.uploader.settings.url = upload_url;
			$wnd.uploader.start();
		}
	}-*/;
	
	private void formClean() {
		blogTitleBox.setText("");
		setDataCustomEditor("blogContent", "");
		DOM.getElementById("blogAvatar").setInnerHTML("");
	}
	
	private void saveBlog() {
		Blog blog = new Blog();
		blog.setTitle(blogTitleBox.getText().replaceAll(" ", "_"));
		blog.setContent(getDataCustomEditor("blogContent"));
		LazzyBee.noticeBox.setNotice("Đang tải lên... ");
		LazzyBee.data_service.insertBlog(blog, new AsyncCallback<Blog>() {
			@Override
			public void onSuccess(Blog result) {
				if(result != null) {
					uploadPhoto(result.getId());
					LazzyBee.noticeBox.setNotice("Bài viết đã được lưu!");
					LazzyBee.noticeBox.setAutoHide();
					formClean();
				}
				else {
					LazzyBee.noticeBox.setNotice("Tiêu đề bài viết không hợp lệ!");
				}
			}
			@Override
			public void onFailure(Throwable caught) {
				LazzyBee.noticeBox.setNotice("! Đã có lỗi xảy ra khi tải lên");
				LazzyBee.noticeBox.setAutoHide();
			}
		});
	}
	
	public void uploadPhoto(final Long blogId) {
		LazzyBee.data_service.getUploadUrl(new AsyncCallback<String>() {	
			@Override
			public void onSuccess(String result) {
				String blog_id = (blogId != null ? blogId.toString() : "");
				updatePlupLoad(blog_id);
				startPlupLoad(result);
			}
			@Override
			public void onFailure(Throwable caught) {
				LazzyBee.noticeBox.setNotice("! Đã có lỗi xảy ra khi tải hình ảnh lên");
				LazzyBee.noticeBox.setAutoHide();
			}
		});
	}
	
	@UiHandler("btnClear") 
	void onBtnClearClick(ClickEvent e) {
		formClean();
	}
	
	@UiHandler("btnSave") 
	void onBtnSaveClick(ClickEvent e) {
		saveBlog();
	}
	
	@UiHandler("btnSaveB") 
	void onBtnSaveBClick(ClickEvent e) {
		saveBlog();
	}
	
	@UiHandler("btnGoTop") 
	void onBtnGoTopClick(ClickEvent e) {
		DOM.getElementById("content").setScrollTop(0);
	}
}
