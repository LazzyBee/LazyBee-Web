package com.born2go.lazzybee.gdatabase.server;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.born2go.lazzybee.gdatabase.client.rpc.DataService;
import com.born2go.lazzybee.gdatabase.shared.Blog;
import com.born2go.lazzybee.gdatabase.shared.GroupVoca;
import com.born2go.lazzybee.gdatabase.shared.Picture;
import com.born2go.lazzybee.gdatabase.shared.SearchLog;
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
		if (userId != null && userId.contains("_G")) {
			User u = ofy().load().type(User.class).filter("google_id", userId)
					.first().now();
			if (u != null && u.isAdmin())
				return true;
			else
				return false;
		} else if (userId != null && userId.contains("_F")) {
			User u = ofy().load().type(User.class)
					.filter("facebook_id", userId).first().now();
			if (u != null && u.isAdmin())
				return true;
			else
				return false;
		} else
			return false;
	}

	private User getUser(String userId) {
		if (userId != null && userId.contains("_G")) {
			User u = ofy().load().type(User.class).filter("google_id", userId)
					.first().now();
			return u;
		} else if (userId != null && userId.contains("_F")) {
			User u = ofy().load().type(User.class)
					.filter("facebook_id", userId).first().now();
			return u;
		}
		return null;
	}

	/*
	 * private String getPlainText(String strSrc) { String resultStr = strSrc;
	 * resultStr = resultStr.replaceAll("<figcaption>.*</figcaption>", "");
	 * resultStr = resultStr.replaceAll("&nbsp;", " "); return resultStr; }
	 */

	/**
	 * 
	 * @param q_Der
	 *            : question derivatives
	 * @return the vocabulary match to q_Der
	 */
	private Voca findVoca_Derivatives(String q) {
		Voca result = null;
		if (q.endsWith(q_ed)) {
			result = findVoca_DerEd(q);
		} else if (q.endsWith(q_ing)) {
			result = findVoca_DerIng(q);
		} else if (q.endsWith(q_s)) {
			result = findVoca_DerS(q);
		}
		return result;

	}

	/**
	 * Lấy một số trường hợp dẫn xuất từ Verb trong tiếng anh khi V-ed
	 * 
	 * @param q
	 *            : question of voca search by user
	 * @return a vocabulary match with question
	 */
	protected Voca findVoca_DerEd(String q) {
		String q_Der = null;
		Voca result = null;
		// Want --> Wanted
		q_Der = q.substring(0, q.lastIndexOf(q_ed));
		result = searchVoca(q_Der);
		if (result == null) {
			// Live --> Lived
			q_Der = q.substring(0, q.lastIndexOf("d"));
			result = searchVoca(q_Der);
			if (result == null) {
				// Study --> Studied
				if (q.endsWith("ied")) {
					q_Der = q.substring(0, q.lastIndexOf("ied")) + "y";
					result = searchVoca(q_Der);
				}
			}
		}

		return result;
	}

	/**
	 * Hàm lấy một số trường hợp dẫn xuất verb trong tiếng anh V-ing
	 * 
	 * @param q
	 *            : question of Voca search by user
	 * 
	 * @return a vocabulary match with the q
	 */
	protected Voca findVoca_DerIng(String q) {
		String q_Der = null;
		Voca result = null;
		// open => opening
		q_Der = q.substring(0, q.lastIndexOf(q_ing));
		result = searchVoca(q_Der);
		if (result == null) {
			// take => taking
			q_Der = q.substring(0, q.lastIndexOf("ing")) + "e";
			result = searchVoca(q_Der);
		}
		return result;
	}

	/**
	 * Hàm lấy một số trường hợp dẫn xuất verb trong tiếng anh V-s/es
	 * 
	 * @param q
	 *            : question user want to search in db
	 * @return a vocabulary match with q in db
	 */
	protected Voca findVoca_DerS(String q) {
		Voca result = null;
		String q_Der = null;
		// run -> runs
		q_Der = q.substring(0, q.lastIndexOf(q_s));
		result = searchVoca(q_Der);
		if (result == null) {
			// do -> does
			if (q.endsWith("es")) {
				q_Der = q.substring(0, q.lastIndexOf("es"));
				result = searchVoca(q_Der);
				if (result == null) {
					// try -> tries
					if (q.endsWith("ies")) {
						q_Der = q.substring(0, q.lastIndexOf("ies")) + "y";
						result = searchVoca(q_Der);
					}
				}
			}
		}

		return result;

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
		Voca voca = ofy().load().type(Voca.class).filter("q", voca_q).first()
				.now();
		if (voca != null)
			return false;
		else {
			VocaPreview voca_preview = ofy().load().type(VocaPreview.class)
					.filter("q", voca_q).first().now();
			if (voca_preview != null)
				return false;
			else
				return true;
		}
	}

	/**
	 * this method Find a vocabulary by question and insert question into table
	 * SearchLog, this method use for API
	 * 
	 * @return the vocabulary match the question
	 */

	public Voca findVoca(String voca_q) {
		String q = voca_q.trim().toLowerCase();
		Voca result = getVoca_byQ(q);
		// save voca_q in to table SearchLog
		// saveSearchLog_API(q, result);
		return result;
	}

	/**
	 * Find a vocabulary by question and insert question into table if question
	 * not exist in db
	 * 
	 * @param voca_q
	 *            : question search by user
	 * @param saveLog
	 *            : if saveLog = true -> save question in to table SearchLog
	 * @return the vocabulary match the question
	 */
	@Override
	public Voca findVoca_Web(String voca_q, boolean saveLog) {
		voca_q = voca_q.toLowerCase();
		Voca result = getVoca_byQ(voca_q);
		// save voca_q in to table SearchLog
		// saveSearchLog_Web(voca_q, result);
		return result;
	}

	/**
	 * this method get a Voca in database by question
	 * 
	 * @param q_voca
	 *            : question of Voca
	 * @return a Voca match with question in database
	 */
	private Voca getVoca_byQ(String q_voca) {
		Voca result = searchVoca(q_voca);
		// tìm kiếm dẫn suất của q_voca example: v-ing, v-ed, v-s, v-es
		if (result == null) {
			result = findVoca_Derivatives(q_voca);
		}
		return result;
	}

	/**
	 * this method use find a vocabulary in database
	 * 
	 * @param q
	 *            : a question user want to search
	 * @return the vocabulary match to q
	 */
	protected Voca searchVoca(String q) {
		Voca result = null;
		// find vocabulary in table Voca
		Voca voca = ofy().load().type(Voca.class).filter("q", q).first().now();
		// if voca != null, return value
		if (voca != null) {
			result = voca;
			result.setCheck(true);
		}
		// else find vocabulary in table VocaPreview
		else {
			VocaPreview voca_preview = ofy().load().type(VocaPreview.class)
					.filter("q", q).first().now();
			if (voca_preview != null) {
				result = new Voca();
				result.getVocaPreviewContent(voca_preview);
				result.setCheck(false);
			}
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
		// Admin update
		if (verifyAdmin(userId)) {
			Voca v = ofy().load().type(Voca.class)
					.filter("q", voca.getQ().toLowerCase()).first().now();
			if (v != null) {
				Long gid = v.getGid();
				v = voca;
				v.setGid(gid);
				v.setQ(v.getQ().toLowerCase());
				ofy().save().entity(v);
				return voca;
			} else {
				VocaPreview vp = ofy().load().type(VocaPreview.class)
						.filter("q", voca.getQ().toLowerCase()).first().now();
				if (vp != null) {
					vp.getVocaContent(voca);
					vp.setQ(vp.getQ().toLowerCase());
					ofy().save().entity(vp);
					return voca;
				} else
					return null;
			}
		}
		// User update
		else {
			Voca v = ofy().load().type(Voca.class)
					.filter("q", voca.getQ().toLowerCase()).first().now();
			if (v != null) {
				VocaPreview vp = new VocaPreview();
				vp.getVocaContent(voca);
				vp.setQ(vp.getQ().toLowerCase());
				ofy().save().entity(vp);
				/* ofy().delete().entity(v); */
				return voca;
			} else {
				VocaPreview vp = ofy().load().type(VocaPreview.class)
						.filter("q", voca.getQ().toLowerCase()).first().now();
				if (vp != null) {
					vp.getVocaContent(voca);
					vp.setQ(vp.getQ().toLowerCase());
					ofy().save().entity(vp);
					return voca;
				} else
					return null;
			}
		}
	}

	@Override
	public Voca verifyUpdateVoca(Voca voca, String userId) {
		if (verifyAdmin(userId)) {
			VocaPreview vp = ofy().load().type(VocaPreview.class)
					.filter("q", voca.getQ().toLowerCase()).first().now();
			if (vp != null) {
				ofy().delete().entity(vp);
				voca.setQ(voca.getQ().toLowerCase());
				Voca v = ofy().load().type(Voca.class)
						.filter("q", voca.getQ().toLowerCase()).first().now();
				if (v != null)
					voca.setGid(v.getGid());
				ofy().save().entity(voca);
				voca.setCheck(true);
				return voca;
			} else
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

		Query<Voca> query = ofy().load().type(Voca.class)
				.limit(VocaList.pageSize);
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

		Query<VocaPreview> query = ofy().load().type(VocaPreview.class)
				.limit(VocaList.pageSize);
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
		if (verifyAdmin(userId)) {
			if (voca.isCheck()) {
				Voca v = ofy().load().type(Voca.class).id(voca.getGid()).now();
				if (v != null)
					ofy().delete().entity(v);
			} else {
				VocaPreview vp = ofy().load().type(VocaPreview.class)
						.id(voca.getGid()).now();
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
			} else
				return old_user;
		} else if (user.getFacebook_id() != null) {
			User old_user = ofy().load().type(User.class)
					.filter("facebook_id", user.getFacebook_id()).first().now();
			if (old_user == null) {
				ofy().save().entity(user);
				return user;
			} else
				return old_user;
		} else
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
		/*
		 * String origin_title = blog.getTitle(); boolean verify_blog = false;
		 * for (int i = 0; i <= 9; i++) { if (!verifyBlog(blog.getTitle())) {
		 * Random generator = new Random(); int random_title =
		 * generator.nextInt(100); blog.setTitle(origin_title + "_" +
		 * random_title); } else { verify_blog = true; break; } }
		 */
		if (verifyAdmin(userId)) {
			if (verifyBlog(blog.getTitle())) {
				blog.setCreateDate(System.currentTimeMillis());
				Key<Blog> key = ofy().save().entity(blog).now();
				Blog b = ofy().load().key(key).now();
				return b;
			} else
				return null;
		} else
			return null;
	}

	@Override
	public Blog updateBlog(Blog blog, String userId) {
		if (verifyAdmin(userId)) {
			Blog old_blog = findBlogById(blog.getId());
			if (old_blog != null) {
				/*
				 * String preshortHtml = getPlainText(blog.getContent());
				 * blog.setShortDescription(Jsoup.parse(preshortHtml).text());
				 */
				ofy().save().entity(blog);
				return blog;
			}
			return null;
		} else
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
		if (isLimited) {
			List<Blog> blogs = ofy().load().type(Blog.class).limit(6)
					.order("-createDate").list();
			List<Blog> result = new ArrayList<Blog>();
			result.addAll(blogs);
			return result;
		} else {
			List<Blog> blogs = ofy().load().type(Blog.class)
					.order("-createDate").list();
			List<Blog> result = new ArrayList<Blog>();
			result.addAll(blogs);
			return result;
		}
	}

	public Blog getPreviousBlog(Blog currentBlog) {
		Blog blog = ofy().load().type(Blog.class)
				.filter("createDate <", currentBlog.getCreateDate())
				.order("-createDate").first().now();
		return blog;
	}

	public Blog getNextBlog(Blog currentBlog) {
		Blog blog = ofy().load().type(Blog.class)
				.filter("createDate >", currentBlog.getCreateDate())
				.order("createDate").first().now();
		return blog;
	}

	/*
	 * Get list blog older than currentBlog
	 */
	public List<Blog> getBlogsOlder(Blog currentBlog) {
		List<Blog> result = ofy().load().type(Blog.class)
				.filter("createDate <", currentBlog.getCreateDate())
				.order("-createDate").limit(6).list();
		if (result.size() <= 4) {
			result = ofy().load().type(Blog.class)
					.filter("createDate >", currentBlog.getCreateDate())
					.limit(6).list();
		}
		return result;
	}

	@Override
	public Picture findPicture(Long pictureId) {
		if (pictureId != null) {
			Picture picture = ofy().load().type(Picture.class).id(pictureId)
					.now();
			return picture;
		} else
			return null;
	}

	private BlobstoreService blobStoreService = BlobstoreServiceFactory
			.getBlobstoreService();

	@Override
	public String getUploadUrl(String userId) {
		if (verifyAdmin(userId)) {
			return blobStoreService.createUploadUrl("/photo_upload");
		} else
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
	public LinkedHashMap<String, String> getTestVocaStep_Two(
			LinkedHashMap<String, String> hashMap) {
		hashMap.put("action", "step_one");
		// Connection
		Connection conn = Jsoup.connect("http://testyourvocab.com")
				.followRedirects(true).data(hashMap);
		Document doc;
		LinkedHashMap<String, String> hmap = new LinkedHashMap<String, String>();
		try {
			doc = conn.post();
			hmap.put(Common.USER_ID, conn.response().url().getPath() + "?"
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
			Connection conn3 = Jsoup
					.connect(
							"http://testyourvocab.com" + "/step_three?user="
									+ user_id).followRedirects(true).data(hmap);

			try {
				Document doc3 = conn3.post();
				Element element = doc3.select("div.num").first();
				cipher = Cipher.getInstance("AES");
				url = "http://www.lazzybee.com/vocab/"
						+ encrypt(element.text());
				// url = "http://localhost:8888/vocab/"+
				// encrypt(element.text());
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return url;
	}

	public String getVocabResult_Test(String encryptedText) {
		String result = null;
		try {
			cipher = Cipher.getInstance("AES");
			result = decrypt(encryptedText);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	final static String key = "lazzybee12345678";

	static Cipher cipher;

	public static String encrypt(String plainText) throws Exception {
		byte[] plainTextByte = plainText.getBytes();
		cipher.init(Cipher.ENCRYPT_MODE, genKey());
		byte[] encryptedByte = cipher.doFinal(plainTextByte);
		String encryptedText = Base64.encodeBase64String(encryptedByte);
		return encryptedText;
	}

	public static String decrypt(String encryptedText) throws Exception {

		byte[] encryptedTextByte = Base64.decodeBase64(encryptedText);
		cipher.init(Cipher.DECRYPT_MODE, genKey());
		byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
		String decryptedText = new String(decryptedByte);
		return decryptedText;
	}

	private static java.security.Key genKey() {
		java.security.Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
		return secretKey;
	}

	public String getIMG_result(String url) {
		String result = null;
		try {
			Document doc = Jsoup.connect(url).get();
			Element image = doc.select("img").first();
			result = image.absUrl("src");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/*
	 * this method save new GroupVoca (non-Javadoc) parameter GroupVoca g
	 */
	@Override
	public GroupVoca insertGroupVoca(GroupVoca g) {
		// List<GroupVoca> listG = ofy().load().type(GroupVoca.class)
		// .order("-__key__").limit(1).list();
		if (g.getCreator() == null) {
			return null;
		} else {
			List<GroupVoca> listG = ofy().load().type(GroupVoca.class)
					.orderKey(true).limit(1).list();
			long id = 10001;
			if (listG != null && !listG.isEmpty()) {
				id = listG.get(0).getId() + 1;
			}
			g.setId(id);
			ofy().save().entity(g).now();
			GroupVoca result = new GroupVoca();
			result.getGroupVocaPreview(g);
			return result;
		}

	}

	/**
	 * this method find GroupVoca in database return a GroupVoca
	 */
	@Override
	public GroupVoca findGroupVoca(long id) {
		GroupVoca g = ofy().load().type(GroupVoca.class).id(id).now();
		return g;
	}

	/**
	 * this method update exist GroupVoca in database, submit by admin or
	 * creator
	 */
	@Override
	public GroupVoca updateGroupVoca(GroupVoca g, String userId) {
		// admin update
		if (verifyAdmin(userId)) {
			GroupVoca findG = findGroupVoca(g.getId());
			if (findG != null)
				ofy().save().entity(g);
			else
				g = null;

		}
		// creator update
		else {
			User u = getUser(userId);
			if (u.getUserName().equals(g.getCreator())) {
				GroupVoca findG = findGroupVoca(g.getId());
				if (findG != null)
					ofy().save().entity(g);
				else
					g = null;

			} else
				g = null;
		}
		return g;

	}

	/**
	 * this method remove a GroupVoca by admin or user creator it
	 */
	 
	@Override
	public void removeGroup(long id, String userId) {
		GroupVoca findG = findGroupVoca(id);
		// admin delete
		if (verifyAdmin(userId)) {
			if (findG != null)
				ofy().delete().entity(findG);
		}
		// user delete
		else {
			User u = getUser(userId);
			if (u.getUserName().equals(findG.getCreator()))
				ofy().delete().entity(findG);

		}

	}

	/*
	 *this method get list GroupVoca by cursor, limit is 100
	 */
	@Override
	public List<GroupVoca> getListGroupVoca(String cursorStr) {
		List<GroupVoca> result = new ArrayList<GroupVoca>();
		int size = 100;
		Query<GroupVoca> query = ofy().load().type(GroupVoca.class).limit(size);
		if (cursorStr != null)
			query = query.startAt(Cursor.fromWebSafeString(cursorStr));
		boolean continu = false;
		QueryResultIterator<GroupVoca> iterator = query.iterator();
		while (iterator.hasNext()) {
			GroupVoca v = iterator.next();
			result.add(v);
			continu = true;
		}
		String encodeCursor;
		if (continu) {
			Cursor cursor = iterator.getCursor();
			encodeCursor = cursor.toWebSafeString();

		} else
			encodeCursor = "\\0";

		GroupVoca vocaCur = new GroupVoca();
		vocaCur.setCreator(encodeCursor);
		result.add(0, vocaCur);
		return result;
	}

	/**
	 * this method save a new entity SearchLog in google data for version mobile
	 * 
	 * @param q
	 *            : voca_q will save in table SearhLog google data
	 */
	public void saveSearchLog_API(String q, Voca v) {
		String q_save = q.toLowerCase();
		SearchLog find = findSearchLog(q_save);
		if (v == null) {
			// save a new SearchLog in database, a new entity has sum = 0
			// because not found in Voca
			if (find == null) {
				SearchLog s = new SearchLog();
				s.setQ(q_save);
				s.setSum(0);
				ofy().save().entity(s).now();
			}

		} else {
			// if question find on Voca, not found on SearchLog -> save a new
			// entity SearchLog and sum = 1
			if (find == null) {
				SearchLog s = new SearchLog();
				s.setQ(q_save);
				s.setSum(1);
				ofy().save().entity(s).now();
			}
			// if question find on Voca, find on SearchLog -> update sum in
			// SearchLog

			else {
				updateSearchLog(find);
			}
		}

	}

	/**
	 * this method save a new entity SearchLog in google data for version web
	 * 
	 * @param q
	 *            : voca_q will save in table SearhLog google data
	 */
	public void saveSearchLog_Web(String q, Voca v) {
		String q_save = q.toLowerCase();
		SearchLog find = findSearchLog(q_save);
		if (v == null)
			// save a new SearchLog in database, a new entity has sum = 0
			// because not found in Voca
			if (find == null) {
				SearchLog s = new SearchLog();
				s.setQ(q_save);
				s.setSum(0);
				ofy().save().entity(s).now();
			}

	}

	/**
	 * 
	 * @param q
	 *            : question search by user
	 * @return a SearchLog in database
	 */
	private SearchLog findSearchLog(String q) {
		SearchLog s = ofy().load().type(SearchLog.class).id(q).now();
		return s;
	}

	/**
	 * this method update total search question by user
	 * 
	 * @param s
	 */
	private void updateSearchLog(SearchLog s) {
		s.setSum(s.getSum() + 1);
		ofy().save().entity(s).now();
	}

}
