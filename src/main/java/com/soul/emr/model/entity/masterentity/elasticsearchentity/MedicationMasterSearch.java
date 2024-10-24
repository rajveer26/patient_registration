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
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "medication_master_search")
public class MedicationMasterSearch extends WhoseColumnsEntity implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	@JsonProperty("medicationMasterId")
	@Field(name = "medication_master_id")
	private Long medicationMasterId;
	
	@JsonProperty("medicationCode")
	@Field(name = "medication_code")
	private String medicationCode;
	
	@JsonProperty("medicationName")
	@Field(name = "medication_name")
	private String medicationName;
	
	@JsonProperty("drugType")
	@Field(name = "drug_type")
	private String drugType;
	
	@JsonProperty("isActive")
	@Field(name = "is_active")
	private Boolean isActive;
	
	@JsonProperty("medicationGroup")
	@Field(name = "medication_group")
	private String medicationGroup;
	
	@JsonProperty("medicationStockMasters")
	@Field(type = FieldType.Nested, includeInParent = true)
	private List <MedicationStockSearch> medicationStockMasters = new ArrayList <>();
}
