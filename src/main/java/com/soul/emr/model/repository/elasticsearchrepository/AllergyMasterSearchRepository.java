package com.soul.emr.model.repository.elasticsearchrepository;

import com.soul.emr.model.entity.masterentity.elasticsearchentity.AllergyMasterSearch;
import jakarta.transaction.Transactional;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

public interface AllergyMasterSearchRepository extends ReactiveElasticsearchRepository <AllergyMasterSearch, Long>
{
	@Transactional
	@Query("{ \"bool\": { \"should\": [" +
			"{ \"prefix\": { \"allergy_type\": \"?0\" } }, " +
			"{ \"prefix\": { \"allergy_desc\": \"?1\" } }, " +
			"{ \"prefix\": { \"allergy_code\": \"?2\" } } " +
			"], \"filter\": { \"term\": { \"is_active\": \"?3\" } } } }")
	Flux<AllergyMasterSearch> searchAllergies(String allergyDesc, String allergyType, String allergyCode, Boolean isActive);
	
	@Transactional
	@Query("{ \"bool\": { \"filter\": { \"term\": { \"is_active\": \"?0\" } } } }")
	Flux<AllergyMasterSearch> searchAllAllergies(Boolean isActive);
	
}
