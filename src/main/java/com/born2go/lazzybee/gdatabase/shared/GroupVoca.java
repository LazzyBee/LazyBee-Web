package com.born2go.lazzybee.gdatabase.shared;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity(name = "groupvoca")
@Cache
public class GroupVoca implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// id tự tăng
	@Index
	@Id
	long id;
	// mô tả
	String description;
	// người tạo
	String creator;
	// nhóm các voca
	String listVoca;

	public String getListVoca() {
		return listVoca;
	}
	public void setListVoca(String listVoca) {
		this.listVoca = listVoca;
	}
	public GroupVoca(){
		super();
	}
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
 

	public void getGroupVocaPreview(GroupVoca g){
		this.id = g.getId();
		this.creator = g.getCreator();
		this.description = g.getDescription();
		this.listVoca = g.getListVoca();
		 
	}
}
