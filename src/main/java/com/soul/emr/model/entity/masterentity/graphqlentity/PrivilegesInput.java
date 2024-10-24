package com.soul.emr.model.entity.masterentity.graphqlentity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class PrivilegesInput extends WhoseColumnsEntity
{
	@JsonProperty("privilegesId")
	private Long privilegesId;
	
	@JsonProperty("privilegesName")
	private String privilegesName;
	
	@JsonProperty("privilegesCode")
	private String privilegesCode;
	
	@JsonProperty("rolesMasterDB")
	private RoleMasterInput rolesMasterDB;
	
}
