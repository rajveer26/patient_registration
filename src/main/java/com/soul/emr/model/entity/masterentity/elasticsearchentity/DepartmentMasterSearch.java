package com.soul.emr.model.entity.masterentity.elasticsearchentity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
public class DepartmentMasterSearch extends WhoseColumnsEntity implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	@JsonProperty("departmentMasterId")
	@Field(name = "department_master_id")
	private Long departmentMasterId;
	
	@JsonProperty("departmentId")
	@Field(name = "department_id")
	private Long departmentId;
	
	@JsonProperty("departmentName")
	@Field(name = "department_name")
	private String departmentName;
	
	@JsonProperty("headOfDepartment")
	@Field(name = "head_of_department")
	private String headOfDepartment;
	
	@JsonProperty("departmentType")
	@Field(name = "department_type")
	private String departmentType;
	
	@JsonProperty("isDeleted")
	@Field(name = "is_deleted")
	private Boolean isDeleted;

}
