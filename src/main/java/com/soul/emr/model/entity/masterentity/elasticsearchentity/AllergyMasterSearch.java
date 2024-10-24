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
@Document(indexName = "allergy_master_search")
public class AllergyMasterSearch extends WhoseColumnsEntity implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("allergyMasterId")
	@Field(name = "allergy_master_id")
	private Long allergyMasterId;
	
	@JsonProperty("allergyCode")
	@Field(name = "allergy_code")
	private String allergyCode;
	
	@JsonProperty("allergyDesc")
	@Field(name = "allergy_desc")
	private String allergyDesc;
	
	@JsonProperty("allergyType")
	@Field(name = "allergy_type")
	private String allergyType;
	
	@JsonProperty("isActive")
	@Field(name = "is_active")
	private Boolean isActive;
}
