package com.soul.emr.model.entity.modelpatient.patientregistrationdb;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = false, exclude = {"patientDetailDB"})
@Entity
@Table(name = "EMR_TXN_REGISTRATION_EMERGENCY_CONTACT_INFO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientEmergencyContacts extends WhoseColumnsEntity implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 1L;
    
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
