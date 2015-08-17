package com.born2go.lazzybee.gdatabase.clientapi;

import java.util.List;

import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DataServiceAsync {

	void verifyVoca(String voca_q, AsyncCallback<Boolean> callback);

	void insertVoca(Voca voca, AsyncCallback<Voca> callback);
	void findVoca(String voca_q, AsyncCallback<Voca> callback);
	void updateVoca(Voca voca, AsyncCallback<Voca> callback);
	void getListVoca(AsyncCallback<List<Voca>> callback);

	 

}
