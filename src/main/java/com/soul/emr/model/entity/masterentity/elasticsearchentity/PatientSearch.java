package com.soul.emr.model.entity.masterentity.elasticsearchentity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import com.soul.emr.model.entity.enummaster.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "patient_search_master")
public class PatientSearch extends WhoseColumnsEntity implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	@JsonProperty("patientId")
	@Field(name = "patient_id")
	private Long patientId;
	
	@Field(name = "patient_name")
	@JsonProperty("patientName")
	private String patientName;
	
	@Enumerated(EnumType.STRING)
	@Field(name = "gender")
	@JsonProperty("gender")
	private Gender gender;
	
	@Field(name = "mrno")
	@JsonProperty("mrno")
	private String mrno;
	
	@Field(name = "dob")
	@JsonProperty("dob")
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private LocalDate dob;
	
	@Field(name = "registered_on")
	@JsonProperty("registeredOn")
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private LocalDate registeredOn;
	
	@Field(name = "plan_name")
	@JsonProperty("planName")
	private String planName;
	
	@Field(type = FieldType.Nested, includeInParent = true)
	private List <CommunicationSearch> communicationInfoDB = new ArrayList <>();
}
