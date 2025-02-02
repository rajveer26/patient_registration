package com.soul.patient.model.entity.modelpatient.graphqlentity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.soul.patient.model.entity.commonentity.WhoseColumnsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = false, exclude = {"patientDetailsInput"})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientInsuranceDetailInput extends WhoseColumnsEntity{
    
    
    @JsonProperty("patientInsuranceInfoId")
    private Long patientInsuranceInfoId;

    @JsonProperty("insuranceType")
    private String insuranceType;

    @JsonProperty("insuranceName")
    private String insuranceName;

    @JsonProperty("insuranceNumber")
    private String insuranceNumber;

    @JsonProperty("insuranceValidFrom")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime insuranceValidFrom;

    @JsonProperty("insuranceValidTo")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime insuranceValidTo;
    
    @JsonProperty("patientDetailDB")
    private PatientDetailsInput patientDetailsInput;
}
