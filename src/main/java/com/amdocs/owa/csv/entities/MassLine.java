package com.amdocs.owa.csv.entities;

import java.util.ArrayList;
import java.util.List;

public class MassLine {
	
	private List<Attribute> attributes = new ArrayList<Attribute>();
	
	private String lineName;
	
	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	

}
