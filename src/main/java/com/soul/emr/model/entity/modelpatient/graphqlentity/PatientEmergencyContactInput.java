package com.soul.emr.model.entity.modelpatient.graphqlentity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = false)
@Data
public class PatientEmergencyContactInput extends WhoseColumnsEntity implements Serializable{
    
    @Serial
    private static final long serialVersionUID = 1L;
    
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
}
