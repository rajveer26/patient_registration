package com.soul.emr.fileupload.controller;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import com.soul.emr.fileupload.service.FileUploadInterf;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.GraphQlRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;

@GraphQlRepository
@DgsComponent
@CrossOrigin
public class FileUploadGraphQLController
{
	private final FileUploadInterf fileUploadInterf;
	
	@Autowired
	public FileUploadGraphQLController(FileUploadInterf fileUploadInterf){
		super();
		this.fileUploadInterf = fileUploadInterf;
	}
	
	@DgsMutation
	public String uploadFile(@InputArgument(value = "file") DataFetchingEnvironment file) throws IOException{

		return fileUploadInterf.uploadFile(file);
	}
}
