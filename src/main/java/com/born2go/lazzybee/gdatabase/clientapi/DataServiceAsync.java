package com.born2go.lazzybee.gdatabase.clientapi;

import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DataServiceAsync {

	void verifyVoca(String voca_q, AsyncCallback<Boolean> callback);

	void insertVoca(Voca voca, AsyncCallback<Voca> callback);

	void getVoca(String voca_q, AsyncCallback<Voca> callback);

}
