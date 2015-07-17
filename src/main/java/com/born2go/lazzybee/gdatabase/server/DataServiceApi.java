package com.born2go.lazzybee.gdatabase.server;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;
import com.born2go.lazzybee.gdatabase.clientapi.DataService;
import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class DataServiceApi extends RemoteServiceServlet implements DataService {

	@Override
	public void insertVoca(Voca voca) {
		ofy().save().entity(voca);
	}

	@Override
	public Voca findVoca(String value) {
		List<Voca> list = ofy().load().type(Voca.class)
				.filter("voca >=", value).filter("voca <=", value + "\uFFFD").list();
		List<Voca> result = new ArrayList<Voca>();
		result.addAll(list);
		if (result != null && !result.isEmpty()) {
			for (Voca voca : result) {
				System.out.println("voca: " + voca.getVoca());
			}
		}
		return null;
	}

}
