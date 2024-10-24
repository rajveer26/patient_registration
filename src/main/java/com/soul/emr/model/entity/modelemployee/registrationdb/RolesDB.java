package com.soul.emr.model.entity.modelemployee.registrationdb;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import com.soul.emr.model.entity.masterentity.masterdb.RoleMasterDB;
import com.soul.emr.model.entity.modelpatient.patientregistrationdb.PatientDetailsDB;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = false, exclude = {"employeeInfoDB", "patientDetailsDBSet"})
@Entity
@Table(name = "EMR_Roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "rolesId", scope = RolesDB.class)
public class RolesDB extends WhoseColumnsEntity implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "user_roles")
	@SequenceGenerator(name = "user_roles", sequenceName = "user_roles", allocationSize = 1)
	@JsonProperty("rolesId")
	@Column(name = "roles_Id")
	private Long rolesId;
	
	@JsonProperty("roleMaster")
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "role", referencedColumnName = "role_Master_Id")
	private RoleMasterDB roleMaster;
	
	@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set <EmployeeInfoDB> employeeInfoDB = new HashSet <>();
	
	@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set <PatientDetailsDB> patientDetailsDBSet = new HashSet <>();
	
}
