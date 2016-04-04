package com.born2go.lazzybee.gdatabase.server;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.born2go.lazzybee.gdatabase.shared.BackupFile;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

@SuppressWarnings("serial")
public class BackupUploadService extends HttpServlet implements Servlet {

	private BlobstoreService blobstoreService = BlobstoreServiceFactory
			.getBlobstoreService();
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		
		String device_id = req.getParameter("device_id");
		if(device_id == null || device_id.equals("")) {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Device id not found!");
			return;
		}

		Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
		
		//check file upload list
		List<BlobKey> blobKeys = blobs.get("file");
		if(blobKeys.isEmpty())
			res.sendError(HttpServletResponse.SC_NO_CONTENT, "No file found!");
		else {
			for(BlobKey key: blobKeys) {
				BackupFile f = new BackupFile();
				f.setId(device_id.substring(device_id.length() - 6, device_id.length()));
				f.setBlob_key(key.getKeyString());
				ofy().save().entity(f);
			}
 		}
	}
}
