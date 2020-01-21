package com.XPTO.TECGRAF.controller.form;

import org.springframework.web.multipart.MultipartFile;

public class DocumentForm {

	private MultipartFile file;

	public DocumentForm() {
		
	}
	
	public DocumentForm(DocumentForm form) {
		this.file = form.getFile();
	}
	
	

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}
	
	
	
}
