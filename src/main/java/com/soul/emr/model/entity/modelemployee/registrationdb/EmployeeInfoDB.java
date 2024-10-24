package com.soul.emr.model.entity.modelemployee.registrationdb;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import com.soul.emr.model.entity.communication.communicationinfodb.CommunicationInfoDB;
import com.soul.emr.model.entity.masterentity.masterdb.BlockDetailsMasterDB;
import com.soul.emr.model.entity.enummaster.Gender;
import com.soul.emr.model.entity.masterentity.masterdb.DepartmentMasterDB;
import com.soul.emr.model.entity.masterentity.masterdb.PrefixMasterDB;
import com.soul.emr.model.entity.modelbusinessgroup.organization.organizationdb.OrganizationDB;
import com.soul.emr.model.entity.enummaster.Provider;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = false, exclude = {"userCredentialsDB", "roles", "organizationDBSet", "communicationInfoDB", "departmentMasterDBSet", "blockDetailsMasterDBSet", "employeeSchedules"})
@Table(name = "emr_user_info")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userDetailsId", scope = EmployeeInfoDB.class)
public class EmployeeInfoDB extends WhoseColumnsEntity implements Serializable{
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "user_details")
	@SequenceGenerator(name = "user_details", sequenceName = "user_details", allocationSize = 1)
	@JsonProperty("userDetailsId")
	@Column(name = "user_details_Id")
	private Long userDetailsId;
	
	@JsonProperty("fullName")
	@Column(name = "full_name", nullable = false, length = 70)
	private String fullName;
	
	@Enumerated(EnumType.STRING)
	@JsonProperty("gender")
	@Column(name = "gender")
	private Gender gender;
	
	@JsonProperty("doctorCode")
	@Column(name = "doctor_code", unique = true)
	private String doctorCode;
	
	@JsonProperty("dob")
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@Column(name = "dob")
	private LocalDate dob;
	
	@Lob
	@JsonProperty("userSignature")
	@Column(name = "user_signature", columnDefinition = "CLOB")
	private String userSignature;
	
	@Lob
	@JsonProperty("userImage")
	@Column(name = "user_image", columnDefinition = "CLOB")
	private String  userImage;
	
	@JsonProperty("isActive")
	@Column(name = "is_active")
	private Boolean isActive;
	
	
	@Enumerated(EnumType.STRING)
	@JsonProperty("provider")
	@Column(name = "provider")
	private Provider provider;
	
	@JsonProperty("prefixMasterDB")
	@ManyToOne(fetch = FetchType.LAZY)
	private PrefixMasterDB prefixMasterDB;
	
	@JsonManagedReference
	@JsonProperty("communicationInfoDB")
	@OneToMany(mappedBy = "employeeInfoDB", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	private List <CommunicationInfoDB> communicationInfoDB = new ArrayList <>();
	
	@JsonManagedReference
	@OneToOne(mappedBy = "employeeInfo", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private UserCredentialsDB userCredentialsDB;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "emr_employee_info_organization_mapping", joinColumns = {@JoinColumn(name = "user_details_Id")}, inverseJoinColumns = {@JoinColumn(name = "organization_Master_Id")})
	private Set <OrganizationDB> organizationDBSet = new HashSet <>();
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "emr_employee_info_role_mapping", joinColumns = {@JoinColumn(name = "user_details_Id")}, inverseJoinColumns = {@JoinColumn(name = "roles_Id")})
	private Set <RolesDB> roles = new HashSet <>();
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "emr_employee_info_department_mapping", joinColumns = {@JoinColumn(name = "user_details_Id")}, inverseJoinColumns = {@JoinColumn(name = "department_master_Id")})
	private Set <DepartmentMasterDB> departmentMasterDBSet = new HashSet <>();

	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
	@JoinTable(name = "emr_block_details_mapping", joinColumns = {@JoinColumn(name = "user_details_Id")}, inverseJoinColumns = {@JoinColumn(name = "block_Master_Id")})
	private Set <BlockDetailsMasterDB> blockDetailsMasterDBSet = new HashSet<>();

	@JsonManagedReference
	@OneToMany(mappedBy = "employeeInfoDB", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JsonProperty("employeeSchedules")
	private List<EmployeeScheduleDB> employeeSchedules = new ArrayList<>();
}

