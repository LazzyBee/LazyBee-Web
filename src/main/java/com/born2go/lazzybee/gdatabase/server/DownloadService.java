package com.born2go.lazzybee.gdatabase.server;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

@SuppressWarnings("serial")
public class DownloadService extends HttpServlet implements Servlet {

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String para = req.getParameter("blobkey");
		if (para == null) {
			redirect(resp);
		} else {
			BlobstoreService blobstoreService = BlobstoreServiceFactory
					.getBlobstoreService();

			BlobKey blobKey = new BlobKey(para);
			blobstoreService.serve(blobKey, resp);
			BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);

			if (blobInfo != null) {
				String encodedFilename = URLEncoder.encode(
						blobInfo.getFilename(), "utf-8");
				encodedFilename.replaceAll("\\+", "%20");

				resp.setContentType("application/octet-stream");
				resp.addHeader("Content-Disposition",
						"attachment; filename*=utf-8''" + encodedFilename);
			} else {
				 throw new IOException("No file found!");
			}

		}

	}

	private void redirect(HttpServletResponse resp) {
		// Set response content type
		resp.setContentType("text/html");
		String urlRedirect = new String(
				"https://drive.google.com/folderview?id=0B9FXcwz1xGH4Wm1nVnRTX0pjMHM&usp=sharing");
		resp.setStatus(resp.SC_MOVED_TEMPORARILY);
		resp.setHeader("Location", urlRedirect);
	}

}