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

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = false, exclude = {"patientDetailDB"})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientAppointmentInput extends WhoseColumnsEntity{

    @JsonProperty("patientAppointmentId")
    private Long patientAppointmentId;

    @JsonProperty("appointmentDate")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate appointmentDate;

    @JsonProperty("appointmentType")
    private String appointmentType;

    @JsonProperty("isActive")
    private Boolean isActive;
    
    @JsonProperty("departmentMasterId")
    private Long departmentMasterId;
    
    @JsonProperty("employeeInfoDB")
    private Long employeeInfoDB;

    @JsonProperty("patientDetailDB")
    private PatientDetailsInput patientDetailDB;
}
