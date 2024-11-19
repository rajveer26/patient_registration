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
public class ABHAProfileResponse {


    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("middleName")
    private String middleName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("dob")
    private String dob;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("photo")
    private String photo;

    @JsonProperty("mobile")
    private String mobile;

    @JsonProperty("phrAddress")
    private List<String> phrAddress;

    @JsonProperty("address")
    private String address;

    @JsonProperty("districtCode")
    private String districtCode;

    @JsonProperty("stateCode")
    private String stateCode;

    @JsonProperty("pinCode")
    private String pinCode;

    @JsonProperty("abhaType")
    private String abhaType;

    @JsonProperty("stateName")
    private String stateName;

    @JsonProperty("districtName")
    private String districtName;

    @JsonProperty("ABHANumber")
    private String ABHANumber;

    @JsonProperty("abhaStatus")
    private String abhaStatus;

}
