package com.soul.emr.model.entity.modelemployee.graphqlentity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeScheduleInput extends WhoseColumnsEntity {

    @JsonProperty("empScheduleId")
    private Long empScheduleId;

    @JsonProperty("fromTime")
    private LocalDateTime fromTime;

    @JsonProperty("toTime")
    private LocalDateTime toTime;

    @JsonProperty("date")
    private LocalDate date;

    @JsonProperty("isActive")
    private Boolean isActive;

    @JsonProperty("notes")
    private String notes;

    @JsonProperty("title")
    private String title;

    @JsonProperty("employeeInfoDB")
    private EmployeeInfoInput employeeInfoDB;

}