package com.soul.emr.masters.controller;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import com.soul.emr.masters.service.MastersServiceInterf;
import com.soul.emr.model.entity.masterentity.elasticsearchentity.*;
import com.soul.emr.model.entity.masterentity.masterdb.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.GraphQlRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;

@GraphQlRepository
@DgsComponent
@CrossOrigin
public class MasterGraphQLController{
	
	private final MastersServiceInterf mastersServiceInterf;
	
	@Autowired
	public MasterGraphQLController(MastersServiceInterf mastersServiceInterf){
		super();
		this.mastersServiceInterf = mastersServiceInterf;
	}
	
	//query to get all role masters
	@DgsQuery
	public List <RoleMasterDB> getAllRolesMaster(){
		
		return mastersServiceInterf.fetchAllRoleMaster();
	}
	
	//query to get all role masters using id
	@DgsQuery
	public RoleMasterDB getRolesMasterId(@InputArgument(value = "roleMasterId") Long roleMasterId){
		
		return mastersServiceInterf.fetchRoleMasterUsingId(roleMasterId);
	}
	
	//query to fetch all departments
	@DgsQuery
	public List <DepartmentMasterDB> getAllDepartmentMaster(){
		return mastersServiceInterf.fetchAllDepartments();
	}
	
	
	//query to fetch department bu id
	@DgsQuery
	public DepartmentMasterDB getDepartmentMasterId(@InputArgument(value = "departmentMasterId") Long departmentMasterId){
		return mastersServiceInterf.getDepartmentById(departmentMasterId);
	}

	//query to fetch holiday master by id
	@DgsQuery
	public Optional <HolidayMasterDB> getHolidayMasterById(@InputArgument(value = "holidayMasterId") Long holidayMasterId) {
		return this.mastersServiceInterf.getHolidayMasterById(holidayMasterId);
	}
	

	//query to fetch all holiday masters
	@DgsQuery
	public List<HolidayMasterDB> getAllHolidays() {
		return this.mastersServiceInterf.getAllHolidayMasters();
	}

	//query to fetch holiday master by ids
	@DgsQuery
	public List <HolidayMasterDB> getHolidayMasterByIds(@InputArgument(value = "holidayMasterIds") List<Long> holidayMasterIds) {
		return this.mastersServiceInterf.getHolidayMasterByIds(holidayMasterIds);
	}

	//query to fetch block master by id
	@DgsQuery
	public Optional<BlockDetailsMasterDB> getAllBlockById(@InputArgument(value = "blockMasterId") Long blockMasterId) {
		return this.mastersServiceInterf.getBlockMasterById(blockMasterId);
	}

	//query to fetch all block masters
	@DgsQuery
	public List<BlockDetailsMasterDB> getAllBlocksMaster() {
		return this.mastersServiceInterf.getAllBlockMasters();
	}

	//query to fetch block masters by ids
	@DgsQuery
	public List <BlockDetailsMasterDB> getAllBlockByIds(@InputArgument(value = "blockMasterIds") List<Long> blockMasterIds) {
		return this.mastersServiceInterf.getBlockMasterByIds(blockMasterIds);
	}
	
	
	//query to fetch all prefixMaster
	@DgsQuery
	public Flux<PrefixMasterSearch> fetchAllPrefixMaster(@InputArgument("isActive") Boolean isActive)
	{
		return mastersServiceInterf.fetchAllPrefixMasters(isActive);
	}
	
	//query to fetch prefixMaster using id
	@DgsQuery
	public Optional<PrefixMasterDB> fetchPrefixMasterUsingId(@InputArgument("id") Long id)
	{
		return mastersServiceInterf.fetchPrefixMasterUsingId(id);
	}
	
}