package com.soul.emr.model.entity.masterentity.graphqlentity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrefixMasterInput extends WhoseColumnsEntity implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("prefixMasterId")
	private Long prefixMasterId;
	
	@JsonProperty("prefixName")
	private String prefixName;
	
	@JsonProperty("isActive")
	private Boolean isActive;
}
