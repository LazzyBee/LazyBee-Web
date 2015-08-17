package com.born2go.lazzybee.gdatabase.server;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;

import com.born2go.lazzybee.gdatabase.clientapi.DataService;
import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Key;

@SuppressWarnings("serial")
public class DataServiceImpl extends RemoteServiceServlet implements DataService{

	/**
	 * Insert new vocabulary
	 * @return inserted vocabulary
	 */
	@Override
	public Voca insertVoca(final Voca voca) {
		voca.setQ(voca.getQ().toLowerCase());
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
	public Voca updateVoca(Voca voca) {
		Voca v = ofy().load().type(Voca.class).id(voca.getGid()).now();
		if(v != null) {
			if(voca.getQ().equals(v.getQ())) {
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

	/**
	 * Get all vocabulary
	 */
	@Override
	public List<Voca> getListVoca() {
		List<Voca> list_voca = ofy().load().type(Voca.class).list();
		List<Voca> result = new ArrayList<Voca>();
		result.addAll(list_voca);
		return result;
	}

}
