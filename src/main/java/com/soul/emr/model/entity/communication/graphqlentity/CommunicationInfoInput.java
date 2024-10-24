package com.soul.emr.model.entity.communication.graphqlentity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import com.soul.emr.model.entity.modelbusinessgroup.organization.graphqlentity.OrganizationGroupInput;
import com.soul.emr.model.entity.modelemployee.graphqlentity.EmployeeInfoInput;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class CommunicationInfoInput extends WhoseColumnsEntity
{
	@JsonProperty("CommunicationInfoId")
	private Long CommunicationInfoId;
	
	@NotBlank(message = "ADDRESS_CANNOT_BE_BLANK")
	@JsonProperty("address")
	private String address;
	
	@NotNull(message = "PIN_CODE_CANNOT_BE_NULL")
	@JsonProperty("pinCode")
	private Long pinCode;
	
	@NotBlank(message = "COUNTRY_CANNOT_BE_BLANK")
	@JsonProperty("country")
	private String country;
	
	@NotBlank(message = "STATE_CANNOT_BE_BLANK")
	@JsonProperty("state")
	private String state;
	
	@NotBlank(message = "DISTRICT_CANNOT_BE_BLANK")
	@JsonProperty("district")
	private String district;
	
	@NotBlank(message = "CITY_CANNOT_BE_BLANK")
	@JsonProperty("city")
	private String city;
	
	@NotBlank(message = "LOCALITY_CANNOT_BE_BLANK")
	@JsonProperty("locality")
	private String locality;
	
	@NotNull(message = "CONTACT_NUMBER_CANNOT_BE_NULL")
	@JsonProperty("contactNumber")
	private Long contactNumber;
	
	@JsonProperty("countryCode")
	private String countryCode;
	
	@NotBlank(message = "EMAIL-ID_CANNOT_BE_BLANK")
	@JsonProperty("emailId")
	@Email
	private String emailId;
	
	@NotNull(message = "IS-ACTIVE-ID_CANNOT_BE_NULL")
	@JsonProperty("isActive")
	private Boolean isActive;
	
	@JsonProperty("contactPersonName")
	private String contactPersonName;
	
	@JsonProperty("contactPersonMobileNumber")
	private Long contactPersonMobileNumber;
	
	@JsonProperty("contactPersonEmailId")
	@Email
	private String contactPersonEmailId;
	
	@JsonProperty("employeeInfoDB")
	private EmployeeInfoInput employeeInfoDB;
	
	@JsonProperty("organizationGroupDB")
	private OrganizationGroupInput organizationGroupInput;
}
