package com.soul.emr.model.repository.elasticsearchrepository;

import com.soul.emr.model.entity.masterentity.elasticsearchentity.AllergyMasterSearch;
import com.soul.emr.model.entity.masterentity.elasticsearchentity.PrefixMasterSearch;
import jakarta.transaction.Transactional;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

public interface PrefixMasterSearchRepository extends ReactiveElasticsearchRepository<PrefixMasterSearch, Long>
{
	@Transactional
	@Query("{ \"bool\": { \"filter\": { \"term\": { \"is_active\": \"?0\" } } } }")
	Flux <PrefixMasterSearch> fetchAllPrefix(Boolean isActive);
}
