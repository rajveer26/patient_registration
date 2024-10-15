package com.soul.emr.fileupload.service;

import graphql.schema.DataFetchingEnvironment;

import java.io.IOException;

public interface FileUploadInterf
{
	String uploadFile(DataFetchingEnvironment dfe) throws IOException;
}
