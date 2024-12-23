package com.soul.patient.model.entity.modelonetimepassword.onetimepassworddb;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.soul.patient.model.entity.commonentity.WhoseColumnsEntity;
import com.soul.patient.model.entity.communication.communicationinfodb.CommunicationInfoDB;
import com.soul.patient.model.entity.enummaster.Medium;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "PATIENT_TXN_REGISTRATION_OTP")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OneTimePasswordEntityDB extends WhoseColumnsEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "seq_registration_otp")
    @SequenceGenerator(name = "seq_registration_otp", sequenceName = "seq_registration_otp", allocationSize = 1)
    private Long otpId;
    
    @JsonProperty("identifier")
    @Column(name = "identifier", nullable = false)
    private String identifier;
    
    @Enumerated(EnumType.STRING)
    @JsonProperty("medium")
    @Column(name = "medium", nullable = false)
    private Medium medium;
    
    @JsonProperty("otp")
    @Column(name = "otp", nullable = false)
    private String otp;
    
    @JsonProperty("isValidated")
    @Column(name = "is_validated")
    private Boolean isValidated;
    
    @JsonProperty("validUpto")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "valid_upto")
    private LocalDateTime validUpto;
    
    @JsonProperty("communicationInfoDB")
    @OneToOne(fetch = FetchType.LAZY)
    private CommunicationInfoDB communicationInfoDB;
}
