package com.soul.emr.repositoryconfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.elasticsearch.repository.config.EnableReactiveElasticsearchRepositories;

@Configuration
@EnableReactiveElasticsearchRepositories(basePackages = "com.soul.emr.model.repository.elasticsearchrepository")
@EnableElasticsearchRepositories(basePackages = "com.soul.emr.model.repository.elasticsearchrepository")
public class ElasticSearConfig {


}