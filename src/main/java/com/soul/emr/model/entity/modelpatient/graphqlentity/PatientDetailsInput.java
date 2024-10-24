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
import jakarta.validation.constraints.NotNull;
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
	
	@NotBlank(message = "patient name cannot be null")
	@JsonProperty("patientName")
	private String patientName;
	
	@Enumerated(EnumType.STRING)
	@NotBlank(message = "gender cannot be blank")
	@JsonProperty("gender")
	private Gender gender;
	
	@NotBlank(message = "mrno cannot be blank")
	@JsonProperty("mrno")
	private String mrno;
	
	@NotNull(message = "dob cannot be blank")
	@JsonProperty("dob")
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private LocalDate dob;
	
	@JsonProperty("patientImage")
	private String patientImage;
	
	@JsonProperty("registeredOn")
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private LocalDate registeredOn;
	
	@JsonProperty("planName")
	private String planName;
	
	@JsonProperty("prefixMasterDB")
	private PrefixMasterInput prefixMasterDB;
	
	@JsonProperty("roles")
	private Set <RoleInput> roles = new HashSet <>();
	
	@JsonProperty("communicationInfoDB")
	private List <CommunicationInfoInput> communicationInfoDB = new ArrayList <>();
	
	@JsonProperty("patientRegistrations")
	private List <PatientConsultationInput> patientRegistrations = new ArrayList <>();
}
