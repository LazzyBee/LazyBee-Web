package com.born2go.lazzybee.gdatabase.server;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;

import org.apache.commons.codec.binary.Base64;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.born2go.lazzybee.gdatabase.client.rpc.DataService;
import com.born2go.lazzybee.gdatabase.shared.Blog;
import com.born2go.lazzybee.gdatabase.shared.Picture;
import com.born2go.lazzybee.gdatabase.shared.User;
import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.born2go.lazzybee.gdatabase.shared.VocaPreview;
import com.born2go.lazzybee.gdatabase.shared.nonentity.VocaList;
import com.born2go.lazzybeemobile.shared.Common;
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
	
	/**
	 * check derivatives of one vocabulary:(ed, s, es, ing...)
	 * 
	 * @param q
	 */
	String q_ing = "ing";

	String q_ed = "ed";

	String q_s = "s";

	String q_es = "es";

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
	
	/*private String getPlainText(String strSrc) {
		String resultStr = strSrc;
		resultStr = resultStr.replaceAll("<figcaption>.*</figcaption>", "");
		resultStr = resultStr.replaceAll("&nbsp;", " ");
		return resultStr;
	}*/
	
	/**
	 * 
	 * @param q_Der
	 *            : question derivatives
	 * @return the vocabulary match to q_Der
	 */
	private Voca findVoca_Derivatives(String q_Der) {
		Voca result = null;
		Voca voca = ofy().load().type(Voca.class)
				.filter("q", q_Der.toLowerCase()).first().now();
		if (voca != null) {
			result = voca;
			result.setCheck(true);
		} else {
			VocaPreview voca_preview = ofy().load().type(VocaPreview.class)
					.filter("q", q_Der.toLowerCase()).first().now();
			if (voca_preview != null) {
				result = new Voca();
				result.getVocaPreviewContent(voca_preview);
				result.setCheck(false);
			}
		}
		return result;
	}

	protected String getQ_Derivatives(String q) {
		if (q.endsWith(q_ed)) {
			q = q.substring(0, q.lastIndexOf(q_ed));
		} else if (q.endsWith(q_ing)) {
			q = q.substring(0, q.lastIndexOf(q_ing));
		} else if (q.endsWith(q_es)) {
			q = q.substring(0, q.lastIndexOf(q_es));
		} else if (q.endsWith(q_s)) {
			q = q.substring(0, q.lastIndexOf(q_s));
		}
		return q;
	}

	/**
	 * Insert new vocabulary
	 * 
	 * @return inserted vocabulary
	 */
	@Override
	public Voca insertVoca(VocaPreview voca) {
		voca.setQ(voca.getQ().toLowerCase());
		if (verifyVoca(voca.getQ())) {
			ofy().save().entity(voca).now();
			Voca v = new Voca();
			v.getVocaPreviewContent(voca);
			return v;
		} else
			return null;
	}

	/**
	 * Verify the vocabulary in 2 table Voca & VocaPreview
	 * 
	 * @return <b>true</b> if voca isn't exist <br/>
	 *         <b>false</b> if contrary
	 */
	@Override
	public boolean verifyVoca(String voca_q) {
		Voca voca = ofy().load().type(Voca.class).filter("q", voca_q.toLowerCase()).first().now();
		if (voca != null)
			return false;
		else {
			VocaPreview voca_preview = ofy().load().type(VocaPreview.class).filter("q", voca_q.toLowerCase()).first().now();
			if(voca_preview != null)
				return false;
			else
				return true;
		}
	}

	/**
	 * Find a vocabulary by question
	 * 
	 * @return the vocabulary match the question
	 */
	@Override
	public Voca findVoca(String voca_q) {
		Voca result = null;
		Voca voca = ofy().load().type(Voca.class)
				.filter("q", voca_q.toLowerCase()).first().now();
		if (voca != null) {
			result = voca;
			result.setCheck(true);
		} else {
			VocaPreview voca_preview = ofy().load().type(VocaPreview.class)
					.filter("q", voca_q.toLowerCase()).first().now();
			if (voca_preview != null) {
				result = new Voca();
				result.getVocaPreviewContent(voca_preview);
				result.setCheck(false);
				result.setGid(voca_preview.getGid());
			}
		}
		if (result == null) {
			String q_Der = getQ_Derivatives(voca_q);
			result = findVoca_Derivatives(q_Der);
		}
		return result;
	}

	/**
	 * Update an exist vocabulary
	 * 
	 * @return updated vocabulary, null if update fail
	 */
	@Override
	public Voca updateVoca(Voca voca, String userId) {
		//Admin update
		if(verifyAdmin(userId)) {
			Voca v = ofy().load().type(Voca.class).filter("q", voca.getQ().toLowerCase()).first().now();
			if (v != null) {
				Long gid = v.getGid();
				v = voca;
				v.setGid(gid);
				v.setQ(v.getQ().toLowerCase());
				ofy().save().entity(v);
				return voca;
			} else {
				VocaPreview vp = ofy().load().type(VocaPreview.class).filter("q", voca.getQ().toLowerCase()).first().now();
				if(vp != null) {
					vp.getVocaContent(voca);
					vp.setQ(vp.getQ().toLowerCase());
					ofy().save().entity(vp);
					return voca;
				}
				else
					return null;
			}
		}
		//User update
		else {
			Voca v = ofy().load().type(Voca.class).filter("q", voca.getQ().toLowerCase()).first().now();
			if (v != null) {
				VocaPreview vp = new VocaPreview();
				vp.getVocaContent(voca);
				vp.setQ(vp.getQ().toLowerCase());
				ofy().save().entity(vp);
				/*ofy().delete().entity(v);*/
				return voca;
			} else {
				VocaPreview vp = ofy().load().type(VocaPreview.class).filter("q", voca.getQ().toLowerCase()).first().now();
				if(vp != null) {
					vp.getVocaContent(voca);
					vp.setQ(vp.getQ().toLowerCase());
					ofy().save().entity(vp);
					return voca;
				}
				else
					return null;
			}
		}
	}

	@Override
	public Voca verifyUpdateVoca(Voca voca, String userId) {
		if(verifyAdmin(userId)) {
			VocaPreview vp = ofy().load().type(VocaPreview.class).filter("q", voca.getQ().toLowerCase()).first().now();
			if(vp != null) {
				ofy().delete().entity(vp);
				voca.setQ(voca.getQ().toLowerCase());
				Voca v = ofy().load().type(Voca.class).filter("q", voca.getQ().toLowerCase()).first().now();
				if(v != null)
					voca.setGid(v.getGid());
				ofy().save().entity(voca);
				voca.setCheck(true);
				return voca;
			}
			else
				return null;
		}
		return null;
	}

	/**
	 * Query limited vocabulary
	 */
	@Override
	public VocaList getListVoca(String cursorStr) {
		List<Voca> result = new ArrayList<Voca>();

		Query<Voca> query = ofy().load().type(Voca.class).limit(VocaList.pageSize);
		if (cursorStr != null)
			query = query.startAt(Cursor.fromWebSafeString(cursorStr));

		boolean continu = false;
		QueryResultIterator<Voca> iterator = query.iterator();
		while (iterator.hasNext()) {
			Voca v = iterator.next();
			v.setCheck(true);
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

		Query<VocaPreview> query = ofy().load().type(VocaPreview.class).limit(VocaList.pageSize);
		if (cursorStr != null)
			query = query.startAt(Cursor.fromWebSafeString(cursorStr));

		boolean continu = false;
		QueryResultIterator<VocaPreview> iterator = query.iterator();
		while (iterator.hasNext()) {
			VocaPreview vp = iterator.next();
			Voca v = new Voca();
			v.getVocaPreviewContent(vp);
			v.setCheck(false);
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
			if(voca.isCheck()) {
				Voca v = ofy().load().type(Voca.class).id(voca.getGid()).now();
				if (v != null)
					ofy().delete().entity(v);
			}
			else {
				VocaPreview vp = ofy().load().type(VocaPreview.class).id(voca.getGid()).now();
				if (vp != null)
					ofy().delete().entity(vp);
			}
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
/*		String origin_title = blog.getTitle();
		boolean verify_blog = false;
		for (int i = 0; i <= 9; i++) {
			if (!verifyBlog(blog.getTitle())) {
				Random generator = new Random();
				int random_title = generator.nextInt(100);
				blog.setTitle(origin_title + "_" + random_title);
			} else {
				verify_blog = true;
				break;
			}
		}*/
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
				/*String preshortHtml = getPlainText(blog.getContent());
				blog.setShortDescription(Jsoup.parse(preshortHtml).text());*/
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
	public List<Blog> getListBlog(boolean isLimited) {
		if(isLimited) {
			List<Blog> blogs = ofy().load().type(Blog.class).limit(6).order("-createDate").list();
			List<Blog> result = new ArrayList<Blog>();
			result.addAll(blogs);
			return result;
		} else {
			List<Blog> blogs = ofy().load().type(Blog.class).order("-createDate").list();
			List<Blog> result = new ArrayList<Blog>();
			result.addAll(blogs);
			return result;
		}
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
	
	/*
	 * Get list blog older than currentBlog
	 */
	public List<Blog> getBlogsOlder(Blog currentBlog) {
		List<Blog> result = ofy().load().type(Blog.class)
				.filter("createDate <", currentBlog.getCreateDate()).order("-createDate")
				.limit(6).list();
		if (result.size() <= 4) {
			result = ofy().load().type(Blog.class)
					.filter("createDate >", currentBlog.getCreateDate())
					.limit(6).list();
		}
		return result;
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
	String lazzybee_seperate = Common.lazzybee_seperate;
	@Override
	public LinkedHashMap<String, String> getTestVocaStep_One() {
		Document doc;
		LinkedHashMap<String, String> hmap = new LinkedHashMap<String, String>();
		try {
			doc = Jsoup.connect("http://testyourvocab.com").get();
			Element table = doc.select("table.wordlist").first();
			Element row = table.select("tr").first();
			Elements tds = row.select("td");
			String key;
			String value;
			for (int i = 0; i < tds.size(); i++) {
				Elements childrenTd = tds.get(i).select("label");
				for (int j = 0; j < childrenTd.size(); j++) {
					  key = childrenTd.get(j).attr("for");
					  value = childrenTd.get(j).text();
					  hmap.put(key, value);
					  
				}
				hmap.put(String.valueOf(i), lazzybee_seperate);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		 
		return hmap;
	}

	@Override
	public LinkedHashMap<String, String> getTestVocaStep_Two(LinkedHashMap<String, String> hashMap) {
		hashMap.put("action", "step_one");
		// Connection
		Connection conn = Jsoup.connect("http://testyourvocab.com")
				.followRedirects(true).data(hashMap);
		Document doc;
		LinkedHashMap<String, String> hmap = new LinkedHashMap<String, String>();
		try {
			doc = conn.post();
			hmap.put(Common.USER_ID,  conn.response().url().getPath() + "?"
					+ conn.response().url().getQuery());
			Element table = doc.select("table.wordlist").first();
			Element row = table.select("tr").first();
			Elements tds = row.select("td");
			String key;
			String value;
			for (int i = 0; i < tds.size(); i++) {
				Elements childrenTd = tds.get(i).select("label");
				for (int j = 0; j < childrenTd.size(); j++) {
					  key = childrenTd.get(j).attr("for");
					  value = childrenTd.get(j).text();
					hmap.put(key, value);
				}
				hmap.put(String.valueOf(i), lazzybee_seperate);
				
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
 
		return hmap;
	}

	@Override
	public String getTestVocaStep_Three(LinkedHashMap<String, String> hashMap,
			String cookie, String user_id) {
		// Connection
		Connection conn = Jsoup.connect("http://testyourvocab.com" + cookie)
				.followRedirects(true).data(hashMap);
		String plainText = "";
		String url = null;
		 try {
			Document doc = conn.post();
			// connect to step four
			
			LinkedHashMap<String, String> hmap = new LinkedHashMap<String, String>();
			hmap.put(Common.USER_ID, user_id);
			hmap.put("action", "step_three");
			hmap.put("native_speaker", "");
			hmap.put("year_born", "");
			hmap.put("month_born", "");
			hmap.put("gender", "");
			hmap.put("nationality", "");
			hmap.put("nationality_nonnative", "");
			hmap.put("reading", "");
			hmap.put("literature", "");
			hmap.put("grades_completed", "");
			hmap.put("verbal_sat", "");
			hmap.put("zip_code", "");
			hmap.put("learning_status", "");
			hmap.put("level_english", "");
			hmap.put("years_total", "");
			hmap.put("years_since", "");
			hmap.put("months_abroad", "");
			
			// connect
			Connection conn3 = Jsoup.connect("http://testyourvocab.com" + "/step_three?user="
					+ user_id).followRedirects(true).data(hmap);
			
			try {
				Document doc3 = conn3.post();
				Element element = doc3.select("div.num").first();
				plainText = element.text();
				 
				java.security.Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
	            cipher = Cipher.getInstance("AES");
				String encryptedText = encrypt(plainText, secretKey);
			  url = "http://www.lazzybee.com/vocab/" + encryptedText;
			  //  url = "http://localhost:8888/vocab/"+ encryptedText;
			} catch (  Exception e) {
				e.printStackTrace();
			}
			 
		} catch (IOException e) {
			e.printStackTrace();
		}
		 
		 
		return url;
	}
	public String getVocabResult_Test(String encryptedText){
		java.security.Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
		String result = null;
        try {
			cipher = Cipher.getInstance("AES");
			result = decrypt(encryptedText, secretKey);
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		return result;
	}
	 
	final String key = "lazzybee12345678";
	 
	static Cipher cipher;
	public static String encrypt(String plainText, java.security.Key secretKey)
			throws Exception {
		byte[] plainTextByte = plainText.getBytes();
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] encryptedByte = cipher.doFinal(plainTextByte);
		String encryptedText = Base64.encodeBase64String(encryptedByte);
		return encryptedText;
	}

	public static String decrypt(String encryptedText, java.security.Key secretKey)
			throws Exception {
		 
		byte[] encryptedTextByte = Base64.decodeBase64(encryptedText);
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
		String decryptedText = new String(decryptedByte);
		return decryptedText;
	}
	 
//	public String getTestVocaStep_Four(HashMap<String, String> hmapInput, String cookie, String user_id){
//		HashMap<String, String> hmap = new HashMap<String, String>();
//		hmap.put(Common.USER_ID, user_id);
//		hmap.put("action", "step_three");
//		hmap.put("native_speaker", "");
//		hmap.put("year_born", "");
//		hmap.put("month_born", "");
//		hmap.put("gender", "");
//		hmap.put("nationality", "");
//		hmap.put("nationality_nonnative", "");
//		hmap.put("reading", "");
//		hmap.put("literature", "");
//		hmap.put("grades_completed", "");
//		hmap.put("verbal_sat", "");
//		hmap.put("zip_code", "");
//		hmap.put("learning_status", "");
//		hmap.put("level_english", "");
//		hmap.put("years_total", "");
//		hmap.put("years_since", "");
//		hmap.put("months_abroad", "");
//		
//		// connect
//		Connection conn = Jsoup.connect("http://testyourvocab.com" + cookie).followRedirects(true).data(hmap);
//		String result = "";
//		try {
//			Document doc = conn.post();
//			Element element = doc.select("div.num").first();
//			result = element.text();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return result;
//		
//	}
}
