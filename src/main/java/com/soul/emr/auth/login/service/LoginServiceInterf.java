package com.soul.emr.auth.login.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

public interface LoginServiceInterf
{
	Map <String, Object> login(String username, String password);
	Map<String, Object> generateRefreshToken(String token) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;
	
}
