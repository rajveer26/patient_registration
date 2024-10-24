package com.soul.emr.model.entity.modelemployee.registrationdb;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = false)
@Table(name = "EMR_EMPLOYEE_SCHEDULE")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeScheduleDB extends WhoseColumnsEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "emp_schedule")
    @SequenceGenerator(name = "emp_schedule", sequenceName = "emp_schedule", allocationSize = 1)
    @JsonProperty("empScheduleId")
    @Column(name = "emp_Schedule_Id")
    private Long empScheduleId;

    @JsonProperty("fromTime")
    @Column(name = "from_Time")
    private LocalDateTime fromTime;

    @JsonProperty("toTime")
    @Column(name = "to_Time")
    private LocalDateTime toTime;

    @JsonProperty("scheduleDate")
    @Column(name = "schedule_Date")
    private LocalDate scheduleDate;

    @JsonProperty("isActive")
    @Column(name = "is_Active")
    private Boolean isActive;

    @JsonProperty("notes")
    @Column(name = "notes")
    private String notes;

    @JsonProperty("title")
    @Column(name = "title")
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_details_Id", referencedColumnName = "user_details_Id", nullable = false)
    @JsonBackReference
    @JsonProperty("employeeInfoDB")
    private EmployeeInfoDB employeeInfoDB;

}
