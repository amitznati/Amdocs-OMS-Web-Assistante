package com.amdocs.owa.csv.entities;

import java.util.ArrayList;
import java.util.List;

public class Attribute {
	
	private String name;
	private String value;
	private String type;
	private List<Validation> validationList = new ArrayList<Validation>();
	public Attribute(){};
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public Attribute(String name,String value)
	{
		this.name = name;
		this.value = value;
	}
	public List<Validation> getValidationList() {
		return validationList;
	}
	public void setValidationList(List<Validation> validationList) {
		this.validationList = validationList;
	}


}
