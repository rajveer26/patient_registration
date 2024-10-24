package com.soul.emr.model.entity.modelemployee.graphqlentity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = false)
@Data
public class UserCredentialsInput extends WhoseColumnsEntity
{
	@JsonProperty("userCredentialId")
	private Long userCredentialId;
	
	@JsonProperty("username")
	private String username;

	@NotNull
	@JsonProperty("password")
	private String password;
	
	@JsonProperty("employeeInfo")
	private EmployeeInfoInput employeeInfo;
}
