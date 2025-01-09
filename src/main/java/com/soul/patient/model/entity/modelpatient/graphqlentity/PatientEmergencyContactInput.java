package com.soul.patient.model.entity.modelpatient.graphqlentity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.soul.patient.model.entity.commonentity.WhoseColumnsEntity;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = false, exclude = {"patientDetailsInput"})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientEmergencyContactInput extends WhoseColumnsEntity{
    
    @JsonProperty("emergencyContactInfoId")
    private Long emergencyContactInfoId;

    @JsonProperty("contactPersonRelation")
    private String contactPersonRelation;

    @JsonProperty("contactPersonName")
    private String contactPersonName;

    @JsonProperty("contactPersonMobileNumber")
    private String contactPersonMobileNumber;

    @JsonProperty("contactPersonWhatsappNumber")
    private String contactPersonWhatsappNumber;

    @JsonProperty("contactPersonEmailId")
    @Email
    private String contactPersonEmailId;
    
    @JsonProperty("patientDetailDB")
    private PatientDetailsInput patientDetailsInput;
}
