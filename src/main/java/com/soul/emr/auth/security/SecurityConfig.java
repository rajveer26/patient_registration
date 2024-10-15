package com.soul.emr.auth.security;

import com.soul.emr.auth.oidcauth.CustomOidcUserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig extends AbstractHttpConfigurer <SecurityConfig, HttpSecurity>{
	
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtTokenFilter jwtTokenFilter;
	private final JwtTokenProvider jwtTokenProvider;
	private final CustomOidcUserService customOidcUserService;
	private final OAuth2AuthorizedClientService authorizedClientService;
	private final Environment environment;
	
	@Autowired
	public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtTokenFilter jwtTokenFilter, JwtTokenProvider jwtTokenProvider, CustomOidcUserService customOidcUserService, OAuth2AuthorizedClientService authorizedClientService, Environment environment){
		this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
		this.jwtTokenFilter              = jwtTokenFilter;
		this.jwtTokenProvider            = jwtTokenProvider;
		this.customOidcUserService       = customOidcUserService;
		this.authorizedClientService     = authorizedClientService;
		this.environment                 = environment;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http.csrf(AbstractHttpConfigurer::disable).cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource())).authorizeHttpRequests(authz -> authz.requestMatchers("/webjars/**", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/swagger-resources", "/swagger-ui.html", "/auth/**", "/google/**", "/google-auth/**",// Allow Google sign-in endpoints
		                                                                                                                                                                                                              "/oauth2/**", "/zoom/**", "/login/**", "/error", "/admin/generateOtp", "/admin/verifyOtp", "/graphiql/**", "/graphql/**", "/actuator/**")  // Allow OAuth2 endpoints
				.permitAll().requestMatchers("/admin/**").hasAuthority("ADMIN").anyRequest().authenticated()).oauth2Login(oauth2Login -> oauth2Login.userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.oidcUserService(customOidcUserService)).successHandler(successHandler()).failureHandler(failureHandler())).exceptionHandling(excp -> excp.authenticationEntryPoint(jwtAuthenticationEntryPoint)).sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
	
	@Bean
	public OidcUserService oidcUserService(){
		return new OidcUserService();
	}
	
	
	@Bean
	public AuthenticationSuccessHandler successHandler(){
		return (request, response, authentication) -> {
			OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
			OidcUser oidcUser = (OidcUser) oauth2Token.getPrincipal();
			
			// Retrieve the authorized client
			OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(oauth2Token.getAuthorizedClientRegistrationId(), oauth2Token.getName()
			                                                                                      
			                                                                                      );
			String tokenLocal;
			try{
				tokenLocal = jwtTokenProvider.generateJWTOidc(oidcUser);
			}
			catch(NoSuchAlgorithmException | InvalidKeySpecException e){
				throw new RuntimeException(e);
			}
			String idToken = oidcUser.getIdToken().getTokenValue();
			String accessToken = authorizedClient.getAccessToken().getTokenValue();
			
			// Return tokens as JSON response
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write("{\"token\": \"" + tokenLocal + "\", \"access_token\": \"" + accessToken + "\"}");
			
			// Build the redirect URL with tokens as query parameters
			UriComponentsBuilder redirectUrl = UriComponentsBuilder.fromHttpUrl(environment.getProperty("client.redirect-url") + "landingPage").queryParam("token", URLEncoder.encode(tokenLocal, StandardCharsets.UTF_8)).queryParam("access_token", URLEncoder.encode(accessToken, StandardCharsets.UTF_8));

			// Redirect to the constructed URL
			response.sendRedirect(redirectUrl.encode().toUriString());
		};
	}
	
	@Bean
	public AuthenticationFailureHandler failureHandler(){
		return (request, response, exception) -> {
			// Handle the error and respond with JSON
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("{\"error\": \"" + exception.getMessage() + "\"}");
		};
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource(){
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedOrigin("*"); // Adjust according to your security requirements
		configuration.addAllowedMethod("*");
		configuration.addAllowedHeader("*");
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
