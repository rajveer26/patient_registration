package com.soul.emr;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableAsync
@Log4j2
public class EmrApplication{

	public static void main(String[] args) {
		SpringApplication.run(EmrApplication.class, args);
	}
	
	
	//creating a Bean for WebClient instance
	@Bean
	public WebClient webClient() {
		final int size = 10 * 1024 * 1024;
		final ExchangeStrategies strategies = ExchangeStrategies.builder()
				.codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
				.build();
		
		return WebClient.builder()
				.exchangeStrategies(strategies)
				.build();
	}
	
}
