package com.soul.patient.model.entity.communication.graphqlentity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.soul.patient.model.entity.commonentity.WhoseColumnsEntity;
import com.soul.patient.model.entity.modelpatient.graphqlentity.PatientDetailsInput;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommunicationInfoInput extends WhoseColumnsEntity implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	
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
	
	@JsonProperty("patientDetails")
	private Set <PatientDetailsInput> patientDetails = new HashSet <>();
}
