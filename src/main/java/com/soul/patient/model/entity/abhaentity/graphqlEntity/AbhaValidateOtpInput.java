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
public class AbhaValidateOtpInput {

    @JsonProperty("mobileNo")
    private String mobileNo;

    @JsonProperty("txnId")
    private String txnId;

    @JsonProperty("otp")
    private String otp;

}