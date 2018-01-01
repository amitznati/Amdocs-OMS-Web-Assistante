package com.amdocs.owa.csv;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.amdocs.owa.csv.entities.MassRequest;


public class CSVInitializer {
	
	public static HashMap<String,MassRequest> validationFilesMap = new HashMap<String,MassRequest>();
	static {
		List<String> allTypes = MassUtils.getAllValidationFilesName();
		for(String type : allTypes)
			validationFilesMap.put(type, MassUtils.buildValidationFile(type));
	}
	
	@Value("${oms.csv.attributes.validTypes}")
	private String[] validAttributeTypes;
	@Value("${oms.csv.validation.options}")
	private String[] validationOptions;
	@Value("${oms.csv.validation.validTypes}")
	private String[] validValidationTypes;
	public String[] getValidAttributeTypes() {
		return validAttributeTypes;
	}
	public void setValidAttributeTypes(String[] validAttributeTypes) {
		this.validAttributeTypes = validAttributeTypes;
	}
	public String[] getValidationOptions() {
		return validationOptions;
	}
	public void setValidationOptions(String[] validationOptions) {
		this.validationOptions = validationOptions;
	}
	public String[] getValidValidationTypes() {
		return validValidationTypes;
	}
	public void setValidValidationTypes(String[] validValidationTypes) {
		this.validValidationTypes = validValidationTypes;
	}
	
	
	

}
