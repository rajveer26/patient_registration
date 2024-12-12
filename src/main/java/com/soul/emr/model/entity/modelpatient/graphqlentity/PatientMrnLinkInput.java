package com.soul.emr.model.entity.modelpatient.graphqlentity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class PatientMrnLinkInput extends WhoseColumnsEntity {

    @JsonProperty("patientMrnLinkId")
    private Long patientMrnLinkId;

    @JsonProperty("familyMrn")
    private String familyMrn;

    @JsonProperty("familyMrnRelation")
    private String familyMrnRelation;
}
