package com.born2go.lazzybee.gdatabase.client.rpc;

import java.util.List;

import com.born2go.lazzybee.gdatabase.shared.Blog;
import com.born2go.lazzybee.gdatabase.shared.Picture;
import com.born2go.lazzybee.gdatabase.shared.User;
import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.born2go.lazzybee.gdatabase.shared.VocaPreview;
import com.born2go.lazzybee.gdatabase.shared.nonentity.VocaList;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DataServiceAsync {

	void verifyVoca(String voca_q, AsyncCallback<Boolean> callback);
	void insertVoca(VocaPreview voca, AsyncCallback<Voca> callback);
	void findVoca(String voca_q, AsyncCallback<Voca> callback);
	void updateVoca(Voca voca, String userId, AsyncCallback<Voca> callback);
	void verifyUpdateVoca(Voca voca, String userId, AsyncCallback<Voca> callback);
//	void getTotalVoca(AsyncCallback<Integer> callback);
//	void getTotalPreviewVoca(AsyncCallback<Integer> callback);
	void getListVoca(String cursorStr, AsyncCallback<VocaList> callback);
	void getListPreviewVoca(String cursorStr, AsyncCallback<VocaList> callback);
	void removeVoca(Voca voca, String userId, AsyncCallback<Void> callback);
	
	void saveUser(User user, AsyncCallback<User> callback);

	void verifyBlog(String blogTitle, AsyncCallback<Boolean> callback);
	void insertBlog(Blog blog, String userId, AsyncCallback<Blog> callback);
	void updateBlog(Blog blog, String userId, AsyncCallback<Blog> callback);
	void findBlogById(Long blogId, AsyncCallback<Blog> callback);
	void findBlogByTitle(String blogTitle, AsyncCallback<Blog> callback);
	void getListBlog(AsyncCallback<List<Blog>> callback);
	
	void findPicture(Long pictureId, AsyncCallback<Picture> callback);
	
	void getUploadUrl(String userId, AsyncCallback<String> callback);
}
