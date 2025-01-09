package com.soul.patient.model.entity.modelpatient.patientregistrationdb;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.soul.patient.model.entity.commonentity.WhoseColumnsEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = false, exclude = {"patientDetailDB"})
@Entity
@Table(name = "EMR_REGISTRATION_EMERGENCY_CONTACT")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientEmergencyContacts extends WhoseColumnsEntity {
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "registration_emergency_contact_info_seq")
    @SequenceGenerator(name = "registration_emergency_contact_info_seq", sequenceName = "registration_emergency_contact_info_seq", allocationSize = 1)
    @JsonProperty("emergencyContactInfoId")
    @Column(name = "emergency_contact_info_id", length = 19)
    private Long emergencyContactInfoId;

    @JsonProperty("contactPersonRelation")
    @Column(name = "contact_Person_Relation")
    private String contactPersonRelation;

    @JsonProperty("contactPersonName")
    @Column(name = "contact_Person_Name", length = 50)
    private String contactPersonName;

    @JsonProperty("contactPersonMobileNumber")
    @Column(name = "contact_Person_Mobile_Number")
    private String contactPersonMobileNumber;

    @JsonProperty("contactPersonWhatsappNumber")
    @Column(name = "contact_Person_Whatsapp_Number")
    private String contactPersonWhatsappNumber;

    @JsonProperty("contactPersonEmailId")
    @Email
    @Column(name = "contact_Person_email_id", length = 50)
    private String contactPersonEmailId;

    @JsonBackReference
    @JsonProperty("patientDetailDB")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_Id", referencedColumnName = "patient_Id")
    private PatientDetailsDB patientDetailDB;
}
