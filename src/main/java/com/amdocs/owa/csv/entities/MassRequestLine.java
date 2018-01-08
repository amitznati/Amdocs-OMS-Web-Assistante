package com.amdocs.owa.csv.entities;

import java.util.ArrayList;
import java.util.List;

public class MassRequestLine extends MassLine {
	public MassRequestLine(){setLineName("Mass Line");}
	
	private List<Component> attributesList = new ArrayList<Component>();
	private String lineNumber;
	
	public String getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}

	public List<Component> getAttributesList() {
		return attributesList;
	}

	public void setAttributesList(List<Component> attributeList) {
		this.attributesList = attributeList;
	}
	
	public MassRequestLine cloneForNewLine(){
		MassRequestLine line = new MassRequestLine();
		List<Attribute> attributes = new ArrayList<Attribute>();
		List<Attribute> thiAttrs = this.getAttributes();
		for(Attribute attr : thiAttrs)
		{
			Attribute newAttr = new Attribute();
			newAttr.setName(attr.getName());
			newAttr.setType(attr.getType());
			newAttr.setValidations(attr.getValidations());
			attributes.add(newAttr);
		}
		line.setAttributes(attributes);
		line.setExceptedAttribute(this.getExceptedAttribute());
		line.setLineName(this.getLineName());
		
		return line;
	}

}
