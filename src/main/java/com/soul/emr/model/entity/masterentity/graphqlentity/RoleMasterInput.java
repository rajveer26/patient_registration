package com.soul.emr.model.entity.masterentity.graphqlentity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class RoleMasterInput extends WhoseColumnsEntity
{
	@JsonProperty("roleMasterId")
	private Long roleMasterId;
	
	@JsonProperty("roleMasterName")
	private String roleMasterName;
	
	@NotBlank(message = "ROLE_MASTER_CODE_CANNOT_BE_BLANK")
	@JsonProperty("roleMasterCode")
	private String roleMasterCode;
	
	@JsonProperty("privileges")
	private List <PrivilegesInput> privileges = new ArrayList <>();
}
