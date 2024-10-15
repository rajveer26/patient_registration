package com.soul.emr.auth.login.controller;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import com.soul.emr.auth.login.service.LoginServiceInterf;
import com.soul.emr.doctor.service.EmployeeServiceInterf;
import com.soul.emr.model.entity.modelemployee.graphqlentity.UserCredentialsInput;
import com.soul.emr.model.entity.modelemployee.registrationdb.EmployeeInfoDB;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.GraphQlRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

@GraphQlRepository
@DgsComponent
@CrossOrigin
public class GraphQLAuthController
{
	private final LoginServiceInterf loginServiceInterf;
	private final EmployeeServiceInterf employeeServiceInterf;

	@Autowired
	public GraphQLAuthController(LoginServiceInterf loginServiceInterf, EmployeeServiceInterf employeeServiceInterf){
		super();
		this.loginServiceInterf    = loginServiceInterf;
		this.employeeServiceInterf = employeeServiceInterf;
	}

	@DgsMutation
	public Map <String, Object> login(@InputArgument(value = "userCredentialsInput") @NotNull UserCredentialsInput userCredentialsInput)
	{
		return loginServiceInterf.login(userCredentialsInput.getUsername(), userCredentialsInput.getPassword());
	}

	@DgsMutation
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public Map <String, Object> refreshToken(@InputArgument(value = "token") String token) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException{
		return loginServiceInterf.generateRefreshToken(token);
	}
	
	@DgsQuery
	//@PreAuthorize("hasAnyAuthority('ADMIN')")
	public EmployeeInfoDB getUserInfo(@InputArgument(value = "userName") String userName)
	{
		return employeeServiceInterf.fetchUserDetails(userName);
	}

}
