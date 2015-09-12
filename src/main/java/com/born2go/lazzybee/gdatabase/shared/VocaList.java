package com.born2go.lazzybee.gdatabase.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VocaList implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Voca> listVoca = new ArrayList<Voca>();
	private String cursorStr;

	public static final int pageSize = 100;
	
	public VocaList() {
		super();
		// TODO Auto-generated constructor stub
	}

	public VocaList(List<Voca> listVoca, String cursorStr) {
		this.listVoca = listVoca;
		this.cursorStr = cursorStr;
	}

	public List<Voca> getListVoca() {
		return listVoca;
	}

	public void setListVoca(List<Voca> listVoca) {
		this.listVoca = listVoca;
	}

	public String getCursorStr() {
		return cursorStr;
	}

	public void setCursorStr(String cursorStr) {
		this.cursorStr = cursorStr;
	}

}
