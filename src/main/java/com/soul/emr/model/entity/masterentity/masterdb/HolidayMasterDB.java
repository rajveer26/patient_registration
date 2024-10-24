package com.soul.emr.model.entity.masterentity.masterdb;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "Emr_Holiday_Master")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HolidayMasterDB extends WhoseColumnsEntity implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_holiday_master")
    @SequenceGenerator(name = "seq_holiday_master", sequenceName = "seq_holiday_master", allocationSize = 1)
    @JsonProperty("holidayMasterId")
    @Column(name = "holiday_Id")
    private Long holidayMasterId;

    @JsonProperty("holiday_date")
    @Column(name = "holiday_Date")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDate holidayDate;

    @JsonProperty("typeOfHoliday")
    @Column(name = "type_Of_Holiday")
    private String typeOfHoliday;

    @JsonProperty("occasion")
    @Column(name = "occasion")
    private String occasion;

    @JsonProperty("isPublicHoliday")
    @Column(name = "is_Public_Holiday")
    private Boolean isPublicHoliday;

    @JsonProperty("isApplicableToAllSites")
    @Column(name = "is_Applicable_To_All_Sites")
    private Boolean isApplicableToAllSites;
}