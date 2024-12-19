package com.soul.emr.auth.login.service;

import com.google.common.base.Strings;
import com.soul.emr.auth.security.JwtTokenProvider;
import com.soul.emr.dao.PatientDaoInterf;
import com.soul.emr.model.entity.modelemployee.registrationdb.UserCredentialsDB;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service("loginService")
public class LoginService implements LoginServiceInterf
{

	private final JwtTokenProvider jwtTokenProvider;
	private final PatientDaoInterf patientDaoInterf;
	private final AuthenticationManager authenticationManager;
	
	
	@Autowired
	public LoginService(JwtTokenProvider jwtTokenProvider, PatientDaoInterf patientDaoInterf, AuthenticationManager authenticationManager){
		super();
		this.jwtTokenProvider      = jwtTokenProvider;
		this.patientDaoInterf      = patientDaoInterf;
		this.authenticationManager = authenticationManager;
	}
	
	//logger
	private final Logger logger = LogManager.getLogger(LoginService.class);
	
	@Override
	public Map <String, Object> login(String username, String password)
	{
		HashMap <String, Object> response = new HashMap<>();
		try{
			
			Optional<UserCredentialsDB> userCredentialsDB = patientDaoInterf.getUserCredentials(username);
			
			if(userCredentialsDB.isPresent()){
				
				Authentication authentication = authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(username, password)
				                                                                  );
				SecurityContextHolder.getContext().setAuthentication(authentication);
				
				String token = jwtTokenProvider.generateToken(authentication);
				
				response.put("token", token);
				
				return response;
			}
			
			else
			{
				throw new RuntimeException("INVALID USERNAME OR PASSWORD");
			}
		
		}
		
		catch(Exception e){
			
			System.out.println("exception " + e.getMessage());
			logger.error(e.fillInStackTrace());
			logger.catching(e);
			
			throw new RuntimeException(e.getMessage());
			
		}
	}
	
	@Override
	public Map<String, Object> generateRefreshToken(String token) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException{
		HashMap <String, Object> response = new HashMap<>();
		
		String newToken = jwtTokenProvider.generateNewAccessToken(token);
		
		if(!Strings.isNullOrEmpty(newToken)) {
			response.put("token", newToken);
			
			return response;
		}
		else
		{
			throw new RuntimeException();
		}
	}

}
