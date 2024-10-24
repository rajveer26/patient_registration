package com.soul.emr.model.entity.modelemployee.graphqlentity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.soul.emr.model.entity.masterentity.graphqlentity.RoleMasterInput;
import com.soul.emr.model.entity.modelpatient.patientregistrationdb.PatientDetailsDB;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class RoleInput
{
	@JsonProperty("rolesId")
	private Long rolesId;
	
	@NotNull(message = "ROLE_MASTER_CANNOT_BE_NULL")
	@JsonProperty("roleMaster")
	private RoleMasterInput roleMaster;
	
	@JsonProperty("employeeInfoDB")
	private Set <EmployeeInfoInput> employeeInfoDB = new HashSet <>();
	
	@JsonProperty("patientDetailsDBSet")
	private Set <PatientDetailsDB> patientDetailsDBSet = new HashSet <>();
}
