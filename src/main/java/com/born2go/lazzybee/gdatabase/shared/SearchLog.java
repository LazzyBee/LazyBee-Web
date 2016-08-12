package com.born2go.lazzybee.gdatabase.shared;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity(name = "searchlog")
@Cache
public class SearchLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Index
	String q;
	@Index
	int sum = 0;
 
	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public int getSum() {
		return sum;
	}

	public void setSum(int sum) {
		this.sum = sum;
	}

}
