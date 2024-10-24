package com.soul.emr.model.entity.modelpatient.graphqlentity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import com.soul.emr.model.entity.modelbusinessgroup.organization.graphqlentity.OrganizationGroupInput;
import com.soul.emr.model.entity.modelemployee.graphqlentity.EmployeeInfoInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientConsultationInput extends WhoseColumnsEntity
{
	
	@JsonProperty("patientConsultationId")
	private Long patientConsultationId;
	
	@JsonProperty("visitId")
	private Integer visitId;
	
	@JsonProperty("visitNo")
	private String visitNo;
	
	@JsonProperty("type")
	private String type;
	
	@JsonProperty("status")
	private String status;
	
	@JsonProperty("encounterId")
	private Integer encounterId;
	
	@JsonProperty("serviceCenterId")
	private Integer serviceCenterId;
	
	@JsonProperty("storeCode")
	private String storeCode;
	
	@JsonProperty("storeId")
	private Integer storeId;
	
	@JsonProperty("visitTypeId")
	private Integer visitTypeId;
	
	@JsonProperty("visitTypeCode")
	private String visitTypeCode;
	
	@JsonProperty("encounterDate")
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private LocalDate encounterDate;
	
	@JsonProperty("unitHead")
	private String unitHead;
	
	@JsonProperty("unitName")
	private String unitName;
	
	@JsonProperty("pvsId")
	private Long pvsId;
	
	@JsonProperty("branchName")
	private String branchName;
	
	@JsonProperty("consultationStatus")
	private String consultationStatus;
	
	@JsonProperty("branchCode")
	private String branchCode;
	
	@JsonProperty("patientStatus")
	private String patientStatus;
	
	@JsonProperty("queueNo")
	private String queueNo;
	
	@JsonProperty("consultationType")
	private String consultationType;
	
	@JsonProperty("isDeleted")
	private Boolean isDeleted;
	
	@JsonProperty("siteId")
	private OrganizationGroupInput siteId;
	
	@JsonProperty("patientDetailDB")
	private PatientDetailsInput patientDetailsInput;
	
	@JsonProperty("employeeInfoDB")
	private EmployeeInfoInput employeeInfoDB;
}
