package com.soul.emr.model.entity.masterentity.masterdb;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "emr_fnd_privileges")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrivilegesMasterDB extends WhoseColumnsEntity implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "user_privileges")
	@SequenceGenerator(name = "user_privileges", sequenceName = "user_privileges", allocationSize = 1)
	@JsonProperty("privilegesId")
	@Column(name = "privileges_Id")
	private Long privilegesId;
	
	@JsonProperty("privilegesName")
	@Column(name = "privileges_Name", length = 30)
	private String privilegesName;
	
	@JsonProperty("privilegesCode")
	@Column(name = "privileges_Code", length = 30)
	private String privilegesCode;
	
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "roles_master_Id", referencedColumnName = "role_master_id")
	private RoleMasterDB rolesMasterDB;
	
	
}
