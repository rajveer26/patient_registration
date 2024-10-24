package com.soul.emr.helper;

import com.netflix.graphql.dgs.client.GraphQLResponse;
import com.soul.emr.model.entity.communication.communicationinfodb.CommunicationInfoDB;
import com.soul.emr.model.entity.communication.graphqlentity.CommunicationInfoInput;
import com.soul.emr.model.entity.email.EmailEntity;
import com.soul.emr.model.entity.modelemployee.graphqlentity.RoleInput;
import com.soul.emr.model.entity.modelemployee.registrationdb.RolesDB;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Optional;

public interface HelperInterf
{
	//to generate otp
	Integer generateOTP();
	
	//rest client
	<T> ResponseEntity <Mono <T>> getAPIMono(Class <T> responseType, UriComponentsBuilder uriComponentsBuilder);
	<T> ResponseEntity <Flux <T>> getAPIFlux(Class <T> responseType, UriComponentsBuilder uriComponentsBuilder);
	<T> ResponseEntity<Mono<String>> postAPIMono(Class <T> requestType, Optional <T> bodyType, UriComponentsBuilder uriComponentsBuilder);
	<T> ResponseEntity<Flux<String>> postAPIFlux(Class <T> requestType, Optional <T> bodyType, UriComponentsBuilder uriComponentsBuilder);
	
	//graphQL client
	Mono<GraphQLResponse> postGraphQLAPIMono(String query, UriComponentsBuilder uriComponentsBuilder);
	
	//for validation
	Boolean isValidPhoneNumber(String phoneNumber);
	Boolean isValidEmail(String email);

	//for sending email
	ResponseEntity<Map<String, Object>> sendEmail(EmailEntity email);
	void sendTemplateEmail(EmailEntity emailTemplateEntity);
	EmailEntity generalEmailEntity(String subject, String fromEmail, String toEmail, String messageHeader, String messageFooter, String messageBodyP1, String messageBodyP2, String messageBodyP3);

	//for communication
	CommunicationInfoDB setCommunicationDataMembers(CommunicationInfoDB communicationInfoExisting, CommunicationInfoInput communicationJson);
	
	//for roles
	RolesDB setRoles(RolesDB dbRoles, RoleInput jsonRoles) throws RuntimeException;
			                                                                     //for whoseWhoIsAColumn
	<T> void whoseWhoColumnHelper(T whoseWhoColumn) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
}
