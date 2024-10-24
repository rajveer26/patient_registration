package com.soul.emr.model.entity.modelpatient.patientregistrationdb;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import com.soul.emr.model.entity.modelbusinessgroup.organization.organizationdb.OrganizationDB;
import com.soul.emr.model.entity.modelemployee.registrationdb.EmployeeInfoDB;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "patient_consultations")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientConsultationDB extends WhoseColumnsEntity implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_patient_registration")
	@SequenceGenerator(name = "seq_patient_registration", sequenceName = "seq_patient_registration", allocationSize = 1)
	@JsonProperty("patientConsultationId")
	@Column(name = "patient_consultation_id")
	private Long patientConsultationId;
	
	@Column(name = "visit_id", nullable = false)
	@JsonProperty("visitId")
	private Integer visitId;
	
	@Column(name = "visit_no", nullable = false)
	@JsonProperty("visitNo")
	private String visitNo;
	
	@Column(name = "type", nullable = false)
	@JsonProperty("type")
	private String type;
	
	@Column(name = "status")
	@JsonProperty("status")
	private String status;
	
	@Column(name = "encounter_id", nullable = false)
	@JsonProperty("encounterId")
	private Integer encounterId;
	
	@Column(name = "service_center_id")
	@JsonProperty("serviceCenterId")
	private Integer serviceCenterId;
	
	@Column(name = "store_code")
	@JsonProperty("storeCode")
	private String storeCode;
	
	@Column(name = "store_id")
	@JsonProperty("storeId")
	private Integer storeId;
	
	@Column(name = "visit_type_id")
	@JsonProperty("visitTypeId")
	private Integer visitTypeId;
	
	@Column(name = "visit_type_code")
	@JsonProperty("visitTypeCode")
	private String visitTypeCode;
	
	@Column(name = "encounter_date")
	@JsonProperty("encounterDate")
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private LocalDate encounterDate;
	
	@Column(name = "unit_head")
	@JsonProperty("unitHead")
	private String unitHead;
	
	@Column(name = "unit_name")
	@JsonProperty("unitName")
	private String unitName;
	
	@Column(name = "pvs_id")
	@JsonProperty("pvsId")
	private Long pvsId;
	
	@Column(name = "branch_name")
	@JsonProperty("branchName")
	private String branchName;
	
	@Column(name = "consultation_status")
	@JsonProperty("consultationStatus")
	private String consultationStatus;
	
	@Column(name = "branch_code")
	@JsonProperty("branchCode")
	private String branchCode;
	
	@Column(name = "patient_status")
	@JsonProperty("patientStatus")
	private String patientStatus;
	
	@Column(name = "queue_no")
	@JsonProperty("queueNo")
	private String queueNo;
	
	@Column(name = "consultation_type")
	@JsonProperty("consultationType")
	private String consultationType;
	
	@JsonProperty("isDeleted")
	@Column(name = "is_deleted")
	private Boolean isDeleted;
	
	@JsonBackReference
	@JsonProperty("patientDetailDB")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_Id", referencedColumnName = "patient_Id")
	private PatientDetailsDB patientDetailDB;
	
	@JsonProperty("employeeInfoDB")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DOCTOR_ID", referencedColumnName = "user_details_Id")
	private EmployeeInfoDB employeeInfoDB;
	
	@JsonProperty("siteId")
	@ManyToOne(fetch = FetchType.LAZY)
	private OrganizationDB siteId;
}
