package com.born2go.lazzybee.gdatabase.client.rpc;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.born2go.lazzybee.gdatabase.shared.Blog;
import com.born2go.lazzybee.gdatabase.shared.GroupVoca;
import com.born2go.lazzybee.gdatabase.shared.Picture;
import com.born2go.lazzybee.gdatabase.shared.User;
import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.born2go.lazzybee.gdatabase.shared.VocaPreview;
import com.born2go.lazzybee.gdatabase.shared.nonentity.VocaList;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DataServiceAsync {

	void verifyVoca(String voca_q, AsyncCallback<Boolean> callback);

	void insertVoca(VocaPreview voca, AsyncCallback<Voca> callback);

//	void findVoca(String voca_q, AsyncCallback<Voca> callback);

	void findVoca_Web(String voca_q, boolean saveLog,
			AsyncCallback<Voca> callbac);

	void updateVoca(Voca voca, String userId, AsyncCallback<Voca> callback);

	void verifyUpdateVoca(Voca voca, String userId, AsyncCallback<Voca> callback);

	// void getTotalVoca(AsyncCallback<Integer> callback);
	// void getTotalPreviewVoca(AsyncCallback<Integer> callback);
	void getListVoca(String cursorStr, AsyncCallback<VocaList> callback);

	void getListPreviewVoca(String cursorStr, AsyncCallback<VocaList> callback);

	void removeVoca(Voca voca, String userId, AsyncCallback<Void> callback);

	void saveUser(User user, AsyncCallback<User> callback);

	void verifyBlog(String blogTitle, AsyncCallback<Boolean> callback);

	void insertBlog(Blog blog, String userId, AsyncCallback<Blog> callback);

	void updateBlog(Blog blog, String userId, AsyncCallback<Blog> callback);

	void findBlogById(Long blogId, AsyncCallback<Blog> callback);

	void findBlogByTitle(String blogTitle, AsyncCallback<Blog> callback);

	void getListBlog(boolean isLimited, AsyncCallback<List<Blog>> callback);

	void findPicture(Long pictureId, AsyncCallback<Picture> callback);

	void getUploadUrl(String userId, AsyncCallback<String> callback);

	void getTestVocaStep_One(
			AsyncCallback<LinkedHashMap<String, String>> callback);

	void getTestVocaStep_Two(LinkedHashMap<String, String> hashMap,
			AsyncCallback<LinkedHashMap<String, String>> callback);

	void getTestVocaStep_Three(LinkedHashMap<String, String> hashMap,
			String cookie, String user_id, AsyncCallback<String> callback);

	// void getTestVocaStep_Four(HashMap<String, String> hmapInput, String
	// cookie,String user_id, AsyncCallback<String> callback);
	void insertGroupVoca(GroupVoca g, AsyncCallback<GroupVoca> callback);

	void findGroupVoca(long id, AsyncCallback<GroupVoca> callback);

	void updateGroupVoca(GroupVoca g, AsyncCallback<GroupVoca> callback);

	void removeGroup(long id, AsyncCallback<Void> callback);

	void getListGroupVoca(String cursorStr,
			AsyncCallback<List<GroupVoca>> callback);
}
