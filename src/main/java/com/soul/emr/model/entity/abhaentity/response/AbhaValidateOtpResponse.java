package com.soul.emr.model.entity.abhaentity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbhaValidateOtpResponse {

    @JsonProperty("txnId")
    private String txnId;

    @JsonProperty("message")
    private String message;

    @JsonProperty("tokens")
    private AbhaTokensResponse tokens;

    @JsonProperty("ABHAProfile")
    private ABHAProfileResponse abhaProfile;

    @JsonProperty("isNew")
    private boolean isNew;

    @JsonProperty("imageBase64String")
    private String imageBase64String;

    @JsonProperty("imageByte")
    private List<Byte> imageByte;
}
