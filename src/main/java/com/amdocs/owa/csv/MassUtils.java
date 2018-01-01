package com.amdocs.owa.csv;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.amdocs.owa.csv.entities.*;

public class MassUtils {
	
	
	public static MassRequest buildMassRequestFromText(String text)
	{
		if(text == null || text.isEmpty())
			return null;
		MassRequest massRequest = new MassRequest();
	    String[] textAsArray = text.split("\n");
//	    if(!validateMassRequest(textAsArray))
//	    {
//	    	return null;
//	    }

	    massRequest.setHeaderAttributesString(textAsArray[0]);
	    massRequest.setDetailsAttributesString(textAsArray[2]);
	    massRequest.setLinesAttributesString(textAsArray[4]);
	    String[] headerAttrs = textAsArray[0].split(",");
	    String[] headerValues = textAsArray[1].split(",");
	    String[] detailsAttrs = textAsArray[2].split(",");
	    String[] detailsValues = textAsArray[3].split(",");
	    String[] linesAttrs = textAsArray[4].split(",");
	    String[] lines = Arrays.copyOfRange(textAsArray,5,textAsArray.length);
	    for(int i =0 ; i<headerAttrs.length;i++)
	    {
	        if(headerAttrs[i] != null)
	        {
	            massRequest.getMassHeader().getAttributes().add(new Attribute(headerAttrs[i],headerValues[i]));
	        }
	        
	    }
	    for(int i =0 ; i<detailsAttrs.length;i++)
	    {
	        if(detailsAttrs[i] != null)
	        {
	            massRequest.getMassDetails().getAttributes().add(new Attribute(detailsAttrs[i],detailsValues[i]));
	        }                  
	    }

	    for(String line : lines){
	        String[] lineValues = line.split(",");
	        MassRequestLine retLine = new MassRequestLine();
	        for(int i =0 ; i<linesAttrs.length;i++){
	            if(linesAttrs[i] == "Request Line Number" || linesAttrs[i] == "RequestLineNumber"){
	                retLine.setLineNumber(Integer.valueOf(lineValues[i]));
	                
	            }
	            retLine.getAttributes().add(new Attribute(linesAttrs[i],lineValues[i]));
	            
	        }
	        massRequest.getMassLines().add(retLine);
	    }
	    return massRequest;
	}
	
	public static MassRequest buildValidationFile(String requestType)
	{
		if(requestType == null || requestType.isEmpty())
			return null;
		String validationFileAsString = getValidationFileAsStringByType(requestType);
		MassRequest massRequest = new MassRequest();
	    String[] textAsArray = validationFileAsString.split("\n");
	    massRequest.setRequestType(requestType);
	    massRequest.setHeaderAttributesString(textAsArray[0]);
	    massRequest.setDetailsAttributesString(textAsArray[2]);
	    massRequest.setLinesAttributesString(textAsArray[4]);
	    String[] headerAttrs = textAsArray[0].split(",");
	    String[] headerValues = textAsArray[1].split(",");
	    String[] detailsAttrs = textAsArray[2].split(",");
	    String[] detailsValues = textAsArray[3].split(",");
	    String[] linesAttrs = textAsArray[4].split(",");
	    String[] linesValues =  textAsArray[5].split(",");
	   
	    massRequest.getMassHeader().setAttributes(getAttributeWithValidation(headerAttrs,headerValues));
	    massRequest.getMassDetails().setAttributes(getAttributeWithValidation(detailsAttrs, detailsValues));
	    massRequest.getMassLines().add(new MassLine());
	    massRequest.getMassLines().get(0).setAttributes(getAttributeWithValidation(linesAttrs, linesValues));
	  
	    return massRequest;
		
	}
	
	private static List<Attribute> getAttributeWithValidation(String[] lineAttrs, String[] lineValues) {
		List<Attribute> retVal = new ArrayList<Attribute>();
		for (int i = 0; i < lineAttrs.length; i++) {
			if (lineAttrs[i] != null && !lineAttrs[i].trim().isEmpty()) {
				Attribute attribute = new Attribute();
				attribute.setName(lineAttrs[i]);
				String[] allProps = lineValues[i].split(";");
				for (String prop : allProps) {
					String[] singleProp = prop.split("=");
					if ("type".equals(singleProp[0])) {
						attribute.setType(singleProp[1]);
					} else if ("validation".equals(singleProp[0])) {
						String[] allValidations = singleProp[1].split("\\|\\|");
						for (String validationAttr : allValidations) {
							String[] pair = validationAttr.split(":");
							attribute.getValidationList().add(new Validation(pair[0],pair[1]));
						}
					}
				}
				retVal.add(attribute);
			}

		}
		return retVal;
	}


	public static String getRequestTypeFromInputFile(String inputFile) 
	{
		if(inputFile == null || StringUtils.isEmptyOrWhitespace(inputFile))
			return null;
		String[] textAsArray = inputFile.split("\n");
		if(textAsArray == null || textAsArray.length < 5)
			return null;
		String[] headerAttrs = textAsArray[0].split(",");
		String[] headerValues = textAsArray[1].split(",");
		for(int i=0;i<headerAttrs.length;i++)
		{
			if("MassRequestType".equals(headerAttrs[i]) || "Mass Request Type".equals(headerAttrs[i]))
				return headerValues[i];
		}
		return null;
	}
	
	public static List<String> getAllValidationFilesName()
    {
    	File folder = new File(System.getProperty("user.dir") + "/csv-tamplets/");
    	List<String> list = new ArrayList<String>();
    	File[] listOfFiles = folder.listFiles();
   	    for (int i = 0; i < listOfFiles.length; i++) 
   	    {
   	    	if (listOfFiles[i].isFile()) 
   	    	{
   	    		String fullName = listOfFiles[i].getName();
    	        list.add(fullName.substring(0, fullName.length()-4));
    	    }     
    	}
    	return list;
    }
	
	public static String getValidationFileAsStringByType(String requestType)
	{
		File validationF = new File(System.getProperty("user.dir") + "/csv-tamplets/" + requestType+".csv");
		if(validationF.exists())
			try {
				return new String(Files.readAllBytes(validationF.toPath()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return null;
	}

	public static String getInputFileAsString(MultipartFile file) {
		String inputFile = null;
		try {
			inputFile =  new String(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return inputFile;
	}
	
//	private static boolean validateMassRequest(String[] textAsArray) {
//	
//		String requestType = textAsArray[1].split(",")[2];
//		try {
//			//MassRequest validationFile = getValidationFileByType(requestType);
//			Method validate  = MassUtils.class.getMethod("validate"+requestType, String[].class,boolean.class);
//			try {
//				return (Boolean)validate.invoke(textAsArray,true);
//			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} catch (NoSuchMethodException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return false;
//	}

}
