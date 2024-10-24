package com.soul.emr.model.entity.modelemployee.graphqlentity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordEntity {

    @NotBlank(message = "USER NAME CANNOT BE BLANK")
    @JsonProperty("userName")
    private String userName;

    @NotBlank(message = "OLD PASSWORD CANNOT BE BLANK")
    @JsonProperty("oldPassword")
    private String oldPassword;

    @NotBlank(message = "NEW PASSWORD CANNOT BE BLANK")
    @JsonProperty("newPassword")
    private String newPassword;
}
