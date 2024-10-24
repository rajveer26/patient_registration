package com.soul.emr.model.entity.masterentity.masterdb;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import com.soul.emr.model.entity.modelemployee.registrationdb.EmployeeInfoDB;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "Emr_Block_details_Master")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "blockMasterId", scope = BlockDetailsMasterDB.class)
public class BlockDetailsMasterDB extends WhoseColumnsEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_block_details_master")
    @SequenceGenerator(name = "seq_block_details_master", sequenceName = "seq_block_details_master", allocationSize = 1)
    @JsonProperty("blockMasterId")
    @Column(name = "block_Master_Id")
    private Long blockMasterId;

    @JsonProperty("fromTime")
    @Column(name = "from_Time")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime fromTime;

    @JsonProperty("toTime")
    @Column(name = "to_Time")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime toTime;

    @JsonProperty("blockName")
    @Column(name = "block_Name")
    private String blockName;

    @ManyToMany(mappedBy = "blockDetailsMasterDBSet", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set <EmployeeInfoDB> employeeInfoDB = new HashSet<>();
}
