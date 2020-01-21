package com.XPTO.TECGRAF.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.XPTO.TECGRAF.controller.form.DocumentForm;
import com.XPTO.TECGRAF.enuns.DirectoryEnum;
import com.XPTO.TECGRAF.service.DocumentService;

@Controller
@RequestMapping("/documento")
public class DocumentController {

	@Autowired
	private DocumentService docServ;
	
	@GetMapping("/geraDV")
	public String generateDvForm(DocumentForm form, Model model){
		
		model.addAttribute("document", form);
		return "geraDV";
	}
	
	@GetMapping("/verificaDV")
	public String dvIsValid(DocumentForm form, Model model){
		
		model.addAttribute("document", form);
		return "verificaDV";
	}
	
	@PostMapping("/validar")
	public String generatesCeckDigit(DocumentForm form, RedirectAttributes attr) throws IOException{
		
		boolean result = docServ.registerCheckDigit(form.getFile());
		if(!result) {
			String error = "Ocorreu algum erro ao ler o arquivo, favor, verifique se o mesmo está correto.";
			attr.addFlashAttribute("error", error);
			attr.addFlashAttribute(new DocumentForm());
			return "redirect:/geraDV";
		}
		
		String success = "Documento lido com sucesso. Por favor, verifique o resultado.";
		attr.addFlashAttribute("success", success);
		attr.addFlashAttribute("matriculaDV", "permite download");
		return "redirect:/"; 
	}
		
	@PostMapping("/verificar")
	public String verifica(DocumentForm form, RedirectAttributes attr) throws IOException{
		
		boolean result= docServ.checksRegistration(form.getFile());
		if(!result) {
			String error = "Ocorreu algum erro ao ler o arquivo, favor, verifique se o mesmo está correto.";
			attr.addFlashAttribute("error", error);
			attr.addFlashAttribute(new DocumentForm());
			return "redirect:/verificaDV";
		}
		
		String success = "Documento lido com sucesso. Por favor, verifique o resultado.";
		attr.addFlashAttribute("success", success);
		attr.addFlashAttribute("validacaoDV", "permite download");

		return "redirect:/"; 
	}

	
	@GetMapping("/Download/matriculaDV")
	@ResponseBody
	public FileSystemResource donwloadFile() throws IOException {
		
		File file = docServ.findLastFile(DirectoryEnum.registryWithDV.getDirectory());
	    return new FileSystemResource(file);
	}
	
	@GetMapping("/Download/validacaoDV")
	@ResponseBody
	public FileSystemResource donwloadFileCheckRegistration() throws IOException {
		
		File file = docServ.findLastFile(DirectoryEnum.registryWithValidate.getDirectory());
	    return new FileSystemResource(file);
	} 
}
