package com.XPTO.TECGRAF.enuns;

public enum DirectoryEnum {

	DOC("/src/main/resources/static/doc"),
	registryWithDV("/src/main/resources/static/doc/registryWithDV"),
	registryWithValidate("/src/main/resources/static/doc/registryWithValidate");
	
	
	private String directory;
	
	DirectoryEnum(String directory) {
		this.directory = directory;
	}
	
	
	public String getDirectory() {
		return System.getProperty("user.dir") + directory;
	}

}



