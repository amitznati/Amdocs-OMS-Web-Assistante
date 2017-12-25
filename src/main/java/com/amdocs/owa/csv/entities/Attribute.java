package com.amdocs.owa.csv.entities;

import java.util.HashMap;

public class Attribute {
	
	private String name;
	private String value;
	private String type;
	private HashMap<String,String> validationMap;
	
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
	public HashMap<String,String> getValidationMap() {
		return validationMap;
	}
	public void setValidationMap(HashMap<String,String> validationMap) {
		this.validationMap = validationMap;
	}

}
