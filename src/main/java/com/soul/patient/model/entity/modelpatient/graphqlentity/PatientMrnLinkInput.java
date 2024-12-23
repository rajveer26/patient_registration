package com.soul.patient.model.entity.modelpatient.graphqlentity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.soul.patient.model.entity.commonentity.WhoseColumnsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientMrnLinkInput extends WhoseColumnsEntity implements Serializable{
    
    @Serial
    private static final long serialVersionUID = 1L;
    
    @JsonProperty("patientMrnLinkId")
    private Long patientMrnLinkId;

    @JsonProperty("familyMrn")
    private String familyMrn;

    @JsonProperty("familyMrnRelation")
    private String familyMrnRelation;
}
