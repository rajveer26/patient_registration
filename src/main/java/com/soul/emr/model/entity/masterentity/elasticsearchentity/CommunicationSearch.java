package com.soul.emr.model.entity.masterentity.elasticsearchentity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommunicationSearch extends WhoseColumnsEntity implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	@JsonProperty("CommunicationInfoId")
	@Field(name = "communication_info_id")
	private Long CommunicationInfoId;
	
	@JsonProperty("address")
	@Field(name = "address")
	private String address;
	
	@JsonProperty("pinCode")
	@Field(name = "pin_code")
	private Long pinCode;
	
	@JsonProperty("country")
	@Field(name = "country")
	private String country;
	
	@JsonProperty("state")
	@Field(name = "state")
	private String state;
	
	@JsonProperty("district")
	@Field(name = "district")
	private String district;
	
	@JsonProperty("city")
	@Field(name = "city")
	private String city;
	
	@JsonProperty("locality")
	@Field(name = "locality")
	private String locality;
	
	@JsonProperty("contactNumber")
	@Field(name = "contact_number")
	private Long contactNumber;
	
	@JsonProperty("countryCode")
	@Field(name = "country_code")
	private String countryCode;
	
	@JsonProperty("emailId")
	@Email
	@Field(name = "email_id")
	private String emailId;
	
	@JsonProperty("isMobileVerified")
	@Field(name = "is_mobile_verified")
	private Boolean isMobileVerified;
	
	@JsonProperty("isEmailVerified")
	@Field(name = "is_email_verified")
	private Boolean isEmailVerified;
	
	@JsonProperty("isActive")
	@Field(name = "is_active")
	private Boolean isActive;
	
	@JsonProperty("contactPersonName")
	@Field(name = "contact_person_name")
	private String contactPersonName;
	
	@JsonProperty("contactPersonMobileNumber")
	@Field(name = "contact_Person_Mobile_Number")
	private Long contactPersonMobileNumber;
	
	@JsonProperty("contactPersonEmailId")
	@Email
	@Field(name = "contact_person_email_id")
	private String contactPersonEmailId;
}
