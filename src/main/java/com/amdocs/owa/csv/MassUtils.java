package com.amdocs.owa.csv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.amdocs.owa.csv.entities.*;
import com.google.gson.Gson;

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
	
//	public static MassRequest buildValidationFile(String requestType)
//	{
//		if(requestType == null || requestType.isEmpty())
//			return null;
//		String validationFileAsString = getValidationFileAsStringByType(requestType);
//		MassRequest massRequest = new MassRequest();
//	    String[] textAsArray = validationFileAsString.split("\n");
//	    massRequest.setRequestType(requestType);
//	    massRequest.setHeaderAttributesString(textAsArray[0]);
//	    massRequest.setDetailsAttributesString(textAsArray[2]);
//	    massRequest.setLinesAttributesString(textAsArray[4]);
//	    String[] headerAttrs = textAsArray[0].split(",");
//	    String[] headerValues = textAsArray[1].split(",");
//	    String[] detailsAttrs = textAsArray[2].split(",");
//	    String[] detailsValues = textAsArray[3].split(",");
//	    String[] linesAttrs = textAsArray[4].split(",");
//	    String[] linesValues =  textAsArray[5].split(",");
//	   
//	    massRequest.getMassHeader().setAttributes(getAttributeWithValidation(headerAttrs,headerValues));
//	    massRequest.getMassDetails().setAttributes(getAttributeWithValidation(detailsAttrs, detailsValues));
//	    massRequest.getMassLines().add(new MassLine());
//	    massRequest.getMassLines().get(0).setAttributes(getAttributeWithValidation(linesAttrs, linesValues));
//	  
//	    return massRequest;
//		
//	}
	
	public static MassRequest getValidationFileByType(String requestType)
	{
		String fullName = System.getProperty("user.dir") + "/csv-tamplets/" + requestType+".json";
		File validationF = new File(fullName);
		
		MassRequest massRequest = null;
		if(validationF.exists())
			try {
				Gson g = new Gson();
				String contents = new String(Files.readAllBytes(Paths.get(fullName)));
				massRequest = g.fromJson(contents, MassRequest.class);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return massRequest;
	}
	
//	private static List<Attribute> getAttributeWithValidation(String[] lineAttrs, String[] lineValues) {
//		List<Attribute> retVal = new ArrayList<Attribute>();
//		for (int i = 0; i < lineAttrs.length; i++) {
//			if (lineAttrs[i] != null && !lineAttrs[i].trim().isEmpty()) {
//				Attribute attribute = new Attribute();
//				attribute.setName(lineAttrs[i]);
//				String[] allProps = lineValues[i].split(";");
//				for (String prop : allProps) {
//					String[] singleProp = prop.split("=");
//					if ("type".equals(singleProp[0])) {
//						attribute.setType(singleProp[1]);
//					} else if ("validation".equals(singleProp[0])) {
//						String[] allValidations = singleProp[1].split("\\|\\|");
//						for (String validationAttr : allValidations) {
//							String[] pair = validationAttr.split(":");
//							attribute.getValidationList().add(new Validation(pair[0],pair[1]));
//						}
//					}
//				}
//				retVal.add(attribute);
//			}
//
//		}
//		return retVal;
//	}


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
    	        list.add(fullName.substring(0, fullName.length()-5));
    	    }     
    	}
    	return list;
    }
	
//	public static String getValidationFileAsStringByType(String requestType)
//	{
//		File validationF = new File(System.getProperty("user.dir") + "/csv-tamplets/" + requestType+".csv");
//		if(validationF.exists())
//			try {
//				System.out.println( validationF.getAbsolutePath());
//				return new String(Files.readAllBytes(validationF.toPath()));
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		return null;
//	}

	public static String getFileAsString(MultipartFile file) {
		String inputFile = null;
		try {
			inputFile =  new String(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return inputFile;
	}

	public static void saveValidationFileFromJsonString(String jsonString) {
		Gson g = new Gson(); 
		MassRequest p = g.fromJson(jsonString, MassRequest.class);
		try {
			FileWriter fileWriter = new FileWriter(System.getProperty("user.dir") + "/csv-tamplets/" + p.getRequestType()+".json");
			fileWriter.write(jsonString);
			fileWriter.flush();
			fileWriter.close();
			//CSVInitializer.validationFilesMap.put(p.getRequestType(), getValidationFileByType(p.getRequestType()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public static MassRequest createNewValidationFile(MultipartFile file) {
		String text = getFileAsString(file);
		String requestType = getRequestTypeFromInputFile(text);
		if(requestType == null)
			return null;
		MassRequest massRequest = new MassRequest();
	    String[] textAsArray = text.split("\n");
	    massRequest.setRequestType(requestType);

	    String[] headerAttrs = textAsArray[0].split(",");
	    String[] detailsAttrs = textAsArray[2].split(",");
	    String[] linesAttrs = textAsArray[4].split(",");
	    
	    createNewLine(massRequest.getMassHeader(), headerAttrs);
	    createNewLine(massRequest.getMassDetails(), detailsAttrs);
	    massRequest.getMassLines().add(new MassRequestLine());
	    createNewLine(massRequest.getMassLines().get(0), linesAttrs);

		return massRequest;
	}

	private static void createNewLine(MassLine line, String[] headerAttrs) {
		for(String attrName : headerAttrs)
	    {
			String realName = attrName != null ? attrName.trim() : null;
			if(!realName.isEmpty())
			{
				line.getExceptedAttribute().add(realName);
		    	Attribute attr = new Attribute(realName,null);
		    	attr.setType("text");
		    	line.getAttributes().add(attr);
			}
	    }
	}

	public static MassRequest buildMassRequestFromFile(MultipartFile file,List<String> errors) {
		String fileAsString = getFileAsString(file);
		String requestType = getRequestTypeFromInputFile(fileAsString);
		MassRequest massRequest = getValidationFileByType(requestType);
		if(massRequest == null ){
			errors.add("Validation File was not found for request type: "+requestType);
			return null;
		}
		addValueToMassRequest(massRequest,fileAsString,errors);
		return massRequest;
	}

	private static void addValueToMassRequest(MassRequest massRequest,String fileAsString,List<String> errors) {
		try{
			if(fileAsString == null || fileAsString.isEmpty()){
				errors.add("File input is empty");
				return;
			}
		    String[] textAsArray = fileAsString.split("\n");
	
		    String[] headerAttrs = textAsArray[0].split(",");
		    String[] headerValues = textAsArray[1].split(",");
		    String[] detailsAttrs = textAsArray[2].split(",");
		    String[] detailsValues = textAsArray[3].split(",");
		    String[] linesAttrs = textAsArray[4].split(",");
		    String[] lines = Arrays.copyOfRange(textAsArray,5,textAsArray.length);
		    validateExpectedAttributeForLine(massRequest.getMassHeader(),headerAttrs,errors);
		    validateExpectedAttributeForLine(massRequest.getMassDetails(),detailsAttrs,errors);
		    validateExpectedAttributeForLine(massRequest.getMassLines().get(0),linesAttrs,errors);
		    if(!errors.isEmpty())
		    	return;
		    addValuesToLine(massRequest.getMassHeader(),headerAttrs,headerValues);
		    addValuesToLine(massRequest.getMassDetails(),detailsAttrs,detailsValues);
		    int lineIndex = 0;
		    for(int i=0 ;i<lines.length;i++)
		    {
		    	String[] lineValues = lines[i].split(",");
		    	addValuesToLine(massRequest.getMassLines().get(lineIndex), linesAttrs, lineValues);
		    	lineIndex++;
		    	if(i!=lines.length-1)
		    	{
		    		massRequest.getMassLines().add(new MassRequestLine());
		    		massRequest.getMassLines().get(lineIndex).setAttributes(massRequest.getMassLines().get(0).getAttributes());
		    		massRequest.getMassLines().get(lineIndex).setExceptedAttribute(massRequest.getMassLines().get(0).getExceptedAttribute());
		    	}
		    }
		    
		}catch(Exception e)
		{
			errors.add(e.getMessage());
			return;
		}
	    
	    
	}

	

	private static void validateExpectedAttributeForLine(MassLine line, String[] recievedAttrs,List<String> errors) {
		List<String> recievedAttrsList = new ArrayList<String>();
		for(String attr : recievedAttrs)
		{
			recievedAttrsList.add(attr.trim());
		}
		List<String> exceptedAttributes = line.getExceptedAttribute();
		for(String attr: exceptedAttributes)
		{
			if(!recievedAttrsList.contains(attr.trim()))
			{
				errors.add("Missing Excepted Attrbite! Attribute: "+attr+ " is missing in section: "+ line.getLineName());
			}
		}
		for(String attr : recievedAttrs)
		{
			if(!attr.trim().isEmpty() && !exceptedAttributes.contains(attr.trim()))
			{
				errors.add("Attrbite Is Not Excepted! Attribute: "+attr+ " should not be in section: "+ line.getLineName());
			}
		}
		
	}
	
	private static void addValuesToLine(MassLine line, String[] lineAttrs, String[] lineValues) {
		HashMap<String,String> nameValuePairs = new HashMap<String,String>();
		for(int i=0;i<lineAttrs.length;i++)
		{
			if(!lineAttrs[i].trim().isEmpty())
				nameValuePairs.put(lineAttrs[i], lineValues[i] != null? lineValues[i].trim():null );
		}
		for(Attribute attr : line.getAttributes())
		{
			if("AttributesList".equals(attr.getName()))
			{
					((MassRequestLine)line).setAttributesList(parsAttributeList(nameValuePairs.get(attr.getName())));
			}
			attr.setValue(nameValuePairs.get(attr.getName()));
		}
	}

	private static List<Component> parsAttributeList(String attrList) {
		List<Component> retList = new ArrayList<Component>();
		if(attrList == null || attrList.trim().isEmpty()) 
			return retList;
	    String[] componentArr = new String[]{attrList};
	    if(attrList.contains(";"))
	        componentArr = attrList.split(";");
	    for(String comp : componentArr){
	        if(comp != null && !comp.trim().isEmpty()){
	            Component component  = new Component();
	            String[] compAttrList = comp.split("\\|\\|");
	            component.setCid(compAttrList[0]);
	            for(int j=1;j<compAttrList.length;j++){
	                String[] attrAndVal = compAttrList[j].split("=");
	                Attribute attribute = new Attribute();
	                attribute.setName(attrAndVal[0]);
	                attribute.setValue(attrAndVal[1]);
	                component.getAttributes().add(attribute);
	            }
	    
	            retList.add(component);
	        }
	        
	        
	    }
	    return retList;
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
