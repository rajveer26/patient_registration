package com.soul.emr.model.entity.modelbusinessgroup.graphqlentity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import com.soul.emr.model.entity.modelbusinessgroup.organization.graphqlentity.OrganizationGroupInput;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class BusinessGroupInput extends WhoseColumnsEntity
{
	
	@JsonProperty("businessGroupId")
	private Long businessGroupId;
	
	@JsonProperty("businessGroupCode")
	@NotBlank(message = "BUSINESS_GROUP_CODE_CANNOT_BE_BLANK")
	private String businessGroupCode;
	
	@JsonProperty("businessGroupName")
	@NotBlank(message = "BUSINESS_GROUP_NAME_CANNOT_BE_BLANK")
	private String businessGroupName;
	
	@JsonProperty("businessGroupTIN")
	@NotBlank(message = "BUSINESS_GROUP_TIN_CANNOT_BE_BLANK")
	private String businessGroupTIN;
	
	@JsonProperty("businessGroupGSTNumber")
	@NotBlank(message = "BUSINESS_GROUP_GST_NUMBER_CANNOT_BE_BLANK")
	private String businessGroupGSTNumber;
	
	@JsonProperty("isActive")
	@NotNull(message = "IS_ACTIVE_CANNOT_BE_NUL")
	private Boolean isActive;
	
	@JsonProperty("organizationDBList")
	private List<OrganizationGroupInput> organizationDBList = new ArrayList <>();
}
