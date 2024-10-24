package com.soul.emr.model.entity.masterentity.masterdb;

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
@Table(name = "PREFIX_MASTER")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrefixMasterDB extends WhoseColumnsEntity implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_prefix_master")
	@SequenceGenerator(name = "seq_prefix_master", sequenceName = "seq_prefix_master", allocationSize = 1)
	@JsonProperty("prefixMasterId")
	@Column(name = "prefix_master_id")
	private Long prefixMasterId;
	
	@JsonProperty("prefixName")
	@Column(name = "PREFIX_NAME")
	private String prefixName;
	
	@JsonProperty("isActive")
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
}
