package com.soul.emr.model.entity.modelonetimepassword.graphqlentity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import com.soul.emr.model.entity.communication.graphqlentity.CommunicationInfoInput;
import com.soul.emr.model.entity.enummaster.Medium;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OneTimePasswordInput extends WhoseColumnsEntity implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;
    
    @NotBlank(message = "IDENTIFIER CANNOT BE BLANK")
    @JsonProperty("identifier")
    private String identifier;
    
    @NotNull(message = "MEDIUM CANNOT BE BLANK")
    @Enumerated(EnumType.STRING)
    @JsonProperty("medium")
    private Medium medium;
    
    @NotNull(message = "OTP CANNOT BE NULL")
    @JsonProperty("otp")
    private String otp;
    
    @JsonProperty("isValidated")
    private Boolean isValidated;
    
    
    @JsonProperty("validUpto")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime validUpto;
    
    @JsonProperty("communicationInfoDB")
    private CommunicationInfoInput communicationInfoDB;
}
