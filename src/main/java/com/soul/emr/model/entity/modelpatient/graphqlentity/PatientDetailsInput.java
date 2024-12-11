package com.soul.emr.model.entity.modelpatient.graphqlentity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import com.soul.emr.model.entity.communication.graphqlentity.CommunicationInfoInput;
import com.soul.emr.model.entity.enummaster.Gender;
import com.soul.emr.model.entity.masterentity.graphqlentity.PrefixMasterInput;
import com.soul.emr.model.entity.modelemployee.graphqlentity.RoleInput;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDetailsInput extends WhoseColumnsEntity
{

	@JsonProperty("patientId")
	private Long patientId;

	@JsonProperty("mrno")
	private String mrno;

	@NotBlank(message = "PATIENT FIRSTNAME CANNOT BE BLANK")
	@JsonProperty("firstName")
	private String firstName;

	@JsonProperty("middleName")
	private String middleName;

	@JsonProperty("lastName")
	private String lastName;

	@JsonProperty("patientName")
	private String patientName;

	@Enumerated(EnumType.STRING)
	@NotBlank(message = "GENDER CANNOT BE BLANK")
	@JsonProperty("gender")
	private Gender gender;

	@JsonProperty("maritalStatus")
	private String maritalStatus;

	@JsonProperty("dob")
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private LocalDate dob;

	@JsonProperty("age")
	private Integer age;

	@JsonProperty("patientImage")
	private String patientImage;

	@JsonProperty("registeredOn")
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private LocalDate registeredOn;

	@JsonProperty("smartCardId")
	private String smartCardId;

	@JsonProperty("esiIpNumber")
	private String esiIpNumber;

	@JsonProperty("abhaNumber")
	private String abhaNumber;

	@JsonProperty("aadhaarNumber")
	private String aadhaarNumber;

	@JsonProperty("prefixMasterDB")
	private PrefixMasterInput prefixMasterDB;

	@JsonProperty("roles")
	private Set <RoleInput> roles = new HashSet <>();

	@JsonProperty("communicationInfoDB")
	private Set <CommunicationInfoInput> communicationInfoDB = new HashSet <>();

	@JsonProperty("patientRegistrations")
	private List <PatientConsultationInput> patientRegistrations = new ArrayList <>();

	@JsonProperty("patientEmergencyContacts")
	private List <PatientEmergencyContactInput> patientEmergencyContacts = new ArrayList <>();

	@JsonProperty("patientInsuranceDetails")
	private List <patientInsuranceDetailInput> patientInsuranceDetails = new ArrayList <>();

	@JsonProperty("patientMrnLinks")
	private List <patientMrnLinkInput> patientMrnLinks = new ArrayList <>();

	@JsonProperty("patientAppointments")
	private List <PatientAppointmentInput> patientAppointments = new ArrayList <>();
}
