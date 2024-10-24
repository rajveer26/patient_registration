package com.soul.emr.model.repository.elasticsearchrepository;

import com.soul.emr.model.entity.masterentity.elasticsearchentity.MedicationMasterSearch;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface MedicationMasterSearchRepository extends ElasticsearchRepository<MedicationMasterSearch, Long>
{
	@Query("{ \"bool\": { \"should\": [" +
			"{ \"match\": { \"medication_name\": { \"query\": \"?1\", \"operator\": \"and\" }}}, " + // Match for medication_name
			"{ \"match\": { \"medication_code\": { \"query\": \"?0\", \"operator\": \"and\" }}}, " + // Match for medication_code
			"{ \"match\": { \"medication_group\": { \"query\": \"?2\" }} } " + // Match for medication_group
			"], \"filter\": { \"term\": { \"is_active\": \"?3\" } } } }")
	List<MedicationMasterSearch> findByMedicationCodeLikeIgnoreCaseOrMedicationNameLikeIgnoreCaseOrMedicationGroupLikeIgnoreCaseAndIsActive(String medicationCode, String medicationName, String medicationGroup, Boolean isActive);

}
