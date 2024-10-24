package com.soul.emr.repositoryconfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.soul.emr.model.repository.jparepository")
public class JPAConfig
{

}
