package com.soul.emr.auth.oidcauth;

import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;
import com.soul.emr.model.entity.modelemployee.registrationdb.UserCredentialsDB;
import com.soul.emr.model.repository.jparepository.registrationrepository.UserCredentialsRepository;
import com.soul.emr.dao.PatientDaoInterf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomOidcUserService extends OidcUserService {
	
	private final UserCredentialsRepository userRepository;
	
	private final PatientDaoInterf patientDaoInterf;
	
	
	@Autowired
	public CustomOidcUserService(UserCredentialsRepository userRepository, PatientDaoInterf patientDaoInterf){
		super();
		this.userRepository   = userRepository;
		this.patientDaoInterf = patientDaoInterf;
	}
	
	@Override
	public OidcUser loadUser(OidcUserRequest userRequest) {
		OidcUser oidcUser = super.loadUser(userRequest);
		String email = oidcUser.getAttribute("email");
		
		UserCredentialsDB user = userRepository.findByUsername(email).orElseThrow(()-> new DgsEntityNotFoundException("NO_USER_FOUND_PLEASE_CONTACT_TO_SUPPORT_TEAM"));
		
		// Convert the RolesDB objects to SimpleGrantedAuthority objects
		List <GrantedAuthority> authorities = user.getEmployeeInfo().getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getRoleMaster().getRoleMasterName())).collect(Collectors.toList());
		
		return new CustomOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo(), email);
	}
}