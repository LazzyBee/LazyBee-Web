package com.born2go.lazzybee.gdatabase.server;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import com.born2go.lazzybee.gdatabase.clientapi.DataService;
import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Work;
import com.googlecode.objectify.cmd.Query;

@SuppressWarnings("serial")
public class DataServiceImpl extends RemoteServiceServlet implements
		DataService {

	/**
	 * Insert new vocabulary
	 * 
	 * @return inserted vocabulary
	 */
	@Override
	public Voca insertVoca(final Voca voca) {
		// Voca v = ofy().transact(new Work<Voca>() {
		// public Voca run() {
		// if(verifyVoca(voca.getQ())) {
		// Key<Voca> key = ofy().save().entity(voca).now();
		// Voca v = ofy().load().key(key).now();
		// return v;
		// }
		// else
		// return null;
		// }
		// });
		// return v;

		voca.setQ(voca.getQ().toLowerCase());
		if (verifyVoca(voca.getQ())) {
			Key<Voca> key = ofy().save().entity(voca).now();
			Voca v = ofy().load().key(key).now();
			return v;
		} else
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

	@Override
	public Voca getVoca(String voca_q) {
		List<Voca> list_voca = ofy().load().type(Voca.class)
				.filter("field >=", voca_q)
				.filter("field <", voca_q + "\uFFFD").list();
		Voca voca = null;
		if (list_voca != null && !list_voca.isEmpty())
			voca = list_voca.get(0);

		return voca;
	}

}
