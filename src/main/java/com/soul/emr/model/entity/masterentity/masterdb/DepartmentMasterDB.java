package com.soul.emr.model.entity.masterentity.masterdb;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import com.soul.emr.model.entity.modelemployee.registrationdb.EmployeeInfoDB;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = false, exclude = {"employeeInfoDB", "PackageServiceMaster"})
@Entity
@Table(name = "department_master")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "departmentMasterId", scope = DepartmentMasterDB.class)
public class DepartmentMasterDB extends WhoseColumnsEntity implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "department_master_seq")
	@SequenceGenerator(name = "department_master_seq", sequenceName = "department_master_seq", allocationSize = 1)
	@JsonProperty("departmentMasterId")
	@Column(name = "department_master_Id")
	private Long departmentMasterId;
	
	@JsonProperty("departmentId")
	@Column(name = "department_id")
	private Long departmentId;
	
	@JsonProperty("departmentName")
	@Column(name = "department_name")
	private String departmentName;
	
	@JsonProperty("headOfDepartment")
	@Column(name = "head_of_department")
	private String headOfDepartment;
	
	@JsonProperty("departmentType")
	@Column(name = "department_Type")
	private String departmentType;
	
	@JsonProperty("isDeleted")
	@Column(name = "is_deleted")
	private Boolean isDeleted;
	
	@ManyToMany(mappedBy = "departmentMasterDBSet", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set <EmployeeInfoDB> employeeInfoDB = new HashSet <>();
	

}
