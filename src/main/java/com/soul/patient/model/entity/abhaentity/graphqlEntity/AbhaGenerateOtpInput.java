package com.soul.patient.model.entity.abhaentity.graphqlEntity;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbhaGenerateOtpInput {

    @JsonProperty("loginId")
    private String loginId;

}
