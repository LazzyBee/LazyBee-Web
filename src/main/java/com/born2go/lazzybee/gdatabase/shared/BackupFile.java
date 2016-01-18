package com.born2go.lazzybee.gdatabase.shared;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity(name = "backup_file")
@Cache
public class BackupFile implements Serializable, IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	String id; //device id last 6 character

	String blob_key;

	public BackupFile() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBlob_key() {
		return blob_key;
	}

	public void setBlob_key(String blob_key) {
		this.blob_key = blob_key;
	}

}
