package com.soul.patient.fileupload.service;

import graphql.schema.DataFetchingEnvironment;

import java.io.IOException;

public interface FileUploadInterf
{
	String uploadFile(DataFetchingEnvironment dfe) throws IOException;
}
