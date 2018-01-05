package com.amdocs.owa.csv.entities;

import java.util.ArrayList;
import java.util.List;

public class Component {
	
	private String cid;
	private List<Attribute> attributes = new ArrayList<Attribute>();
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public List<Attribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

}
