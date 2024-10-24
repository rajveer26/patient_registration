package com.soul.emr.model.repository.elasticsearchrepository;

import com.soul.emr.model.entity.masterentity.elasticsearchentity.NamespaceMasterSearch;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface NameSpaceMasterSearchRepository extends ElasticsearchRepository<NamespaceMasterSearch, Long>
{
	@Query("{ \"bool\": { \"should\": [" +
			"{ \"match\": { \"namespace_code\": { \"query\": \"?0\", \"operator\": \"and\" }}}, " +
			"{ \"match\": { \"namespace_name\": { \"query\": \"?1\" }} } " +
			"], \"filter\": { \"term\": { \"is_active\": \"?2\" } } } }")
	List<NamespaceMasterSearch> searchByNamespaceCodeOrNamespaceName(String nameSpaceCode, String nameSpaceName, Boolean isActive);
}
