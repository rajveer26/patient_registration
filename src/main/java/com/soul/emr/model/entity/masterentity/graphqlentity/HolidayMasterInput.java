package com.soul.emr.model.entity.masterentity.graphqlentity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HolidayMasterInput extends WhoseColumnsEntity
{
    @JsonProperty("holidayMasterId")
    private Long holidayMasterId;

    @JsonProperty("holiday_date")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDate holidayDate;

    @JsonProperty("typeOfHoliday")
    private String typeOfHoliday;

    @JsonProperty("occasion")
    private String occasion;

    @JsonProperty("isPublicHoliday")
    private Boolean isPublicHoliday;

    @JsonProperty("isApplicableToAllSites")
    private Boolean isApplicableToAllSites;
}