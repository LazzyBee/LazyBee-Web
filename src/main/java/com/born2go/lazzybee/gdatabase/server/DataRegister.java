package com.born2go.lazzybee.gdatabase.server;

import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.ObjectifyService;

@SuppressWarnings("serial")
public class DataRegister extends RemoteServiceServlet {
	
	public DataRegister() {
		super();
		ObjectifyService.register(Voca.class);
	}

}
