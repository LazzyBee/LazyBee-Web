package com.born2go.lazzybee.gdatabase.shared;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity(name = "vocabulary")
@Cache
public class Voca implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	Long gid;

	@Index
	String q;
	String a;
	@Index
	int level;
	String packages;
	String l_en;
	String l_vn;
	String note;
	String creator;
	String user_comment;
	
	//@Index
	boolean isCheck = false;

	public Voca() {
		super();
	}

	public Long getGid() {
		return gid;
	}

	public void setGid(Long gid) {
		this.gid = gid;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getPackages() {
		return packages;
	}

	public void setPackages(String packages) {
		this.packages = packages;
	}

	public String getL_en() {
		return l_en;
	}

	public void setL_en(String l_en) {
		this.l_en = l_en;
	}

	public String getL_vn() {
		return l_vn;
	}

	public void setL_vn(String l_vn) {
		this.l_vn = l_vn;
	}
	
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getUser_comment() {
		return user_comment;
	}

	public void setUser_comment(String user_comment) {
		this.user_comment = user_comment;
	}
	
	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}
	
	public void getVocaPreviewContent(VocaPreview v) {
		this.gid = v.getGid();
		this.q = v.getQ();
		this.a = v.getA();
		this.level = v.getLevel();
		this.packages = v.getPackages();
		this.l_en = v.getL_en();
		this.l_vn = v.getL_vn();
		this.note = v.getNote();
		this.creator = v.getCreator();
		this.user_comment = v.getUser_comment();
	}

	@Override
	public boolean equals(Object obj) {
		Voca v = (Voca) obj;
		if(v.getQ().equals(q))
			return true;
		else
			return false;
	}

}
