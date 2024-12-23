package com.soul.patient.fileupload.service;

import com.soul.patient.dao.PatientDaoInterf;
import com.soul.patient.helper.HelperInterf;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service("fileUploadService")
public class FileUploadService implements FileUploadInterf
{
	private final PatientDaoInterf patientDaoInterf;
	private final HelperInterf helperInterf;
	
	@Autowired
	public FileUploadService(PatientDaoInterf patientDaoInterf, HelperInterf helperInterf){
		super();
		this.patientDaoInterf = patientDaoInterf;
		this.helperInterf     = helperInterf;
	}
	
	@Override
	public String uploadFile(DataFetchingEnvironment dfe) throws IOException{
		
		MultipartFile file = dfe.getArgument("file");
		
		// Save file to local storage or process as needed
		//File convertFile = new File("uploaded_files/" + file.getOriginalFilename());
		//convertFile.getParentFile().mkdirs();
		//FileOutputStream fout = new FileOutputStream(convertFile);
		//IOUtils.copy(file.getInputStream(), fout);
		//fout.close();
		return "File uploaded successfully: " + file.getOriginalFilename();
	}
	
	
	
}
