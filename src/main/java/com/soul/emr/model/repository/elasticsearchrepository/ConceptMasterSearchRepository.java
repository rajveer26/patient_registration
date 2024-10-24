package com.soul.emr.model.repository.elasticsearchrepository;

import com.soul.emr.model.entity.masterentity.elasticsearchentity.ConceptMasterSearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ConceptMasterSearchRepository extends ElasticsearchRepository<ConceptMasterSearch, Long>
{
	
	List <ConceptMasterSearch> searchByConceptMasterTypeAndConceptMasterCodeContainingIgnoreCaseOrConceptMasterNameContainingIgnoreCase(String conceptMasterType, String conceptMasterCode, String conceptMasterName);

}
