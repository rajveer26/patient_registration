package com.soul.patient.model.entity.modelpatient.graphqlentity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.soul.patient.model.entity.commonentity.WhoseColumnsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = false, exclude = {"patientDetailsInput"})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientMrnLinkInput extends WhoseColumnsEntity{
    
    @JsonProperty("patientMrnLinkId")
    private Long patientMrnLinkId;

    @JsonProperty("familyMrn")
    private String familyMrn;

    @JsonProperty("familyMrnRelation")
    private String familyMrnRelation;
    
    @JsonProperty("patientDetailDB")
    private PatientDetailsInput patientDetailsInput;
}
