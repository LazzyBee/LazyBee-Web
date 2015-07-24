package com.born2go.lazzybee.gdatabase.clientapi;

import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("dataservice")
public interface DataService extends RemoteService{
	
	public boolean verifyVoca(String voca_q);
	public Voca insertVoca(Voca voca);
	public Voca findVoca(String voca_q);
	public Voca updateVoca(Voca voca);
	
}
