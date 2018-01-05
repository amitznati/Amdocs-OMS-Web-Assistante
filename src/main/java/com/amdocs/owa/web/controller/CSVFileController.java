package com.amdocs.owa.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	
	@RequestMapping("import-file")
    public String importCSVFile(Model model) {
		
        return "csv/import";
    }

    @PostMapping("validate")
    public String validateCSVFile(@RequestParam("file") MultipartFile file,Model model) 
    {
    	List<String> errorsHolder = new ArrayList<String>();
    	MassRequest inputFile = MassUtils.buildMassRequestFromFile(file,errorsHolder);
		model.addAttribute("inputFile" , inputFile);
		model.addAttribute("errors" , errorsHolder);
        return "csv/validate";
    }
    
    @GetMapping("load-file")
    public String loadCSVFile(Model model) {

        return "csv/load-file";
    }

    @GetMapping("validation-settings")
    public String validationSettings(Model model) {
    	model.addAttribute("validationFiles", MassUtils.getAllValidationFilesName());
        return "csv/validation-settings";
    }
    
    @PostMapping("new-validation-file")
    public String newValidationFile(@RequestParam(value = "file",required=true) MultipartFile file, Model model,RedirectAttributes redirectAttributes) {
    	MassRequest validationFile = MassUtils.createNewValidationFile(file);
    	model.addAttribute("validationFile", validationFile);
    	model.addAttribute("validAttributeTypes", validAttributeTypes.clone());
    	model.addAttribute("validationOptions", validationOptions.clone());
    	model.addAttribute("validValidationTypes", validValidationTypes.clone());
        return "csv/validation-file-edit";
    }
    
    @PostMapping("edit-validation-file")
    public String editValidationFile(@RequestParam(value ="request-type",required=true) String requestType, Model model) {
    	
    	MassRequest validationFile = MassUtils.getValidationFileByType(requestType);
    	model.addAttribute("validationFile", validationFile);
    	model.addAttribute("validAttributeTypes", validAttributeTypes.clone());
    	model.addAttribute("validationOptions", validationOptions.clone());
    	model.addAttribute("validValidationTypes", validValidationTypes.clone());
        return "csv/validation-file-edit";
    }
    
    @PostMapping("save-validation-file")
    public String saveValidationFile(@RequestParam(value= "file",required =true) String file,Model model){
    	MassUtils.saveValidationFileFromJsonString(file);
    	return "redirect:" + "/validation-settings";
    }

    
}
