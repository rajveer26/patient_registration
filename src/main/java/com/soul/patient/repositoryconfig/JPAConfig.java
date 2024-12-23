package com.soul.patient.repositoryconfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.soul.patient.model.repository.jparepository")
public class JPAConfig
{

}
