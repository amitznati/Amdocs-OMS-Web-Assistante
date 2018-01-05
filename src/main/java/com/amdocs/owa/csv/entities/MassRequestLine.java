package com.amdocs.owa.csv.entities;

import java.util.ArrayList;
import java.util.List;

public class MassRequestLine extends MassLine {
	public MassRequestLine(){setLineName("Mass Line");}
	
	private List<Component> attributesList = new ArrayList<Component>();
	private int lineNumber;
	
	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public List<Component> getAttributesList() {
		return attributesList;
	}

	public void setAttributesList(List<Component> attributeList) {
		this.attributesList = attributeList;
	}

}
