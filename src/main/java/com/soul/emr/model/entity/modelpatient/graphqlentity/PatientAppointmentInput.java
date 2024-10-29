package com.soul.emr.model.entity.modelpatient.graphqlentity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import com.soul.emr.model.entity.masterentity.graphqlentity.DepartmentMasterInput;
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
public class PatientAppointmentInput extends WhoseColumnsEntity {


    @JsonProperty("patientConsultationId")
    private Long patientAppointmentId;

    @JsonProperty("appointmentDate")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDate appointmentDate;

    @JsonProperty("isActive")
    private Boolean isActive;

    @JsonProperty("departmentMasterDB")
    private DepartmentMasterInput departmentMasterInput;

    @JsonProperty("employeeInfoDB")
    private EmployeeInfoInput employeeInfoInput;

    @JsonProperty("patientDetailDB")
    private PatientDetailsInput patientDetailsInput;
}
