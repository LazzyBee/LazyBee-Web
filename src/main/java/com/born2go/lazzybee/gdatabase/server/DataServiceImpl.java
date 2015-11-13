package com.born2go.lazzybee.gdatabase.server;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jsoup.Jsoup;

import com.born2go.lazzybee.gdatabase.client.rpc.DataService;
import com.born2go.lazzybee.gdatabase.shared.Blog;
import com.born2go.lazzybee.gdatabase.shared.Picture;
import com.born2go.lazzybee.gdatabase.shared.User;
import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.born2go.lazzybee.gdatabase.shared.nonentity.VocaList;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;

@SuppressWarnings("serial")
public class DataServiceImpl extends RemoteServiceServlet implements
		DataService {
	
	private boolean verifyAdmin(String userId) {
		if(userId != null && userId.contains("_G")) {
			User u = ofy().load().type(User.class).filter("google_id", userId).first().now();
			if(u != null && u.isAdmin())
				return true;
			else
				return false;
		}
		else if(userId != null && userId.contains("_F")) {
			User u = ofy().load().type(User.class).filter("facebook_id", userId).first().now();
			if(u != null && u.isAdmin())
				return true;
			else
				return false;
		}
		else
			return false;
	}
	
//	private String getPlainText(String strSrc) {
//		String resultStr = strSrc;
//		resultStr = resultStr.replaceAll("<figcaption>.*</figcaption>", "");
//		resultStr = resultStr.replaceAll("&nbsp;", " ");
//		return resultStr;
//	}

	/**
	 * Insert new vocabulary
	 * 
	 * @return inserted vocabulary
	 */
	@Override
	public Voca insertVoca(final Voca voca, String userId) {
		if(verifyAdmin(userId)) {
			voca.setQ(voca.getQ().toLowerCase());
			voca.setCheck(false);
			if (verifyVoca(voca.getQ())) {
				Key<Voca> key = ofy().save().entity(voca).now();
				Voca v = ofy().load().key(key).now();
				return v;
			} else
				return null;
		}
		else
			return null;
	}

	/**
	 * Verify the vocabulary
	 * 
	 * @return <b>true</b> if voca isn't exist <br/>
	 *         <b>false</b> if contrary
	 */
	@Override
	public boolean verifyVoca(String voca_q) {
		Voca voca = ofy().load().type(Voca.class).filter("q", voca_q).first()
				.now();
		if (voca == null)
			return true;
		else
			return false;
	}

	/**
	 * Find a vocabulary by question
	 * 
	 * @return the vocabulary match the question
	 */
	@Override
	public Voca findVoca(String voca_q) {
		Voca voca = ofy().load().type(Voca.class)
				.filter("q", voca_q.toLowerCase()).first().now();
		return voca;
	}

	/**
	 * Update an exist vocabulary
	 * 
	 * @return updated vocabulary, null if update fail
	 */
	@Override
	public Voca updateVoca(Voca voca, boolean isCheck, String userId) {
		if(verifyAdmin(userId)) {
			Voca v = ofy().load().type(Voca.class).id(voca.getGid()).now();
			if (v != null) {
				if (voca.getQ().equals(v.getQ())) {
					if (isCheck)
						voca.setCheck(true);
					ofy().save().entity(voca);
					return voca;
				} else {
					voca.setGid(null);
					return insertVoca(voca, userId);
				}
			}
			return null;
		}
		else
			return null;
	}

	/**
	 * Query limited vocabulary
	 */
	@Override
	public VocaList getListVoca(String cursorStr) {
		List<Voca> result = new ArrayList<Voca>();

		Query<Voca> query = ofy().load().type(Voca.class)
				.limit(VocaList.pageSize).filter("isCheck", true);
		if (cursorStr != null)
			query = query.startAt(Cursor.fromWebSafeString(cursorStr));

		boolean continu = false;
		QueryResultIterator<Voca> iterator = query.iterator();
		while (iterator.hasNext()) {
			Voca v = iterator.next();
			result.add(v);
			continu = true;
		}

		if (continu) {
			Cursor cursor = iterator.getCursor();
			String encodeCursor = cursor.toWebSafeString();
			VocaList vocaList = new VocaList(result, encodeCursor);
			return vocaList;
		} else {
			VocaList vocaList = new VocaList(result, "\\0");
			return vocaList;
		}
	}

	/**
	 * Query limited preview vocabulary
	 */
	@Override
	public VocaList getListPreviewVoca(String cursorStr) {
		List<Voca> result = new ArrayList<Voca>();

		Query<Voca> query = ofy().load().type(Voca.class)
				.limit(VocaList.pageSize).filter("isCheck", false);
		if (cursorStr != null)
			query = query.startAt(Cursor.fromWebSafeString(cursorStr));

		boolean continu = false;
		QueryResultIterator<Voca> iterator = query.iterator();
		while (iterator.hasNext()) {
			Voca v = iterator.next();
			result.add(v);
			continu = true;
		}

		if (continu) {
			Cursor cursor = iterator.getCursor();
			String encodeCursor = cursor.toWebSafeString();
			VocaList vocaList = new VocaList(result, encodeCursor);
			return vocaList;
		} else {
			VocaList vocaList = new VocaList(result, "\\0");
			return vocaList;
		}
	}

	/**
	 * Remove a vocabulary
	 */
	@Override
	public void removeVoca(Voca voca, String userId) {
		if(verifyAdmin(userId)) {
			Voca v = ofy().load().type(Voca.class).id(voca.getGid()).now();
			if (v != null)
				ofy().delete().entity(v);
		}
	}

	@Override
	public User saveUser(User user) {
		user.setAdmin(false);
		if (user.getGoogle_id() != null) {
			User old_user = ofy().load().type(User.class)
					.filter("google_id", user.getGoogle_id()).first().now();
			if (old_user == null) {
				ofy().save().entity(user);
				return user;
			}
			else
				return old_user;
		}
		else if (user.getFacebook_id() != null) {
			User old_user = ofy().load().type(User.class)
					.filter("facebook_id", user.getFacebook_id()).first().now();
			if (old_user == null) {
				ofy().save().entity(user);
				return user;
			}
			else
				return old_user;
		}
		else
			return user;
	}

	@Override
	public boolean verifyBlog(String blogTitle) {
		Blog blog = ofy().load().type(Blog.class).filter("title", blogTitle)
				.first().now();
		if (blog == null)
			return true;
		else
			return false;
	}

	@Override
	public Blog insertBlog(Blog blog, String userId) {
//		String origin_title = blog.getTitle();
//		boolean verify_blog = false;
//		for (int i = 0; i <= 9; i++) {
//			if (!verifyBlog(blog.getTitle())) {
//				Random generator = new Random();
//				int random_title = generator.nextInt(100);
//				blog.setTitle(origin_title + "_" + random_title);
//			} else {
//				verify_blog = true;
//				break;
//			}
//		}
		if(verifyAdmin(userId)) {
			if (verifyBlog(blog.getTitle())) {
				blog.setCreateDate(System.currentTimeMillis());
				Key<Blog> key = ofy().save().entity(blog).now();
				Blog b = ofy().load().key(key).now();
				return b;
			} else
				return null;
			}
		else
			return null;
	}

	@Override
	public Blog updateBlog(Blog blog, String userId) {
		if(verifyAdmin(userId)) {
			Blog old_blog = findBlogById(blog.getId());
			if(old_blog != null) {
//				String preshortHtml = getPlainText(blog.getContent());
//				blog.setShortDescription(Jsoup.parse(preshortHtml).text());
				ofy().save().entity(blog);
				return blog;
			}
			return null;
		}
		else
			return null;
	}

	@Override
	public Blog findBlogById(Long blogId) {
		Blog blog = ofy().load().type(Blog.class).id(blogId).now();
		return blog;
	}

	@Override
	public Blog findBlogByTitle(String blogTitle) {
		Blog blog = ofy().load().type(Blog.class).filter("title", blogTitle)
				.first().now();
		return blog;
	}

	@Override
	public List<Blog> getListBlog() {
		List<Blog> blogs = ofy().load().type(Blog.class).limit(6).list();
		List<Blog> result = new ArrayList<Blog>();
		result.addAll(blogs);
		return result;
	}
	
	public Blog getPreviousBlog(Blog currentBlog) {
		Blog blog = ofy().load().type(Blog.class).
				filter("createDate <", currentBlog.getCreateDate()).order("-createDate").first().now();
		return blog;
	}
	
	public Blog getNextBlog(Blog currentBlog) {
		Blog blog = ofy().load().type(Blog.class).
				filter("createDate >", currentBlog.getCreateDate()).order("createDate").first().now();
		return blog;
	}

	@Override
	public Picture findPicture(Long pictureId) {
		if(pictureId != null) {
			Picture picture = ofy().load().type(Picture.class).id(pictureId).now();
			return picture;
		}
		else 
			return null;
	}

	private BlobstoreService blobStoreService = BlobstoreServiceFactory
			.getBlobstoreService();

	@Override
	public String getUploadUrl(String userId) {
		if(verifyAdmin(userId)) {		
			return blobStoreService.createUploadUrl("/photo_upload");
		}
		else
			return null;
	}
}
