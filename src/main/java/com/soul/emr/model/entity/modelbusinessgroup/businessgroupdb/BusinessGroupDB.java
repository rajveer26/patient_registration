package com.soul.emr.model.entity.modelbusinessgroup.businessgroupdb;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import com.soul.emr.model.entity.modelbusinessgroup.organization.organizationdb.OrganizationDB;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "emr_business_Group")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BusinessGroupDB extends WhoseColumnsEntity implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "business_group_seq")
	@SequenceGenerator(name = "business_group_seq", sequenceName = "business_group_seq", allocationSize = 1)
	@JsonProperty("businessGroupId")
	@Column(name = "business_Group_Id")
	private Long businessGroupId;
	
	@JsonProperty("businessGroupCode")
	@Column(name = "business_group_code", length = 50, unique = true, nullable = false)
	private String businessGroupCode;
	
	@JsonProperty("businessGroupName")
	@Column(name = "business_Group_Name",length = 50, nullable = false)
	private String businessGroupName;
	
	@JsonProperty("businessGroupTIN")
	@Column(name = "business_Group_TIN", length = 50, unique = true, nullable = false)
	private String businessGroupTIN;
	
	@JsonProperty("businessGroupGSTNumber")
	@Column(name = "business_Group_GST_Number", length = 50, unique = true, nullable = false)
	private String businessGroupGSTNumber;
	
	@JsonProperty("isActive")
	@Column(name = "is_active", nullable = false)
	private Boolean isActive;
	
	@OneToMany(mappedBy = "businessGroupDB", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	private List<OrganizationDB> organizationDBList = new ArrayList <>();
	
	
}
