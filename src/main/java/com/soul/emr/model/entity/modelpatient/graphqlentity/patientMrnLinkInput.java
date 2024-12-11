package com.soul.emr.model.entity.modelpatient.graphqlentity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = false)
@Data
public class patientMrnLinkInput {

    @JsonProperty("patientMrnLinkId")
    private Long patientMrnLinkId;

    @JsonProperty("familyMrn")
    private String familyMrn;

    @JsonProperty("familyMrnRelation")
    private String familyMrnRelation;
}
