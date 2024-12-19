package com.soul.emr.model.entity.modelpatient.patientregistrationdb;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = false, exclude = {"patientDetailDB"})
@Entity
@Table(name = "EMR_REGISTRATION_PATIENT_APPOINTMENTS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientAppointmentDB extends WhoseColumnsEntity implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_patient_self_appointments")
    @SequenceGenerator(name = "seq_patient_self_appointments", sequenceName = "seq_patient_self_appointments", allocationSize = 1)
    @JsonProperty("patientConsultationId")
    @Column(name = "patient_appointment_id")
    private Long patientAppointmentId;

    @JsonProperty("appointmentType")
    @Column(name = "appointment_type")
    private String appointmentType;

    @Column(name = "appointment_date")
    @JsonProperty("appointmentDate")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDate appointmentDate;

    @JsonProperty("isActive")
    @Column(name = "is_active")
    private Boolean isActive;

    @JsonProperty("departmentMasterDB")
    @Column(name = "department_master_id")
    private Long departmentMasterId;

    @JsonProperty("employeeInfoDB")
    @Column(name = "consultant_id")
    private Long employeeInfoDB;

    @JsonBackReference
    @JsonProperty("patientDetailDB")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_Id", referencedColumnName = "patient_Id")
    private PatientDetailsDB patientDetailDB;
}
