package com.soul.emr.model.entity.masterentity.elasticsearchentity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "namespace_master_search")
public class NamespaceMasterSearch extends WhoseColumnsEntity implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	@Field(name = "namespace_id", type = FieldType.Long)
	private Long namespaceId;
	
	@JsonProperty("namespaceName")
	@Field(name = "namespace_name", type = FieldType.Keyword)
	private String namespaceName;
	
	@JsonProperty("namespaceCode")
	@Field(name = "namespace_code", type = FieldType.Keyword)
	private String namespaceCode;
	
	@JsonProperty("namespaceDescription")
	@Field(name = "namespace_description", type = FieldType.Keyword)
	private String namespaceDescription;
	
	@JsonProperty("isActive")
	@Field(name = "is_active", type = FieldType.Boolean)
	private Boolean isActive;
	
	@JsonProperty("diagnosisMasterDB")
	@Field(type = FieldType.Nested, includeInParent = true)
	private List <DiagnosisMasterSearch> diagnosisMasterDB = new ArrayList <>();
}
