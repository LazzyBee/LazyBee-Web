package com.born2go.lazzybee.gdatabase.client.rpc;

import java.util.List;

import com.born2go.lazzybee.gdatabase.shared.Blog;
import com.born2go.lazzybee.gdatabase.shared.Picture;
import com.born2go.lazzybee.gdatabase.shared.User;
import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.born2go.lazzybee.gdatabase.shared.VocaPreview;
import com.born2go.lazzybee.gdatabase.shared.nonentity.VocaList;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("dataservice")
public interface DataService extends RemoteService {

	public boolean verifyVoca(String voca_q);
	public Voca insertVoca(VocaPreview voca);
	public Voca findVoca(String voca_q);
	public Voca updateVoca(Voca voca, String userId);
	public Voca verifyUpdateVoca(Voca voca, String userId);
//	public Integer getTotalVoca();
//	public Integer getTotalPreviewVoca();
	public VocaList getListVoca(String cursorStr);
	public VocaList getListPreviewVoca(String cursorStr);
	public void removeVoca(Voca voca, String userId);
	
	public User saveUser(User user);
	
	public boolean verifyBlog(String blogTitle);
	public Blog insertBlog(Blog blog, String userId);
	public Blog updateBlog(Blog blog, String userId);
	public Blog findBlogById(Long blogId);
	public Blog findBlogByTitle(String blogTitle);
	public List<Blog> getListBlog(); 
	
	public Picture findPicture(Long pictureId);
	
	public String getUploadUrl(String userId);
}
