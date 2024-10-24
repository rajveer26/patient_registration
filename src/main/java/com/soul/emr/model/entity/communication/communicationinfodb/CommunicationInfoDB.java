package com.soul.emr.model.entity.communication.communicationinfodb;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import com.soul.emr.model.entity.modelbusinessgroup.organization.organizationdb.OrganizationDB;
import com.soul.emr.model.entity.modelemployee.registrationdb.EmployeeInfoDB;
import com.soul.emr.model.entity.modelpatient.patientregistrationdb.PatientDetailsDB;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = false, exclude = {"employeeInfoDB", "organizationDB", "patientDetailsDB"})
@Entity
@Table(name = "EMR_TXN_COMMUNICATION_INFO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommunicationInfoDB extends WhoseColumnsEntity implements Serializable{
	
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "communication_info_seq")
	@SequenceGenerator(name = "communication_info_seq", sequenceName = "communication_info_seq", allocationSize = 1)
	@JsonProperty("CommunicationInfoId")
	@Column(name = "communication_Info_Id", length = 19)
	private Long CommunicationInfoId;
	
	@JsonProperty("address")
	@Column(name = "address", length = 50, unique = true, nullable = false)
	private String address;
	
	@JsonProperty("pinCode")
	@Column(name = "pin_code", length = 50, nullable = false)
	private Long pinCode;
	
	@JsonProperty("country")
	@Column(name = "country", length = 50, nullable = false)
	private String country;
	
	@JsonProperty("state")
	@Column(name = "state", length = 50, nullable = false)
	private String state;
	
	@JsonProperty("district")
	@Column(name = "district", length = 50, nullable = false)
	private String district;
	
	@JsonProperty("city")
	@Column(name = "city", length = 50, nullable = false)
	private String city;
	
	@JsonProperty("locality")
	@Column(name = "locality", length = 50, nullable = false)
	private String locality;
	
	@JsonProperty("contactNumber")
	@Column(name = "contact_number", length = 19, unique = true, nullable = false)
	private Long contactNumber;
	
	@JsonProperty("countryCode")
	@Column(name = "country_code", length = 50, nullable = false)
	private String countryCode;
	
	@JsonProperty("emailId")
	@Email
	@Column(name = "email_id", length = 50, unique = true, nullable = false)
	private String emailId;
	
	@JsonProperty("isMobileVerified")
	@Column(name = "IS_MOBILE_VERIFIED", length = 5)
	private Boolean isMobileVerified;
	
	@JsonProperty("isEmailVerified")
	@Column(name = "IS_EMAIL_VERIFIED", length = 5)
	private Boolean isEmailVerified;
	
	@JsonProperty("isActive")
	@Column(name = "is_active", nullable = false)
	private Boolean isActive;
	
	@JsonProperty("contactPersonName")
	@Column(name = "contact_Person_Name", length = 50)
	private String contactPersonName;
	
	@JsonProperty("contactPersonMobileNumber")
	@Column(name = "contact_Person_Mobile_Number")
	private Long contactPersonMobileNumber;
	
	@JsonProperty("contactPersonEmailId")
	@Email
	@Column(name = "contact_Person_email_id", unique = true, nullable = false, length = 50)
	private String contactPersonEmailId;
	
	@JsonBackReference
	@JsonProperty("employeeInfoDB")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_Info_Id", referencedColumnName = "user_details_Id")
	private EmployeeInfoDB employeeInfoDB;
	
	@JsonBackReference
	@JsonProperty("organizationGroupDB")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organization_Info_Id", referencedColumnName = "organization_Master_Id")
	private OrganizationDB organizationDB;
	
	@JsonBackReference
	@JsonProperty("patientDetailsDB")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_Info_Id", referencedColumnName = "patient_Id")
	private PatientDetailsDB patientDetailsDB;
}
