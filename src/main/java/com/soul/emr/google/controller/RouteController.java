package com.soul.emr.google.controller;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.*;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.EntryPoint;
import com.google.api.services.calendar.model.Event;
import com.soul.emr.google.configuration.GoogleConfiguration;
import com.soul.emr.google.service.EventServiceInterf;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/google-auth")
@CrossOrigin(allowedHeaders = {"Authorization"})
public class RouteController{
	
	private final GoogleConfiguration config;
	private final EventServiceInterf eventService;
	private final Environment environment;
	
	private static final Log logger = LogFactory.getLog(RouteController.class);
	private static final String APPLICATION_NAME = "EMR";
	private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
	
	protected static HttpTransport httpTransport;
	
	GoogleAuthorizationCodeFlow flow;
	Credential credential;
	GoogleClientSecrets clientSecrets;
	protected static com.google.api.services.calendar.Calendar client;
	
	@Autowired
	public RouteController(GoogleConfiguration config, EventServiceInterf eventService, Environment environment){
		this.config = config;
		this.eventService = eventService;
		this.environment = environment;
	}
	
	@GetMapping("/google/signing")
	public RedirectView googleSignIn(){
		return new RedirectView("/soul/oauth2/authorization/google");
	}
	
	
	@GetMapping(value = "/login")
	public RedirectView googleConnectionStatus(@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from, @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to, HttpServletRequest request) throws Exception{
		System.err.println("coming 1");
		
		// Generate the authorization URL
		String state = from + ";" + to;
		String redirectUri = config.getRedirectUri() + "google/login";
		String authorizationUrl = this.authorize(redirectUri, state);
		
		return new RedirectView(authorizationUrl);
	}
	
	@GetMapping(value = "/login", params = {"code", "state"})
	public ResponseEntity <List <Event>> oauth2Callback(@RequestParam(value = "code") String code, @RequestParam("state") String state, HttpServletRequest request){
		
		try{
			// Retrieve from and to from state parameter or session
			String[] stateParams = state.split(";");
			LocalDateTime from = LocalDateTime.parse(stateParams[0]);
			LocalDateTime to = LocalDateTime.parse(stateParams[1]);
			
			List <Object> ls = eventService.requiredToken(code, config.getRedirectUri() + "google/login", flow, credential, httpTransport, APPLICATION_NAME, JSON_FACTORY, client);
			Calendar.Events events = (Calendar.Events) ls.get(1);
			this.credential = (Credential) ls.get(0);
			
			List <Event> message = eventService.showEvents(events, from, to);
			
			return ResponseEntity.status(HttpStatus.OK).body(message);
		}
		
		catch(Exception e){
			logger.warn("Exception while handling OAuth2 callback (" + e.getMessage() + ")." + " Redirecting to google connection status page.");
			System.err.println("Exception while handling OAuth2 callback (" + e.getMessage() + ")." + " Redirecting to google connection status page.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
		
		
	}
	
	
	@GetMapping(value = "/createEventConference")
	public RedirectView googleConnectionStatus1(HttpServletRequest request, @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "dd-MM-yyyy HH:mm") LocalDateTime from, @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "dd-MM-yyyy HH:mm") LocalDateTime to, @RequestParam("eventName") String eventName) throws Exception{
		System.err.println("coming");
		String state = from + ";" + to + ";" + eventName;
		return new RedirectView(this.authorize(config.getRedirectUri() + "google/createEventConference", state));
	}
	
	@GetMapping(value = "/createEventConference", params = "code")
	public void oauth2CallbackCreateConfEvent(HttpServletResponse response, @RequestParam(value = "code") String code, @RequestParam("state") String state){
		try{
			List <Object> ls = eventService.requiredToken(code, config.getRedirectUri() + "google/createEventConference", flow, credential, httpTransport, APPLICATION_NAME, JSON_FACTORY, client);
			
			Calendar.Events events = (Calendar.Events) ls.get(1);
			this.credential = (Credential) ls.get(0);
			
			String[] stateParams = state.split(";");
			
			LocalDateTime from = LocalDateTime.parse(stateParams[0]);
			LocalDateTime to = LocalDateTime.parse(stateParams[1]);
			String eventName = stateParams[2];
			
			if (eventService.createEvent(events, from, to, eventName, Boolean.TRUE)) {
				HashMap <String, String> responseMap = new HashMap <>();
				List <Event> eventList = eventService.showEvents(events, from, to);
				eventList.forEach(event -> {
					
					List <EntryPoint> entryPointList = event.getConferenceData().getEntryPoints();
					
					Optional <EntryPoint> filteredEntryPoint = entryPointList.stream().filter(entryPoint -> Objects.equals(entryPoint.getEntryPointType().trim().toLowerCase(), "video".trim().toLowerCase())).findFirst();
					
					filteredEntryPoint.ifPresent(entryPoint -> responseMap.put("meetingLink", entryPoint.getUri()));
					
				});
				
				//checking the map is empty or not
				if (!responseMap.isEmpty()) {
					response.getWriter().write(responseMap.toString());
					
					//creating uri
					UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(environment.getProperty("client.redirect-url") + "createWebinar").queryParam("meetingLink", responseMap.get("meetingLink"));
					response.sendRedirect(uriComponentsBuilder.toUriString());
					
				} else {
					
					responseMap.put("message", "Error while creating meeting link");
					response.getWriter().write(responseMap.toString());
					
					//creating uri
					UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(environment.getProperty("client.redirect-url") + "createWebinar");
					response.sendRedirect(uriComponentsBuilder.toUriString());
				}
				
			}
			
			
		}
		//catch block
		catch(Exception e){
			System.err.println(e.getMessage());
			logger.warn("Exception in oauth2CallbackCreateEvent present in RouteController " + e.getMessage());
			
		}
		
	}
	
	@GetMapping(value = "/createEvent")
	public RedirectView googleConnectionStatus2(HttpServletRequest request, @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "dd-MM-yyyy HH:mm") LocalDateTime from, @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "dd-MM-yyyy HH:mm") LocalDateTime to, @RequestParam("eventName") String eventName) throws Exception{
		
		System.err.println("coming");
		String state = from + ";" + to + ";" + eventName;
		return new RedirectView(this.authorize(config.getRedirectUri() + "google/createEventConference", state));
		
	}
	
	
	@GetMapping(value = "/createEvent", params = "code")
	public ResponseEntity <Boolean> oauth2CallbackCreateEvent(@RequestParam(value = "code") String code, @RequestParam("state") String state){
		
		try{
			
			List <Object> ls = eventService.requiredToken(code, config.getRedirectUri() + "google/createEvent", flow, credential, httpTransport, APPLICATION_NAME, JSON_FACTORY, client);
			
			Calendar.Events events = (Calendar.Events) ls.get(1);
			this.credential = (Credential) ls.get(0);
			
			String[] stateParams = state.split(";");
			
			LocalDateTime from = LocalDateTime.parse(stateParams[0]);
			LocalDateTime to = LocalDateTime.parse(stateParams[1]);
			String eventName = stateParams[2];
			
			if (eventService.createEvent(events, from, to, eventName, Boolean.FALSE)) {
				return ResponseEntity.status(HttpStatus.OK).body(Boolean.TRUE);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Boolean.FALSE);
				
			}
			
		}
		catch(Exception e){
			System.err.println(e.getMessage());
			logger.warn("Exception in oauth2CallbackCreateEvent present in RouteController " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Boolean.FALSE);
			
		}
		
	}
	
	
	private String authorize(String redirectURL, String state) throws Exception{
		AuthorizationCodeRequestUrl authorizationUrl;
		if (flow == null) {
			GoogleClientSecrets.Details web = new GoogleClientSecrets.Details().setClientId(config.getClientId()).setClientSecret(config.getClientSecret()).setTokenUri(config.getTokenUri()).set("project_id", config.getProjectId()).set("access_type", "offline").set("prompt", "consent");
			
			clientSecrets = new GoogleClientSecrets().setWeb(web);
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			flow          = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, Collections.singleton(CalendarScopes.CALENDAR)).build();
		}
		System.err.println(redirectURL);
		authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectURL).setState(state);
		System.out.println("cal authorizationUrl->" + authorizationUrl);
		return authorizationUrl.build();
	}
	
	@GetMapping(value = "/logout")
	public ResponseEntity <Boolean> logout(){
		try{
			if (credential != null && credential.getAccessToken() != null) {
				String accessToken = credential.getAccessToken();
				HttpRequestFactory factory = httpTransport.createRequestFactory();
				GenericUrl url = new GenericUrl("https://accounts.google.com/o/oauth2/revoke?token=" + accessToken);
				HttpRequest request = factory.buildGetRequest(url);
				HttpResponse response = request.execute();
				if (response.getStatusCode() == 200) {
					System.out.println("Successfully logged out.");
					credential = null;
					flow       = null;
					return ResponseEntity.status(HttpStatus.OK).body(Boolean.TRUE);
				} else {
					System.out.println("Failed to log out.");
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Boolean.FALSE);
				}
			} else {
				System.out.println("User is already logged out.");
				return ResponseEntity.status(HttpStatus.OK).body(Boolean.TRUE);
			}
		}
		catch(Exception e){
			System.err.println(e.getMessage());
			logger.warn("Exception in logout present in RouteController " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Boolean.FALSE);
		}
	}
}
