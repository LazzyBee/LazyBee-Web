package com.born2go.lazzybee.gdatabase.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity(name = "voca")
@Cache
public class Voca implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	class VocaDefi {
		String type;
		String defi;
		String figure;
		
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getDefi() {
			return defi;
		}
		public void setDefi(String defi) {
			this.defi = defi;
		}
		public String getFigure() {
			return figure;
		}
		public void setFigure(String figure) {
			this.figure = figure;
		}
	}
	
	@Id
	Long gid;
	
	String voca;
	String spelling;
	String pronounce;
	List<VocaDefi> definations = new ArrayList<VocaDefi>();
	
	public Voca() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getGid() {
		return gid;
	}

	public void setGid(Long gid) {
		this.gid = gid;
	}

	public String getVoca() {
		return voca;
	}

	public void setVoca(String voca) {
		this.voca = voca;
	}

	public String getSpelling() {
		return spelling;
	}

	public void setSpelling(String spelling) {
		this.spelling = spelling;
	}

	public String getPronounce() {
		return pronounce;
	}

	public void setPronounce(String pronounce) {
		this.pronounce = pronounce;
	}

	public List<VocaDefi> getDefinations() {
		return definations;
	}

	public void setDefinations(List<VocaDefi> definations) {
		this.definations = definations;
	}
	
}
