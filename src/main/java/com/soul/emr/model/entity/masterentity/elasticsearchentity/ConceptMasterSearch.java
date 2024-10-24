package com.soul.emr.model.entity.masterentity.elasticsearchentity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "concept_master_search")
public class ConceptMasterSearch extends WhoseColumnsEntity implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	@JsonProperty("conceptMasterId")
	@Field(name = "concept_master_id")
	private Long conceptMasterId;
	
	@JsonProperty("conceptMasterCode")
	@Field(name = "concept_master_code")
	private String conceptMasterCode;
	
	@JsonProperty("conceptMasterName")
	@Field(name = "concept_master_name")
	private String conceptMasterName;
	
	@JsonProperty("conceptMasterTypeCode")
	@Field(name = "concept_master_type_code")
	private String conceptMasterTypeCode;
	
	@JsonProperty("conceptMasterType")
	@Field(name = "concept_master_type")
	private String conceptMasterType;
	
	@JsonProperty("conceptMasterDataType")
	@Field(name = "concept_master_data_type")
	private String conceptMasterDataType;
}
