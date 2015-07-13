package com.born2go.lazzybee.gdatabase.clientapi;

import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("dataservice")
public interface DataService extends RemoteService{
	
	public void insertVoca(Voca voca);
	
}
