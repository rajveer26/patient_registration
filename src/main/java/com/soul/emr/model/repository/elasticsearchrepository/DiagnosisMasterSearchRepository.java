package com.soul.emr.model.repository.elasticsearchrepository;

import com.soul.emr.model.entity.masterentity.elasticsearchentity.DiagnosisMasterSearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface DiagnosisMasterSearchRepository extends ElasticsearchRepository<DiagnosisMasterSearch, Long>
{
	List <DiagnosisMasterSearch> findDiagnosisMasterSearchByLongDescriptionContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String longDescription, String description);


}
