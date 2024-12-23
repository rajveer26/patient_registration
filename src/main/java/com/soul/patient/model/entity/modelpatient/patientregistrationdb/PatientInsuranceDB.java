package com.soul.patient.model.entity.modelpatient.patientregistrationdb;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.soul.patient.model.entity.commonentity.WhoseColumnsEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = false, exclude = {"patientDetailDB"})
@Entity
@Table(name = "EMR_PATIENT_INSURANCE_INFO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientInsuranceDB extends WhoseColumnsEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "emr_patient_insurance_info_seq")
    @SequenceGenerator(name = "emr_patient_insurance_info_seq", sequenceName = "emr_patient_insurance_info_seq", allocationSize = 1)
    @JsonProperty("patientInsuranceInfoId")
    @Column(name = "patient_insurance_info_id", length = 19)
    private Long patientInsuranceInfoId;

    @JsonProperty("insuranceType")
    @Column(name = "insurance_type")
    private String insuranceType;

    @JsonProperty("insuranceName")
    @Column(name = "insurance_name")
    private String insuranceName;

    @JsonProperty("insuranceNumber")
    @Column(name = "insurance_number")
    private String insuranceNumber;

    @JsonProperty("insuranceValidFrom")
    @Column(name = "insurance_valid_from")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime insuranceValidFrom;

    @JsonProperty("insuranceValidTo")
    @Column(name = "insurance_valid_to")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime insuranceValidTo;

    @JsonBackReference
    @JsonProperty("patientDetailDB")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_Id", referencedColumnName = "patient_Id")
    private PatientDetailsDB patientDetailDB;
}
