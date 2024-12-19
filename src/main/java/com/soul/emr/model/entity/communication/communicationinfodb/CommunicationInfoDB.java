package com.soul.emr.model.entity.communication.communicationinfodb;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import com.soul.emr.model.entity.modelpatient.patientregistrationdb.PatientDetailsDB;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = false, exclude = {"patientDetails"})
@Entity
@Table(name = "EMR_TXN_REGISTRATION_COMMUNICATION_INFO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommunicationInfoDB extends WhoseColumnsEntity implements Serializable{

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "registration_communication_info_seq")
	@SequenceGenerator(name = "registration_communication_info_seq", sequenceName = "registration_communication_info_seq", allocationSize = 1)
	@JsonProperty("CommunicationInfoId")
	@Column(name = "communication_Info_Id", length = 19)
	private Long CommunicationInfoId;

	@JsonProperty("address")
	@Column(name = "address", length = 50)
	private String address;

	@JsonProperty("pinCode")
	@Column(name = "pin_code", length = 50)
	private Long pinCode;

	@JsonProperty("country")
	@Column(name = "country", length = 50)
	private String country;

	@JsonProperty("countryCode")
	@Column(name = "country_code")
	private String countryCode;

	@JsonProperty("state")
	@Column(name = "state", length = 50)
	private String state;

	@JsonProperty("district")
	@Column(name = "district", length = 50)
	private String district;

	@JsonProperty("cityVillage")
	@Column(name = "city_village", length = 50)
	private String cityVillage;

	@JsonProperty("policeStation")
	@Column(name = "police_station", length = 50)
	private String policeStation;

	@JsonProperty("emailId")
	@Email
	@Column(name = "email_id", length = 50, unique = true)
	private String emailId;

	@JsonProperty("mobileNumber")
	@Column(name = "mobile_number", unique = true, nullable = false)
	private String mobileNumber;

	@JsonProperty("nationality")
	@Column(name = "nationality", nullable = false)
	private String nationality;

	@JsonProperty("isMobileVerified")
	@Column(name = "IS_MOBILE_VERIFIED", length = 5)
	private Boolean isMobileVerified;

	@JsonProperty("isEmailVerified")
	@Column(name = "IS_EMAIL_VERIFIED", length = 5)
	private Boolean isEmailVerified;

	@JsonProperty("isActive")
	@Column(name = "is_active", nullable = false)
	private Boolean isActive;

	@JsonProperty("patientDetails")
	@ManyToMany(mappedBy = "communicationInfoDB", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set <PatientDetailsDB> patientDetails = new HashSet <>();
}
