package com.XPTO.TECGRAF.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.XPTO.TECGRAF.enuns.DirectoryEnum;
import com.XPTO.TECGRAF.util.StringUtil;

@Service
public class DocumentService {
	
	public boolean registerCheckDigit(MultipartFile file){
		
		try {
			InputStreamReader isr = new InputStreamReader(file.getInputStream());
			BufferedReader bf = new BufferedReader(isr);
			String linha = bf.readLine();
			
			File directory = getDirectory(DirectoryEnum.registryWithDV.getDirectory());
			OutputStream fos;
			fos = new FileOutputStream(directory + "/" + StringUtil.nowInString() + "CheckDigit.txt");
			Writer osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);
			while(linha != null) {
				String linhaDV = generateCheckDigit(linha);
				bw.write(linhaDV);
				bw.newLine();
				linha = bf.readLine();
			}
			bf.close();
			bw.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean checksRegistration(MultipartFile file) {
		try {
			InputStreamReader isr = new InputStreamReader(file.getInputStream());
			BufferedReader bf = new BufferedReader(isr);
			String linha = bf.readLine();
			
			File directory = getDirectory(DirectoryEnum.registryWithValidate.getDirectory());
			OutputStream fos = new FileOutputStream(directory + "/" + StringUtil.nowInString() + "checkedRegistry.txt");
			Writer osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);
			while(linha != null) {
				String linhaDV = checkDigitIsValid(linha);
				bw.write(linha + " " + linhaDV.toString());
				bw.newLine();
				linha = bf.readLine();
			}	
			bf.close();
			bw.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public String generateCheckDigit(String linha) {
		
		char[] chars =linha.toCharArray();
		if(chars.length > 4) {
			return linha + " matrícula mal formatada";
		}
		int result = 0;
		int decrement = new Integer(5);
		for(char c : chars) {
			int digit = Character.getNumericValue(c);
			result += digit * decrement;
			decrement--;
		}
		
		int remainder = result%16;
		String checkDigit = Integer.toHexString(remainder);
		String linhaDV = (linha + "-" + checkDigit.toUpperCase());

		return linhaDV;
	}
	
	public String checkDigitIsValid(String linha) {
		
		String regist = linha.substring(0, linha.indexOf("-"));	
		String dv = generateCheckDigit(regist);		
		if (dv.equals(linha)){
			return "Verdadeiro";
		}		
		return "Falso";
	}
	
	public File getDirectory(String filename) {
		
		File directory = new File(filename);
		if(!directory.exists()){
			directory.mkdir();
		}
		return directory;
	}
	
	public File findLastFile(String directoryInput) throws IOException {
	    
		Path directory = Paths.get(directoryInput);
	    Optional<Path> lastFilePath = Files.list(directory)    // here we get the stream with full directory listing
	    	    .filter(f -> !Files.isDirectory(f))  // exclude subdirectories from listing
	    	    .max(Comparator.comparingLong(f -> f.toFile().lastModified()));
	    
	    if (!lastFilePath.isPresent() )
	    {
	    	return null;	
	    }
	    
	    File file = new File(lastFilePath.orElseGet(null).toString());
	    
	    return file;
	}
}
