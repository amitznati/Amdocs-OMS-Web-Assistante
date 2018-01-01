package com.amdocs.owa.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.amdocs.owa.csv.CSVInitializer;
import com.amdocs.owa.csv.MassUtils;
import com.amdocs.owa.csv.entities.MassRequest;

@Controller
public class CSVFileController {
	
	@Value("${oms.csv.attributes.validTypes}")
	private String[] validAttributeTypes;
	@Value("${oms.csv.validation.options}")
	private String[] validationOptions;
	@Value("${oms.csv.validation.validTypes}")
	private String[] validValidationTypes;
	
	@RequestMapping("/import-file")
    public String importCSVFile(Model model) {
		
        return "csv/import";
    }

    @PostMapping("/validate")
    public String validateCSVFile(@RequestParam("file") MultipartFile file,Model model) 
    {
    	String inputFile = MassUtils.getInputFileAsString(file);
    	String requestType = MassUtils.getRequestTypeFromInputFile(inputFile);
    	String validationFile = MassUtils.getValidationFileAsStringByType(requestType);
    	
		model.addAttribute("ValidationFile" , validationFile);
		model.addAttribute("inputFile" , inputFile);

        return "csv/validate";
    }
    
    @GetMapping("/validate")
    public String loadCSVFile(Model model) {

        return "csv/load-file";
    }

    @GetMapping("/validation-settings")
    public String validationSettings(Model model) {
    	model.addAttribute("validationFiles", MassUtils.getAllValidationFilesName());
        return "csv/validation-settings";
    }
    
    @PostMapping("/new-validation-file")
    public String newValidationFile(@RequestParam(value = "file",required=true) MultipartFile file, Model model) {
    	
        return "csv/validation-settings";
    }
    
    @PostMapping("/edit-validation-file")
    public String editValidationFile(@RequestParam(value ="request-type",required=true) String requestType, Model model) {
    	
    	MassRequest validationFile = CSVInitializer.validationFilesMap.get(requestType);
    	model.addAttribute("validationFile", validationFile);
    	model.addAttribute("validAttributeTypes", validAttributeTypes.clone());
    	model.addAttribute("validationOptions", validationOptions.clone());
    	model.addAttribute("validValidationTypes", validValidationTypes.clone());
        return "csv/validation-file-edit";
    }

    
}
