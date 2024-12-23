package com.soul.patient.model.entity.abhaentity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbhaGenerateOtpResponse {

    @JsonProperty("txnId")
    private String txnId;

    @JsonProperty("message")
    private String message;
}
