package com.soul.emr.model.repository.elasticsearchrepository;

import com.soul.emr.model.entity.masterentity.elasticsearchentity.ServiceMasterSearch;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ServiceMasterSearchRepository extends ElasticsearchRepository<ServiceMasterSearch, Long>
{
	
	@Query("{ \"bool\": { \"minimum_should_match\": 1, \"should\": [" +
			"{ \"match\": { \"service_master_name\": { \"query\": \"?0\" }}}, " + // Match for service_master_name
			"{ \"match\": { \"service_master_code\": { \"query\": \"?1\" }}}, " + // Match for service_master_code
			"{ \"match\": { \"service_category\": { \"query\": \"?2\" }} } " + // Match for service_category
			"], \"filter\": { \"term\": { \"applicable_gender\": \"?3\" } } } }")
	List<ServiceMasterSearch> findByApplicableGenderIgnoreCaseAndServiceCategoryContainingIgnoreCaseOrServiceMasterNameContainingIgnoreCaseOrServiceMasterCodeContainingIgnoreCase(String serviceMasterName, String serviceMasterCode, String serviceCategory, String applicableGender);
	
}
