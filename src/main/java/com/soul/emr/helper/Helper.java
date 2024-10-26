package com.soul.emr.helper;

import com.google.common.base.Strings;
import com.netflix.graphql.dgs.client.*;
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;
import com.soul.emr.auth.security.AuthAbstract;
import com.soul.emr.dao.EmrDaoInterf;
import com.soul.emr.masters.service.MastersServiceInterf;
import com.soul.emr.model.entity.communication.communicationinfodb.CommunicationInfoDB;
import com.soul.emr.model.entity.communication.graphqlentity.CommunicationInfoInput;
import com.soul.emr.model.entity.email.EmailEntity;
import com.soul.emr.model.entity.masterentity.masterdb.RoleMasterDB;
import com.soul.emr.model.entity.modelemployee.graphqlentity.RoleInput;
import com.soul.emr.model.entity.modelemployee.registrationdb.RolesDB;
import jakarta.mail.internet.MimeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.intellij.lang.annotations.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("helper")
public class Helper implements HelperInterf
{
	
	//logger
	private final Logger logger = LogManager.getLogger(Helper.class);
	private final WebClient webClient;
	private final AuthAbstract authAbstract;
	private final JavaMailSender emailSender;
	private final TemplateEngine templateEngine;
	private final Environment environment;
	private final MastersServiceInterf mastersServiceInterf;
	private final EmrDaoInterf emrDaoInterf;
	
	
	@Autowired
	public Helper(WebClient webClient, AuthAbstract authAbstract, JavaMailSender emailSender, TemplateEngine templateEngine, Environment environment, MastersServiceInterf mastersServiceInterf, EmrDaoInterf emrDaoInterf){
		super();
		this.webClient        = webClient;
		this.authAbstract     = authAbstract;
		this.emailSender = emailSender;
		this.templateEngine = templateEngine;
		this.environment      = environment;
		this.mastersServiceInterf = mastersServiceInterf;
		this.emrDaoInterf = emrDaoInterf;
	}
	
	// Method to Generate a random OTP of 6 Digits
	@Override
	public Integer generateOTP() {
		try {
			System.out.println("otp started");
			SecureRandom random = new SecureRandom();
			return random.nextInt(900000) + 100000; // generates a random number between 100000 and 999999
		}
		
		catch (Exception e) {
			logger.error(e.fillInStackTrace());
			logger.catching(e);
			
			throw new RuntimeException(e.getMessage());
			
		}
	}
	
	// Method for Validating Phone Number Using Regex
	@Override
	public Boolean isValidPhoneNumber(String phoneNumber) {
		
		String pattern = "^\\+91\\d{10}|0?\\d{10}$";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(phoneNumber);
		
		return m.matches();
		
	}
	
	//method for validating email Id using Regex
	@Override
	public Boolean isValidEmail(String email) {
		
		String regex = "^[A-Za-z0-9]+(?:[._%+-](?![._%+-])[A-Za-z0-9]+)*@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(email);
		
		return m.matches();
	}
	
	//client for getAPI Mono
	@Override
	public <T> ResponseEntity <Mono <T>> getAPIMono(Class <T> responseType, UriComponentsBuilder uriComponentsBuilder){
		
		Mono <T> response;
		
		try{
			
			// Set up headers
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", authAbstract.getBearerToken());
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			
			//building request using webClient, sending request and receiving response
			response = webClient.get().uri(new URI(uriComponentsBuilder.encode().build().toUriString()))
					.headers(httpHeaders -> httpHeaders.addAll(headers))
					.retrieve()
					.bodyToMono(responseType);
			
			return ResponseEntity.ok(response);
		}
		
		catch(Exception e){
			// Logging exception
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			
			// Returning exception as a response
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//client for get api flux
	@Override
	public <T> ResponseEntity <Flux <T>> getAPIFlux(Class <T> responseType, UriComponentsBuilder uriComponentsBuilder){
		
		Flux <T> response;
		
		try{
			
			// Set up headers
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", authAbstract.getBearerToken());
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			
			//building request using webClient, sending request and receiving response
			response = webClient.get().uri(new URI(uriComponentsBuilder.encode().build().toUriString()))
					.headers(httpHeaders -> httpHeaders.addAll(headers))
					.retrieve()
					.bodyToFlux(responseType);
			
			return ResponseEntity.ok(response);
		}
		
		catch(Exception e){
			// Logging exception
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			
			// Returning exception as a response
			throw new RuntimeException(e.getMessage());		}
	}
	
	//method to set whose who is a column
	private <T> void settingWhoseWhoIsAColumn(T whoseWhoColumn, String setNameOfColumn, String getNameOfColumn){
		try{
			//for getting value
			Method getValue = whoseWhoColumn.getClass().getSuperclass().getDeclaredMethod(getNameOfColumn);
			
			//invoking getUpdatedBy bypassing an object to read the value
			Object updatedValue = getValue.invoke(whoseWhoColumn);
			
			//checking if the whoseWhoColumn object can access the method
			if (getValue.canAccess(whoseWhoColumn)) {
				//checking if getCreatedByValue is null or not
				if (!Objects.isNull(updatedValue)) {
					
					//if updated value is an instanceOf long
					if (updatedValue instanceof Long) {
						//for setUpdatedBy
						Method setUpdatedBy = whoseWhoColumn.getClass().getSuperclass().getDeclaredMethod(setNameOfColumn, Long.class);
						
						//setting accessible for setUpdatedBy
						setUpdatedBy.setAccessible(true);
						
						//casting the object in Long
						Long castedValue = (Long) (updatedValue);
						
						//invoking getUpdatedBy bypassing an object and value to write the value
						setUpdatedBy.invoke(whoseWhoColumn, castedValue);
					} else if (updatedValue instanceof LocalDateTime) {
						//for setUpdatedBy
						Method setUpdatedBy = whoseWhoColumn.getClass().getSuperclass().getDeclaredMethod(setNameOfColumn, LocalDateTime.class);
						
						//setting accessible for setUpdatedBy
						setUpdatedBy.setAccessible(true);
						
						//invoking getUpdatedBy bypassing an object and value to write the value
						setUpdatedBy.invoke(whoseWhoColumn, LocalDateTime.now());
					} else
						// throwing run-time exception
						throw new RuntimeException("instance of not covered");
				}
			}
		}
		catch(NoSuchMethodException | InvocationTargetException | IllegalAccessException e)
		{
			// Logging exception
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//whose who is a column helper
	@Override
	public <T> void whoseWhoColumnHelper(T whoseWhoColumn) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException{
		
		//for getting value
		Method getValue = whoseWhoColumn.getClass().getSuperclass().getDeclaredMethod("getCreatedBy");
		
		//invoking getUpdatedBy bypassing an object to read the value
		Object updatedValue = getValue.invoke(whoseWhoColumn);
		if (Objects.isNull(updatedValue)) this.settingWhoseWhoIsAColumn(whoseWhoColumn, "setCreatedBy", "getCreatedBy");
		
		this.settingWhoseWhoIsAColumn(whoseWhoColumn, "setUpdatedBy", "getUpdatedBy");
		this.settingWhoseWhoIsAColumn(whoseWhoColumn, "setUpdationTimeStamp", "getUpdationTimeStamp");
		
	}
	
	//client for post api mono
	@Override
	public <T> ResponseEntity<Mono<String>> postAPIMono(Class <T> requestType, Optional <T> bodyType, UriComponentsBuilder uriComponentsBuilder){
		
		Mono<String> responseEntity;
		try{
			
			WebClient.RequestBodySpec requestSpec = this.postAPI(requestType, bodyType, uriComponentsBuilder);
			
			// Add request body if present
			bodyType.ifPresent(jsonBody -> requestSpec.body(BodyInserters.fromValue(jsonBody)));
			
			// Send request and receive response
			responseEntity = requestSpec.retrieve().bodyToMono(String.class); // Block to wait for the response
			
			// Returning response
			return ResponseEntity.ok(responseEntity);
		}
		catch(Exception e){
			// Logging exception
			logger.catching(e);
			logger.error(e);
			
			// Returning exception as a response
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//client for post api flux
	@Override
	public <T> ResponseEntity<Flux<String>> postAPIFlux(Class <T> requestType, Optional <T> bodyType, UriComponentsBuilder uriComponentsBuilder){
		
		Flux<String> responseEntity;
		try{
			
			WebClient.RequestBodySpec requestSpec = this.postAPI(requestType, bodyType, uriComponentsBuilder);
			
			// Send request and receive response
			responseEntity = requestSpec.retrieve().bodyToFlux(String.class); // Block to wait for the response
			
			// Returning response
			return ResponseEntity.ok(responseEntity);
		}
		catch(Exception e){
			// Logging exception
			logger.catching(e);
			logger.error(e);
			
			// Returning exception as a response
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public <T> WebClient.RequestBodySpec postAPI(Class <T> requestType, Optional <T> bodyType, UriComponentsBuilder uriComponentsBuilder){
		try{
			// Set up headers
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", authAbstract.getBearerToken());
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			// Build the request using webClient
			WebClient.RequestBodySpec requestSpec = webClient.post().uri(new URI(uriComponentsBuilder.encode().build().toUriString())).headers(httpHeaders -> httpHeaders.addAll(headers));
			
			// Add request body if present
			bodyType.ifPresent(jsonBody -> requestSpec.body(BodyInserters.fromValue(jsonBody)));
			
			return requestSpec;
		}
		catch(Exception e)
		{
			logger.error(e.fillInStackTrace());
			logger.catching(e);
			
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//POST API for MONO type objects with graphQL client
	@Override
	public Mono<GraphQLResponse> postGraphQLAPIMono(@Language("graphql") String query, UriComponentsBuilder uriComponentsBuilder){
		
		try
		{
			// Set up headers
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", authAbstract.getBearerToken());
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			//creating and configure WebClient
			WebClient webClient = WebClient.builder()
					.baseUrl(uriComponentsBuilder.encode().build().toUriString())
					.defaultHeaders(httpHeaders -> httpHeaders.addAll(headers))
					.build();
			
			
			//creating graphQL client using webClient
			WebClientGraphQLClient graphQLClient = MonoGraphQLClient.createWithWebClient(webClient);
			
			//The GraphQLResponse contains data and errors.
			return graphQLClient.reactiveExecuteQuery(query);
			
			
		}
		catch(Exception e){
			// Logging exception
			logger.catching(e);
			logger.error(e);
			
			// Returning exception as a response
			throw new RuntimeException(e.getMessage());
		}
	}
	
	@Override
	public ResponseEntity <Map<String, Object>> sendEmail(EmailEntity email) {

		HashMap<String, Object> response = new HashMap<>();

		try {

			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(email.getTo());
			message.setSubject(email.getSubject());
			message.setText(email.getSingleContent());
			emailSender.send(message);

			response.put("status", Boolean.TRUE);
			response.put("message", "email sent successfully");

			return ResponseEntity.ok(response);

		}
		catch (Exception e){
			
			logger.error(e.fillInStackTrace());
			logger.catching(e);
			response.put("exception", e.getMessage());

			// Returning exception as a response
			throw new RuntimeException(e.getMessage());

		}
	}

	@Override
	@Async
	public void sendTemplateEmail(EmailEntity emailTemplateEntity){
		
		try{

			Context context = new Context();
			context.setVariable("email", emailTemplateEntity);
			String htmlContent = templateEngine.process("EmailTemplate", context);

			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

			helper.setTo(emailTemplateEntity.getTo());
			helper.setSubject(emailTemplateEntity.getSubject());
			helper.setText(htmlContent, true);
			helper.setFrom("${spring.mail.username}");

			String tN = Thread.currentThread().getName();
			
			System.err.println(tN);
			emailSender.send(message);
			
		}
		catch(Exception e){
			
			logger.error(e.fillInStackTrace());
			logger.catching(e);

			// Returning exception as a response
			throw new RuntimeException(e.getMessage());

		}
	}
	
	@Override
	public EmailEntity generalEmailEntity(String subject, String fromEmail, String toEmail, String messageHeader, String messageFooter, String messageBodyP1, String messageBodyP2, String messageBodyP3) {
		
		EmailEntity emailEntity = new EmailEntity();
		
		if(!Strings.isNullOrEmpty(subject)) {
			emailEntity.setSubject(subject);
		}
		if(Strings.isNullOrEmpty(toEmail)) {
			emailEntity.setTo(Objects.requireNonNull(environment.getProperty("spring.mail.username")));
		} else {
			emailEntity.setTo(toEmail);
		}
		if(Strings.isNullOrEmpty(fromEmail)) {
			emailEntity.setFrom(Objects.requireNonNull(environment.getProperty("spring.mail.username")));
		} else {
			emailEntity.setFrom(fromEmail);
		}
		if(!Strings.isNullOrEmpty(messageHeader)) {
			emailEntity.setMessageHeader(messageHeader);
		}
		if(!Strings.isNullOrEmpty(messageFooter)) {
			emailEntity.setMessageFooter(messageFooter);
		}
		if(!Strings.isNullOrEmpty(messageBodyP1)) {
			emailEntity.setMessageBodyP1(messageBodyP1);
		}
		if(!Strings.isNullOrEmpty(messageBodyP2)) {
			emailEntity.setMessageBodyP2(messageBodyP2);
		}
		if(!Strings.isNullOrEmpty(messageBodyP3)) {
			emailEntity.setMessageBodyP3(messageBodyP3);
		}
		
		return emailEntity;
	}
	
	//method to set communication data members
	@Override
	public CommunicationInfoDB setCommunicationDataMembers(CommunicationInfoDB communicationInfoExisting, CommunicationInfoInput communicationJson) throws RuntimeException
	{
		//setting data-members
		communicationInfoExisting.setAddress(communicationJson.getAddress());
		communicationInfoExisting.setPinCode(communicationJson.getPinCode());
		communicationInfoExisting.setCountry(communicationJson.getCountry());
		communicationInfoExisting.setState(communicationJson.getState());
		communicationInfoExisting.setDistrict(communicationJson.getDistrict());
		communicationInfoExisting.setCity(communicationJson.getCity());
		communicationInfoExisting.setLocality(communicationJson.getLocality());
		communicationInfoExisting.setContactNumber(communicationJson.getContactNumber());
		communicationInfoExisting.setCountryCode(communicationJson.getCountryCode());
		communicationInfoExisting.setEmailId(communicationJson.getEmailId());
		communicationInfoExisting.setIsActive(communicationJson.getIsActive());
		communicationInfoExisting.setContactPersonName(communicationJson.getContactPersonName());
		communicationInfoExisting.setContactPersonMobileNumber(communicationJson.getContactPersonMobileNumber());
		communicationInfoExisting.setContactPersonEmailId(communicationJson.getContactPersonEmailId());
		
		
		//whose who is a column
		communicationInfoExisting.setCreatedBy(Objects.isNull(communicationInfoExisting.getCreatedBy()) ? communicationJson.getCreatedBy() : communicationInfoExisting.getCreatedBy());
		communicationInfoExisting.setCreationTimeStamp(Objects.isNull(communicationInfoExisting.getCreationTimeStamp()) ? LocalDateTime.now() : communicationInfoExisting.getCreationTimeStamp());
		communicationInfoExisting.setUpdationTimeStamp(LocalDateTime.now());
		communicationInfoExisting.setUpdatedBy(communicationJson.getUpdatedBy());
		
		return communicationInfoExisting;
		
	}
	
	//setting role
	@Override
	public RolesDB setRoles(RolesDB dbRoles, RoleInput jsonRoles) throws RuntimeException
	{
			//setting role master
			if (!Objects.isNull(jsonRoles.getRoleMaster())) {
				
				//calling current class setRoleMaster() method to set roleMaster
				dbRoles.setRoleMaster(emrDaoInterf.getRoleMaster(jsonRoles.getRoleMaster().getRoleMasterId()).orElseThrow(() -> new DgsEntityNotFoundException("Role Master not found")));
			}
			//returning dbRoles
			return dbRoles;
	}
}
