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

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "diagnosis_master_search")
public class DiagnosisMasterSearch extends WhoseColumnsEntity implements Serializable{
	@Serial
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@JsonProperty("diagnosisMasterId")
	@Field(name = "diagnosis_master_id", type = FieldType.Long, store = true)
	private Long diagnosisMasterId;
	
	@JsonProperty("longDescription")
	@Field(name = "long_description", type = FieldType.Keyword, store = true)
	private String longDescription;
	
	@JsonProperty("description")
	@Field(name = "description", type = FieldType.Keyword, store = true)
	private String description;
	
	@JsonProperty("icdCode")
	@Field(name = "icd_code", type = FieldType.Keyword, store = true)
	private String icdCode;
	
}
