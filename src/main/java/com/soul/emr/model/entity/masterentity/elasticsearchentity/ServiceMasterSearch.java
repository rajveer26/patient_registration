package com.soul.emr.model.entity.masterentity.elasticsearchentity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import com.soul.emr.model.entity.enummaster.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "service_master_search")
public class ServiceMasterSearch extends WhoseColumnsEntity implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	@JsonProperty("serviceMasterId")
	@Field(name = "service_master_id")
	private Long serviceMasterId;
	
	@JsonProperty("serviceMasterCode")
	@Field(name = "service_master_code")
	private String serviceMasterCode;
	
	@JsonProperty("serviceMasterName")
	@Field(name = "service_master_name")
	private String serviceMasterName;
	
	@JsonProperty("serviceType")
	@Field(name = "service_type")
	private String serviceType;
	
	@JsonProperty("isActive")
	@Field(name = "is_active")
	private Boolean isActive;
	
	@JsonProperty("serviceGroup")
	@Field(name = "service_group")
	private String serviceGroup;
	
	@JsonProperty("serviceCategory")
	@Field(name = "service_category")
	private String serviceCategory;
	
	@Enumerated(EnumType.STRING)
	@JsonProperty("applicableGender")
	@Field(name = "applicable_gender")
	private Gender applicableGender;
	
	@JsonProperty("applicableVisitType")
	@Field(name = "applicable_visit_type")
	private String applicableVisitType;
	
	@JsonProperty("isAutoProcess")
	@Field(name = "is_auto_process")
	private Boolean isAutoProcess;
	
	@JsonProperty("sendSMS")
	@Field(name = "send_sms")
	private Boolean sendSMS;
	
	@JsonProperty("departmentMasterDBSet")
	@Field(type = FieldType.Nested, includeInParent = true)
	private List <DepartmentMasterSearch> departmentMasterDBSet = new ArrayList <>();
	
}
