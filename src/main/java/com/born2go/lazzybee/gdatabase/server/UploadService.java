package com.born2go.lazzybee.gdatabase.server;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.born2go.lazzybee.gdatabase.shared.Blog;
import com.born2go.lazzybee.gdatabase.shared.Picture;
import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.googlecode.objectify.Key;

@SuppressWarnings("serial")
public class UploadService extends HttpServlet implements Servlet {

	private BlobstoreService blobstoreService = BlobstoreServiceFactory
			.getBlobstoreService();

	private ImagesService imagesService = ImagesServiceFactory
			.getImagesService();

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
		List<BlobKey> blobKeys = blobs.get("file");
		System.out.println("Blog Id: " + req.getParameter("blogId")
				+ "-----------------------------------");

		if (blobKeys != null) {
			for (BlobKey key : blobKeys) {
				// get file name on blob info
				BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(key);
				long size = blobInfo.getSize();
				if (size > 0) {
					String encodedFilename = URLEncoder.encode(
							blobInfo.getFilename(), "utf-8");
					encodedFilename.replaceAll("\\+", "%20");
					// Set fileupload info
					Picture file = new Picture();
					file.setKey(key.getKeyString());
					String serveUrl = imagesService.getServingUrl(
							ServingUrlOptions.Builder.withBlobKey(key))
							.replace("0.0.0.0", "127.0.0.1");
					file.setServeUrl(serveUrl);
					Key<Picture> keyPicture = ofy().save().entity(file).now();
					Picture exportPicture = ofy().load().key(keyPicture).now();

					// Save id picture on blog
					Long blogId = Long.valueOf(req.getParameter("blogId")
							.replaceAll(",", ""));
					Blog blog = ofy().load().type(Blog.class).id(blogId).now();
					if (blog != null) {
						blog.setAvatar(exportPicture.getId());
						ofy().save().entity(blog);
					}
				} else {
					blobstoreService.delete(key);
				}
			}
		}
	}
}
