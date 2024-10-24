package com.soul.emr.model.entity.masterentity.graphqlentity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import com.soul.emr.model.entity.modelbusinessgroup.organization.graphqlentity.OrganizationGroupInput;
import com.soul.emr.model.entity.modelemployee.graphqlentity.EmployeeInfoInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentMasterInput extends WhoseColumnsEntity
{
	@JsonProperty("departmentMasterId")
	private Long departmentMasterId;
	
	@JsonProperty("departmentId")
	private Long departmentId;
	
	@JsonProperty("departmentName")
	private String departmentName;
	
	@JsonProperty("headOfDepartment")
	private String headOfDepartment;
	
	@JsonProperty("departmentType")
	private String departmentType;
	
	@JsonProperty("isDeleted")
	private Boolean isDeleted;
	
	@JsonProperty("employeeInfoDB")
	private Set <EmployeeInfoInput> employeeInfoDB = new HashSet <>();
	
	@JsonProperty("organizationDBS")
	private Set<OrganizationGroupInput> organizationDBS = new HashSet<>();
}
