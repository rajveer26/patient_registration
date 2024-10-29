package com.soul.emr.model.entity.abhaentity.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbhaTokensResponse {

    @JsonProperty("token")
    private String token;

    @JsonProperty("expiresIn")
    private Integer expiresIn;

    @JsonProperty("refreshToken")
    private String refreshToken;

    @JsonProperty("refreshExpiresIn")
    private Integer refreshExpiresIn;


}
