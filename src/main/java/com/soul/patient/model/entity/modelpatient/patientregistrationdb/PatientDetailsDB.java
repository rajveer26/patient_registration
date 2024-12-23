package com.soul.patient.model.entity.modelpatient.patientregistrationdb;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.soul.patient.model.entity.commonentity.WhoseColumnsEntity;
import com.soul.patient.model.entity.communication.communicationinfodb.CommunicationInfoDB;
import com.soul.patient.model.entity.enummaster.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = false, exclude = {"patientRegistrations", "communicationInfoDB", "patientAppointments", "patientMrnLinks", "patientInsuranceDetails", "patientEmergencyContacts"})
@Entity
@Table(name = "EMR_REGISTRATION_PATIENT_DETAILS")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "patientId", scope = PatientDetailsDB.class)
public class PatientDetailsDB extends WhoseColumnsEntity implements Serializable{
	
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_emr_patient_registration")
	@SequenceGenerator(name = "seq_emr_patient_registration", sequenceName = "seq_emr_patient_registration", allocationSize = 1)
	@JsonProperty("patientId")
	@Column(name = "patient_Id")
	private Long patientId;

	@Column(name = "first_name", nullable = false)
	@JsonProperty("firstName")
	private String firstName;

	@Column(name = "middle_name")
	@JsonProperty("middleName")
	private String middleName;

	@Column(name = "last_name")
	@JsonProperty("lastName")
	private String lastName;

	@Column(name = "patient_name")
	@JsonProperty("patientName")
	private String patientName;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "gender", nullable = false)
	@JsonProperty("gender")
	private Gender gender;
	
	@Column(name = "mrno", unique = true, nullable = false)
	@JsonProperty("mrno")
	private String mrno;
	
	@Column(name = "dob", nullable = false)
	@JsonProperty("dob")
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private LocalDate dob;

	@Column(name = "age")
	@JsonProperty("age")
	private Integer age;
	
	@Lob
	@Column(name = "patient_image", columnDefinition = "CLOB")
	@JsonProperty("patientImage")
	private String patientImage;
	
	@Column(name = "registered_on")
	@JsonProperty("registeredOn")
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private LocalDate registeredOn;

	@Column(name = "marital_status")
	@JsonProperty("maritalStatus")
	private String maritalStatus;

	@Column(name = "smart_card_id")
	@JsonProperty("smartCardId")
	private String smartCardId;

	@Column(name = "esi_ip_number")
	@JsonProperty("esiIpNumber")
	private String esiIpNumber;

	@Column(name = "abha_number")
	@JsonProperty("abhaNumber")
	private String abhaNumber;

	@Column(name = "aadhaar_number")
	@JsonProperty("aadhaarNumber")
	private String aadhaarNumber;
	
	@JsonProperty("prefixMasterDB")
	@Column(name = "prefix_master_id")
	private Long prefixMasterDB;
	
	@JsonProperty("roleMasterId")
	@Column(name = "role_master_id")
	private Long roleMasterId;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "emr_registration_patient_info_communication_mapping", joinColumns = {@JoinColumn(name = "patient_Id")}, inverseJoinColumns = {@JoinColumn(name = "communication_Info_Id")})
	private Set <CommunicationInfoDB> communicationInfoDB = new HashSet <>();

	@JsonManagedReference
	@JsonProperty("patientRegistrations")
	@OneToMany(mappedBy = "patientDetailDB", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	private List <PatientConsultationDB> patientRegistrations = new ArrayList <>();

	@JsonManagedReference
	@JsonProperty("patientEmergencyContacts")
	@OneToMany(mappedBy = "patientDetailDB", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	private List <PatientEmergencyContacts> patientEmergencyContacts = new ArrayList <>();

	@JsonManagedReference
	@JsonProperty("patientInsuranceDetails")
	@OneToMany(mappedBy = "patientDetailDB", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	private List <PatientInsuranceDB> patientInsuranceDetails = new ArrayList <>();

	@JsonManagedReference
	@JsonProperty("patientMrnLinks")
	@OneToMany(mappedBy = "patientDetailDB", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	private List <PatientMrnLinkDB> patientMrnLinks = new ArrayList <>();
	
	@JsonManagedReference
	@JsonProperty("patientAppointments")
	@OneToMany(mappedBy = "patientDetailDB", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	private List <PatientAppointmentDB> patientAppointments = new ArrayList <>();
	
}
