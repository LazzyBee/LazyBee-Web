package com.born2go.lazzybee.gdatabase.clientapi;

import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.born2go.lazzybee.gdatabase.shared.VocaList;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DataServiceAsync {
	
	void verifyVoca(String voca_q, AsyncCallback<Boolean> callback);
	void insertVoca(Voca voca, AsyncCallback<Voca> callback);
	void findVoca(String voca_q, AsyncCallback<Voca> callback);
	void updateVoca(Voca voca, AsyncCallback<Voca> callback);
	void getTotalVoca(AsyncCallback<Integer> callback);
	void getListVoca(String cursorStr, AsyncCallback<VocaList> callback);
	void removeVoca(Voca voca, AsyncCallback<Void> callback);

}