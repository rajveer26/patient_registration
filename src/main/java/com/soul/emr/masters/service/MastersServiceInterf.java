package com.soul.emr.masters.service;

import com.soul.emr.model.entity.masterentity.elasticsearchentity.*;
import com.soul.emr.model.entity.masterentity.masterdb.*;
import com.soul.emr.model.entity.masterentity.masterdb.DepartmentMasterDB;
import com.soul.emr.model.entity.masterentity.masterdb.RoleMasterDB;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;

public interface MastersServiceInterf{
	
	//roleMaster with privileges
	List <RoleMasterDB> fetchAllRoleMaster();
	RoleMasterDB fetchRoleMasterUsingId(Long id);
	
	//for department
	DepartmentMasterDB getDepartmentById(Long departmentId);
	List <DepartmentMasterDB> fetchAllDepartments();
	

	//for holiday master
	List<HolidayMasterDB> getAllHolidayMasters();
	List<HolidayMasterDB> getHolidayMasterByIds(List<Long> ids);
	Optional<HolidayMasterDB> getHolidayMasterById(Long id);

	//for block master
	Optional<BlockDetailsMasterDB> getBlockMasterById(Long id);
	List<BlockDetailsMasterDB> getBlockMasterByIds (List<Long> ids);
	List<BlockDetailsMasterDB> getAllBlockMasters();
	
	
	//for prefix master
	Optional<PrefixMasterDB> fetchPrefixMasterUsingId(Long id);
	Flux<PrefixMasterSearch> fetchAllPrefixMasters(Boolean isActive);
	
	
}