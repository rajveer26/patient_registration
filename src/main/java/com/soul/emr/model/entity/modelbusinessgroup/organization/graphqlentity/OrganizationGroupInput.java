package com.soul.emr.model.entity.modelbusinessgroup.organization.graphqlentity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import com.soul.emr.model.entity.communication.graphqlentity.CommunicationInfoInput;
import com.soul.emr.model.entity.masterentity.graphqlentity.DepartmentMasterInput;
import com.soul.emr.model.entity.modelbusinessgroup.businessgroupdb.BusinessGroupDB;
import com.soul.emr.model.entity.modelemployee.graphqlentity.EmployeeInfoInput;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class OrganizationGroupInput extends WhoseColumnsEntity
{
	
	@JsonProperty("organizationMasterId")
	private Long organizationMasterId;
	
	@JsonProperty("organizationName")
	private String organizationName;
	
	@NotBlank(message = "ORGANIZATION_CODE_CANNOT_BE_BLANK")
	@JsonProperty("organizationCode")
	private String organizationCode;
	
	@JsonProperty("organizationLatitude")
	private Double organizationLatitude;
	
	@JsonProperty("organizationLongitude")
	private Double organizationLongitude;
	
	@JsonProperty("communicationInfoDB")
	private List <CommunicationInfoInput> communicationInfoDB = new ArrayList <>();
	
	@JsonProperty("businessGroupDB")
	private BusinessGroupDB businessGroupDB;
	
	@JsonProperty("employeeInfoDB")
	private Set <EmployeeInfoInput> employeeInfoDB = new HashSet <>();
	
	@JsonProperty("departmentDBS")
	private Set<DepartmentMasterInput> departmentMasterInputSet = new HashSet<>();
}
