package com.soul.emr.model.entity.masterentity.elasticsearchentity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "prefix_search_master")
public class PrefixMasterSearch extends WhoseColumnsEntity implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("prefixMasterId")
	@Field(name = "prefix_master_id")
	private Long prefixMasterId;
	
	@JsonProperty("prefixName")
	@Field(name = "prefix_name")
	private String prefixName;
	
	@JsonProperty("isActive")
	@Field(name = "is_active")
	private Boolean isActive;
}
