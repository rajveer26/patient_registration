package com.soul.emr.model.entity.communication.graphqlentity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import com.soul.emr.model.entity.modelbusinessgroup.organization.graphqlentity.OrganizationGroupInput;
import com.soul.emr.model.entity.modelemployee.graphqlentity.EmployeeInfoInput;
import com.soul.emr.model.entity.modelpatient.graphqlentity.PatientDetailsInput;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = false)
@Data
public class CommunicationInfoInput extends WhoseColumnsEntity
{
	@JsonProperty("CommunicationInfoId")
	private Long CommunicationInfoId;

	@JsonProperty("address")
	private String address;

	@JsonProperty("pinCode")
	private Long pinCode;

	@JsonProperty("country")
	private String country;

	@JsonProperty("countryCode")
	private String countryCode;

	@JsonProperty("state")
	private String state;

	@JsonProperty("district")
	private String district;

	@JsonProperty("cityVillage")
	private String cityVillage;

	@JsonProperty("policeStation")
	private String policeStation;

	@JsonProperty("emailId")
	@Email
	private String emailId;

	@NotNull(message = "CONTACT_NUMBER_CANNOT_BE_NULL")
	@JsonProperty("mobileNumber")
	private String mobileNumber;

	@NotNull(message = "NATIONALITY_CANNOT_BE_NULL")
	@Column(name = "nationality")
	private String nationality;

	@NotNull(message = "IS-ACTIVE-ID_CANNOT_BE_NULL")
	@JsonProperty("isActive")
	private Boolean isActive;

	@JsonProperty("contactPersonRelation")
	private String contactPersonRelation;

	@JsonProperty("contactPersonName")
	private String contactPersonName;
	
	@JsonProperty("contactPersonMobileNumber")
	private String contactPersonMobileNumber;
	
	@JsonProperty("contactPersonEmailId")
	@Email
	private String contactPersonEmailId;
	
	@JsonProperty("employeeInfoDB")
	private EmployeeInfoInput employeeInfoDB;
	
	@JsonProperty("organizationGroupDB")
	private OrganizationGroupInput organizationGroupInput;
	
	@JsonProperty("patientDetails")
	private Set <PatientDetailsInput> patientDetails = new HashSet <>();
}
