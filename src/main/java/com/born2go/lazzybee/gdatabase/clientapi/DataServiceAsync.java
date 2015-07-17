package com.born2go.lazzybee.gdatabase.clientapi;

import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DataServiceAsync {

	void insertVoca(Voca voca, AsyncCallback<Void> callback);

	void findVoca(String value, AsyncCallback<Voca> callback);

}
