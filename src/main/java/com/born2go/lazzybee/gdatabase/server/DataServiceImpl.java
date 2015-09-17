package com.born2go.lazzybee.gdatabase.server;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;

import com.born2go.lazzybee.gdatabase.client.rpc.DataService;
import com.born2go.lazzybee.gdatabase.shared.User;
import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.born2go.lazzybee.gdatabase.shared.nonentity.VocaList;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;

@SuppressWarnings("serial")
public class DataServiceImpl extends RemoteServiceServlet implements DataService{

	/**
	 * Insert new vocabulary
	 * @return inserted vocabulary
	 */
	@Override
	public Voca insertVoca(final Voca voca) {
		voca.setQ(voca.getQ().toLowerCase());
		voca.setCheck(true);
		if(verifyVoca(voca.getQ())) {
    		Key<Voca> key = ofy().save().entity(voca).now();
    		Voca v = ofy().load().key(key).now();
    		return v;
    	}
    	else 
    		return null;
	}

	/**
	 * Verify the vocabulary
	 * @return 	<b>true</b> if voca isn't exist <br/> 
	 * 			<b>false</b> if contrary 
	 */
	@Override
	public boolean verifyVoca(String voca_q) {
		Voca voca = ofy().load().type(Voca.class).filter("q", voca_q).first().now();
		if(voca == null)
			return true;
		else
			return false;
	}

	/**
	 * Find a vocabulary by question
	 * @return the vocabulary match the question
	 */
	@Override
	public Voca findVoca(String voca_q) {
		Voca voca = ofy().load().type(Voca.class).filter("q", voca_q.toLowerCase()).first().now();
		return voca;
	}

	/**
	 * Update an exist vocabulary
	 * @return updated vocabulary, null if update fail
	 */
	@Override
	public Voca updateVoca(Voca voca, boolean isCheck) {
		Voca v = ofy().load().type(Voca.class).id(voca.getGid()).now();
		if(v != null) {
			if(voca.getQ().equals(v.getQ())) {
				if(isCheck) {
					voca.setCheck(true);
				}
				ofy().save().entity(voca);
				return voca;
			}
			else {
				voca.setGid(null);
				return insertVoca(voca);
			}
		}
		return null;
	}

	@Override
	public Integer getTotalVoca() {
		return ofy().load().type(Voca.class).count();
	}

	@Override
	public Integer getTotalPreviewVoca() {
		return ofy().load().type(Voca.class).filter("isCheck", false).count();
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
	    	result.add(v);
	        continu = true;
	    }

	    if (continu) {
	        Cursor cursor = iterator.getCursor();
	        String encodeCursor = cursor.toWebSafeString();
	        VocaList vocaList = new VocaList(result, encodeCursor);
	        return vocaList;
	    }
	    else {
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
		
		Query<Voca> query = ofy().load().type(Voca.class).limit(VocaList.pageSize).filter("isCheck", false);
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
	    }
	    else {
	    	 VocaList vocaList = new VocaList(result, "\\0");
		     return vocaList;
	    }
	}

	/**
	 * Remove a vocabulary
	 */
	@Override
	public void removeVoca(Voca voca) {
		Voca v = ofy().load().type(Voca.class).id(voca.getGid()).now();
		if(v != null)
			ofy().delete().entity(v);
	}

	@Override
	public void saveUser(User user) {
		if(user.getGoogle_id() != null) {
			int i = ofy().load().type(User.class).filter("google_id", user.getGoogle_id()).count();
			if(i == 0)
				ofy().save().entity(user);
		}
		if(user.getFacebook_id() != null) {
			int i = ofy().load().type(User.class).filter("facebook_id", user.getFacebook_id()).count();
			if(i == 0)
				ofy().save().entity(user);
		}
	}

}
