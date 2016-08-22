package com.born2go.lazzybee.gdatabase.server;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.born2go.lazzybee.gdatabase.shared.BackupFile;
import com.born2go.lazzybee.gdatabase.shared.GroupVoca;
import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.born2go.lazzybee.gdatabase.shared.VocaPreview;
import com.born2go.lazzybee.gdatabase.shared.nonentity.DownloadTarget;
import com.born2go.lazzybee.gdatabase.shared.nonentity.UploadTarget;
import com.born2go.lazzybee.gdatabase.shared.nonentity.VocaList;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.ConflictException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.repackaged.com.google.api.client.util.Strings;
import com.googlecode.objectify.cmd.Query;

/** An endpoint class we are exposing */

@Api(name = "dataServiceApi", version = "v1.1", title = "LazzyBee Backend Api", namespace = @ApiNamespace(ownerDomain = "server.gdatabase.lazzybee.born2go.com", ownerName = "server.gdatabase.lazzybee.born2go.com", packagePath = ""))
public class DataServiceApi {

	private BlobstoreService blobStoreService = BlobstoreServiceFactory
			.getBlobstoreService();

	private DataServiceImpl dataService = new DataServiceImpl();

	/** Get a vocabulary by id */
	@ApiMethod(name = "getVocaById", path = "get_voca_byId")
	public Voca getVocaById(@Named("id") Long id) throws NotFoundException {
		Voca voca = ofy().load().type(Voca.class).id(id).now();
		if (voca == null) {
			String message = "No entity exists with ID: " + id;
			throw new NotFoundException(message);
		} else {
			voca.setCheck(true);
			return voca;
		}
	}

	/**
	 * Get a vocabulary by question
	 * 
	 * @throws NotFoundException
	 */
	@ApiMethod(name = "getVocaByQ", path = "get_voca_byQ")
	public Voca getVocaByQ(@Named("q") String q) throws NotFoundException {
		Voca result = dataService.findVoca(q);
		if (result != null)
			return result;
		else {
			String message = "No entity exists with question: " + q;
			throw new NotFoundException(message);
		}
	}

	// @Named("cursorStr") annote chi parameter
	/** Get list all of vocabulary */
	@ApiMethod(name = "listVoca")
	public List<Voca> listVoca(@Named("cursorStr") String cursorStr) {
		// List<Voca> list_voca = ofy().load().type(Voca.class)
		// .filter("level <", 8).list();
		if (cursorStr.equals("LAZZYBEE"))
			cursorStr = null;
		List<Voca> result = new ArrayList<Voca>();
		int size = 1000;
		Query<Voca> query = ofy().load().type(Voca.class).filter("level <", 8)
				.limit(size);
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
		String encodeCursor;
		if (continu) {
			Cursor cursor = iterator.getCursor();
			encodeCursor = cursor.toWebSafeString();

		} else
			encodeCursor = "\\0";

		Voca vocaCur = new Voca();
		vocaCur.setQ(encodeCursor);
		result.add(0, vocaCur);
		return result;
	}

	private VocaList getListVoca(String cursorStr) {
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

	/** Save a vocabulary */
	@ApiMethod(name = "saveVoca")
	public void saveVoca(Voca voca) throws ConflictException {
		voca.setQ(voca.getQ().toLowerCase());
		Voca v = ofy().load().type(Voca.class).filter("q", voca.getQ()).first()
				.now();
		VocaPreview vp = ofy().load().type(VocaPreview.class)
				.filter("q", voca.getQ()).first().now();
		if (v == null && vp == null)
			ofy().save().entity(voca).now();
		else {
			String message = voca.getQ() + " already existed";
			throw new ConflictException(message);
		}
	}

	/** update answer a vocabulary */
	@ApiMethod(name = "updateA", path = "update_A")
	public void updateA(Voca voca) throws NotFoundException {
		voca.setQ(voca.getQ().toLowerCase());
		Voca v = ofy().load().type(Voca.class).filter("q", voca.getQ()).first()
				.now();
		if (v != null) {
			v.setA(voca.getA());
			ofy().save().entity(v);
		} else {
			String message = voca.getQ() + " not found";
			throw new NotFoundException(message);
		}
	}

	/** update dictionary a vocabulary */
	@ApiMethod(name = "updateD", path = "update_D")
	public void updateD(Voca voca) throws NotFoundException {
		voca.setQ(voca.getQ().toLowerCase());
		Voca v = ofy().load().type(Voca.class).filter("q", voca.getQ()).first()
				.now();
		if (v != null) {
			v.setL_en(voca.getL_en());
			v.setL_vn(voca.getL_vn());
			ofy().save().entity(v);
		} else {
			String message = voca.getQ() + " not found";
			throw new NotFoundException(message);
		}
	}

	/** update l_en of dictionary a vocabulary */
	@ApiMethod(name = "updateEDict", path = "updateEDict")
	public void updateEDict(Voca voca) throws NotFoundException {
		voca.setQ(voca.getQ().toLowerCase().trim());
		Voca v = ofy().load().type(Voca.class).filter("q", voca.getQ().trim())
				.first().now();
		if (v != null) {
			if (!Strings.isNullOrEmpty(voca.getL_en())) {
				v.setL_en(voca.getL_en());
				ofy().save().entity(v);
			}

		} else {
			String message = voca.getQ() + " not found";
			throw new NotFoundException(message);
		}
	}
	
	/** update l_vn of dictionary a vocabulary */
	@ApiMethod(name = "updateVDict", path = "updateVDict")
	public void updateVDict(Voca voca) throws NotFoundException {
		voca.setQ(voca.getQ().toLowerCase().trim());
		Voca v = ofy().load().type(Voca.class).filter("q", voca.getQ().trim())
				.first().now();
		if (v != null) {
			if (!Strings.isNullOrEmpty(voca.getL_vn())) {
				v.setL_vn(voca.getL_vn());
				ofy().save().entity(v);
			}

		} else {
			String message = voca.getQ() + " not found";
			throw new NotFoundException(message);
		}
	}

	/**
	 * Find Voca by id
	 * 
	 * @param id
	 * @param orderSearch
	 *            <b>true</b> Voca -> VocaPreview, <b>false</b> VocaPreview ->
	 *            Voca
	 * @return
	 * @throws NotFoundException
	 */
	@ApiMethod(name = "findVocaById", path = "find_voca_byId")
	public Voca findVocaById(@Named("id") Long id,
			@Named("orderSearch") Boolean orderSearch) throws NotFoundException {
		if (orderSearch) {
			Voca v = ofy().load().type(Voca.class).id(id).now();
			if (v != null) {
				v.setCheck(true);
				return v;
			} else {
				VocaPreview vp = ofy().load().type(VocaPreview.class).id(id)
						.now();
				if (vp != null) {
					Voca voca = new Voca();
					voca.getVocaPreviewContent(vp);
					voca.setGid(vp.getGid());
					voca.setCheck(false);
					return voca;
				} else {
					String message = "No voca match with id: " + id;
					throw new NotFoundException(message);
				}
			}
		} else {
			VocaPreview vp = ofy().load().type(VocaPreview.class).id(id).now();
			if (vp != null) {
				Voca voca = new Voca();
				voca.getVocaPreviewContent(vp);
				voca.setGid(vp.getGid());
				voca.setCheck(false);
				return voca;
			} else {
				Voca v = ofy().load().type(Voca.class).id(id).now();
				if (v != null) {
					v.setCheck(true);
					return v;
				} else {
					String message = "No voca match with id: " + id;
					throw new NotFoundException(message);
				}
			}
		}
	}

	/**
	 * Find Voca by Q
	 * 
	 * @param q
	 * @param orderSearch
	 *            <b>true</b> Voca -> VocaPreview, <b>false</b> VocaPreview ->
	 *            Voca
	 * @return
	 * @throws NotFoundException
	 */
	@ApiMethod(name = "findVocaByQ", path = "find_voca_byQ")
	public Voca findVocaByQ(@Named("q") String q,
			@Named("orderSearch") Boolean orderSearch) throws NotFoundException {
		// Voca result = null;
		// if (orderSearch)
		// result = dataService.findVoca(q);
		// else
		// result = searchVocaPreview_Voca(q);
		//
		// if (result != null)
		// return result;
		// else {
		// String message = q + " not found";
		// throw new NotFoundException(message);
		// }
		return null;
	}

	/**
	 * Open upload url for app
	 * 
	 * @return
	 */
	@ApiMethod(name = "getUploadUrl")
	public UploadTarget getUploadUrl() {
		UploadTarget ut = new UploadTarget();
		ut.setUrl(blobStoreService.createUploadUrl("/backup_upload"));
		return ut;
	}

	/**
	 * Get download url
	 * 
	 * @param code
	 *            link with backup file
	 * @return
	 */
	@ApiMethod(name = "getDownloadUrl")
	public DownloadTarget getDownloadUrl(@Named("code") String code) {
		DownloadTarget dt = new DownloadTarget();
		BackupFile f = ofy().load().type(BackupFile.class)
				.id(code.toLowerCase()).now();
		if (f == null) {
			f = ofy().load().type(BackupFile.class).id(code.toUpperCase())
					.now();
		}
		if (f != null) {
			dt.setUrl("http://www.lazzybee.com/download?blobkey="
					+ f.getBlob_key());
			return dt;
		}
		return null;
	}

	/**
	 * this method search voca in table VocaPreview fist, if voca = null, this
	 * method will search in Voca; if value voca continue = null; find
	 * derivatives of q, and continue find voca by derivatives of q
	 * 
	 * @param q
	 *            : q match voca in gdatabase
	 * @return voca
	 */
	private Voca searchVocaPreview_Voca(String q) {
		Voca result = null;
		// VocaPreview vp = ofy().load().type(VocaPreview.class).filter("q", q)
		// .first().now();
		// if (vp != null) {
		// result = new Voca();
		// result.getVocaPreviewContent(vp);
		// result.setGid(vp.getGid());
		// result.setCheck(false);
		//
		// } else {
		// result = ofy().load().type(Voca.class).filter("q", q).first().now();
		// if (result != null) {
		// result.setCheck(true);
		//
		// } else
		// result = null;
		//
		// }
		// if (result == null) {
		// String q_Der = dataService.getQ_Derivatives(q);
		// result = findVoca_DerAPI(q_Der);
		// }

		return result;
	}

	/**
	 * 
	 * @param q_Der
	 *            : question derivatives
	 * @return the vocabulary match to q_Der
	 */
	private Voca findVoca_DerAPI(String q_Der) {
		Voca result = null;
		VocaPreview vp = ofy().load().type(VocaPreview.class)
				.filter("q", q_Der).first().now();
		if (vp != null) {
			result = new Voca();
			result.getVocaPreviewContent(vp);
			result.setGid(vp.getGid());
			result.setCheck(false);
		} else {
			result = ofy().load().type(Voca.class).filter("q", q_Der).first()
					.now();
			if (result != null) {
				result.setCheck(true);
			} else
				result = null;
		}
		return result;
	}

	@ApiMethod(name = "getGroupVoca", path = "get_Group_Voca")
	public GroupVoca getGroupVoca(@Named("id") long id)
			throws NotFoundException {
		GroupVoca result = dataService.findGroupVoca(id);
		if (result != null)
			return result;
		else {
			String message = "No groupvoca exists with id: " + id;
			throw new NotFoundException(message);
		}
	}

}
