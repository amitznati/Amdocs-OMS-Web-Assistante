package com.amdocs.owa.web.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.amdocs.owa.csv.MassUtils;

@Controller
public class CSVFileController {
	
	@RequestMapping("/import-file")
    public String importCSVFile(Model model) {
		
        return "csv/import";
    }

    @PostMapping("/validate")
    public String validateCSVFile(@RequestParam("file") MultipartFile file,Model model) {
		
    	
    	String validationFile = "";
    	
    	String inputFile = "";
		try {//C:\\p4\\VFRO\\amitz_AMITZ02_OWA-SWP2_3806\\springboot-adminlte-thymeleaf-master\\src\\main\\resources\\
			inputFile =  new String(file.getBytes());
			//MassRequest inputMassRequest = MassUtils.buildMassRequestFromText(inputFile);
			String requestType = MassUtils.getrequestTypeFromInputFile(inputFile);
			//String validationFileURL = getClass().getClassLoader().getResource("static/csv-tamplets/"+requestType+".csv").getPath().substring(1);
			File validationF = new File(System.getProperty("user.dir") + "/csv-tamplets/" + requestType+".csv");
			if(validationF.exists())
				validationFile = new String(Files.readAllBytes(validationF.toPath()));
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
    	
		model.addAttribute("ValidationFile" , validationFile);
		model.addAttribute("inputFile" , inputFile);
    	//Read more: http://javarevisited.blogspot.com/2015/09/how-to-read-file-into-string-in-java-7.html#ixzz4yoqOHpAJ
        return "csv/validate";
    }
    
    @GetMapping("/validate")
    public String loadCSVFile(Model model) {

        return "csv/load-file";
    }

    @GetMapping("/validation-settings")
    public String validationSettings(Model model) {
    	model.addAttribute("validationFiles", getAllValidationFilesName());
        return "csv/validation-settings";
    }
    
    @PostMapping("/new-validation-file")
    public String newValidationFile(@RequestParam(value = "file",required=true) MultipartFile file, Model model) {
    	
        return "csv/validation-settings";
    }
    
    @PostMapping("/edit-validation-file")
    public String editValidationFile(@RequestParam(value ="request-type",required=true) String requestType, Model model) {

        return "csv/validation-settings";
    }

    private List<String> getAllValidationFilesName()
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
}
