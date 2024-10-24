package com.soul.emr.model.entity.modelpatient.patientregistrationdb;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import com.soul.emr.model.entity.communication.communicationinfodb.CommunicationInfoDB;
import com.soul.emr.model.entity.enummaster.Gender;
import com.soul.emr.model.entity.masterentity.masterdb.PrefixMasterDB;
import com.soul.emr.model.entity.modelemployee.registrationdb.RolesDB;
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

@EqualsAndHashCode(callSuper = false, exclude = {"roles", "patientRegistrations", "communicationInfoDB"})
@Entity
@Table(name = "patient_details")
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
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_patient_registration")
	@SequenceGenerator(name = "seq_patient_registration", sequenceName = "seq_patient_registration", allocationSize = 1)
	@JsonProperty("patientId")
	@Column(name = "patient_Id")
	private Long patientId;
	
	@Column(name = "patient_name", nullable = false)
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
	
	@Column(name = "plan_name")
	@JsonProperty("planName")
	private String planName;
	
	@JsonProperty("prefixMasterDB")
	@ManyToOne(fetch = FetchType.LAZY)
	private PrefixMasterDB prefixMasterDB;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "patient_info_role_mapping", joinColumns = {@JoinColumn(name = "patient_Id")}, inverseJoinColumns = {@JoinColumn(name = "roles_Id")})
	private Set <RolesDB> roles = new HashSet <>();
	
	@JsonManagedReference
	@JsonProperty("communicationInfoDB")
	@OneToMany(mappedBy = "patientDetailsDB", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	private List <CommunicationInfoDB> communicationInfoDB = new ArrayList <>();
	
	@JsonManagedReference
	@JsonProperty("patientRegistrations")
	@OneToMany(mappedBy = "patientDetailDB", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	private List <PatientConsultationDB> patientRegistrations = new ArrayList <>();
	
}
