package com.born2go.lazzybee.gdatabase.server;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.born2go.lazzybee.gdatabase.clientapi.DataService;
import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class DataServiceApi extends RemoteServiceServlet implements DataService{

	@Override
	public void insertVoca(Voca voca) {
		ofy().save().entity(voca);
	}

}
