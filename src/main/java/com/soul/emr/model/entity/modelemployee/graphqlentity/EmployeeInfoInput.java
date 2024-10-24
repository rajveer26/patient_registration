package com.soul.emr.model.entity.modelemployee.graphqlentity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import com.soul.emr.model.entity.communication.graphqlentity.CommunicationInfoInput;
import com.soul.emr.model.entity.masterentity.graphqlentity.BlockDetailsMasterInput;
import com.soul.emr.model.entity.enummaster.Gender;
import com.soul.emr.model.entity.masterentity.graphqlentity.DepartmentMasterInput;
import com.soul.emr.model.entity.masterentity.graphqlentity.PrefixMasterInput;
import com.soul.emr.model.entity.modelbusinessgroup.organization.graphqlentity.OrganizationGroupInput;
import com.soul.emr.model.entity.enummaster.Provider;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = false)
@Data
public class EmployeeInfoInput extends WhoseColumnsEntity{
	
	@JsonProperty("userDetailsId")
	private Long userDetailsId;
	
	@NotNull
	@JsonProperty("fullName")
	private String fullName;
	
	@Enumerated(EnumType.STRING)
	@JsonProperty("gender")
	private Gender gender;
	
	@JsonProperty("doctorCode")
	private String doctorCode;
	
	@JsonProperty("dob")
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate dob;
	
	@JsonProperty("userSignature")
	private String userSignature;
	
	@JsonProperty("userImage")
	private String  userImage;
	
	@JsonProperty("isActive")
	private Boolean isActive;
	
	@Enumerated(EnumType.STRING)
	@JsonProperty("provider")
	private Provider provider;
	
	@JsonProperty("prefixMasterDB")
	private PrefixMasterInput prefixMasterDB;
	
	@JsonProperty("communicationInfoDB")
	private List <CommunicationInfoInput> communicationInfoDB = new ArrayList <>();
	
	@NotNull
	@JsonProperty("userCredentialsInput")
	private UserCredentialsInput userCredentialsInput;
	
	@JsonProperty("roles")
	private Set <RoleInput> roles = new HashSet <>();
	
	@JsonProperty("organizationDBSet")
	private Set <OrganizationGroupInput> organizationDBSet = new HashSet <>();
	
	@JsonProperty("departmentMasterDBSet")
	private Set <DepartmentMasterInput> departmentMasterDBSet = new HashSet <>();

	@JsonProperty("blockDetailsMasterDBSet")
	private Set <BlockDetailsMasterInput> blockDetailsMasterDB = new HashSet<>();

	@JsonProperty("employeeSchedules")
	private List <EmployeeScheduleInput> employeeSchedules = new ArrayList<>();
}
