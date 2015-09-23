package com.born2go.lazzybee.gdatabase.server;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.born2go.lazzybee.gdatabase.shared.User;
import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.ObjectifyService;

@SuppressWarnings("serial")
public class DataRegister extends RemoteServiceServlet {
	
	public DataRegister() {
		super();
		ObjectifyService.register(Voca.class);
		ObjectifyService.register(User.class);
		
//		for(int i = 1; i<=4; i++) {
//			Voca v = new Voca();
//			v.setQ(i+"");
//			v.setLevel("1");
//			v.setA("{\"q\":\"a\", \"pronoun\":\"/a\", \"packages\":{}}");
//			v.setPackages(",");
//			v.setCheck(false);
//			v.setL_en("E");
//			v.setL_vn("V");
//			ofy().save().entity(v);
//		}
	}

}
