package com.born2go.lazzybee.gdatabase.shared;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;

@Entity(name = "vocabulary_preview")
@Cache
public class VocaPreview extends Voca implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String user_comment;

	public VocaPreview() {
		super();
	}

	public String getUser_comment() {
		return user_comment;
	}

	public void setUser_comment(String user_comment) {
		this.user_comment = user_comment;
	}

	@Override
	public boolean equals(Object obj) {
		VocaPreview v = (VocaPreview) obj;
		if (v.getQ().equals(q))
			return true;
		else
			return false;
	}

}
