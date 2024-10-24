package com.soul.emr.model.entity.modelbusinessgroup.organization.organizationdb;

import com.fasterxml.jackson.annotation.*;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import com.soul.emr.model.entity.communication.communicationinfodb.CommunicationInfoDB;
import com.soul.emr.model.entity.modelbusinessgroup.businessgroupdb.BusinessGroupDB;
import com.soul.emr.model.entity.modelemployee.registrationdb.EmployeeInfoDB;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = false, exclude = {"employeeInfoDB", "businessGroupDB", "communicationInfoDB"})
@Entity
@Table(name = "emr_organization_master")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "organizationMasterId", scope = OrganizationDB.class)
public class OrganizationDB extends WhoseColumnsEntity implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "organization_master_seq")
	@SequenceGenerator(name = "organization_master_seq", sequenceName = "organization_master_seq", allocationSize = 1)
	@JsonProperty("organizationMasterId")
	@Column(name = "organization_Master_Id")
	private Long organizationMasterId;
	
	@JsonProperty("organizationName")
	@Column(name = "organization_Name", length = 50)
	private String organizationName;
	
	@JsonProperty("organizationCode")
	@Column(name = "organization_Code", length = 50, nullable = false, unique = true)
	private String organizationCode;
	
	@JsonProperty("organizationLatitude")
	@Column(name = "organization_Latitude", length = 53)
	private Double organizationLatitude;
	
	@JsonProperty("organizationLongitude")
	@Column(name = "organization_Longitude", length = 53)
	private Double organizationLongitude;
	
	@JsonManagedReference
	@JsonProperty("communicationInfoDB")
	@OneToMany(mappedBy = "organizationDB", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	private List <CommunicationInfoDB> communicationInfoDB = new ArrayList <>();
	
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "business_group_Id", referencedColumnName = "business_Group_Id")
	private BusinessGroupDB businessGroupDB;
	
	@ManyToMany(mappedBy = "organizationDBSet", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set <EmployeeInfoDB> employeeInfoDB = new HashSet <>();
	
	
}
