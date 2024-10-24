package com.soul.emr.masters.service;

import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;
import com.soul.emr.dao.EmrDaoInterf;
import com.soul.emr.model.entity.masterentity.elasticsearchentity.*;
import com.soul.emr.model.entity.masterentity.masterdb.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.*;

@Service("mastersService")
public class MastersService implements MastersServiceInterf{
	
	private final EmrDaoInterf emrDaoInterf;
	
	@Autowired
	public MastersService(EmrDaoInterf emrDaoInterf){
		this.emrDaoInterf = emrDaoInterf;
	}
	
	//logger
	private final Logger logger = LogManager.getLogger(MastersService.class);
	
	
	//to fetch all roleMaster
	@Override
	public List <RoleMasterDB> fetchAllRoleMaster(){
		return emrDaoInterf.getAllRoleMaster();
	}
	
	
	//fetch roleMaster using id
	@Override
	public RoleMasterDB fetchRoleMasterUsingId(Long id){
		return emrDaoInterf.getRoleMaster(id).orElseThrow(() -> new DgsEntityNotFoundException("no entity found"));
	}
	
	//to fetch departmentsById
	@Override
	public DepartmentMasterDB getDepartmentById(Long departmentId){
		
		//calling getDepartmentMasterById() method present in emrDaoInterf
		return emrDaoInterf.getDepartmentMasterById(departmentId).orElseThrow(() -> new DgsEntityNotFoundException("not found entity"));
	}
	
	//to fetch all departments
	@Override
	public List <DepartmentMasterDB> fetchAllDepartments(){
		
		//calling getAllDepartments() method present in emrDaoInterf
		return emrDaoInterf.getAllDepartments();
	}
	
	//to get holidayMasterId
	@Override
	public Optional<HolidayMasterDB> getHolidayMasterById(Long id)
	{
		return this.emrDaoInterf.getHolidayMasterById(id);
	}

	//service layer to get holidayMaster by Ids
	@Override
	public List<HolidayMasterDB> getHolidayMasterByIds(List<Long> ids)
	{
		return this.emrDaoInterf.getHolidayMasterByIds(ids);
	}

	//service to fetch all holidayMasters
	@Override
	public List<HolidayMasterDB> getAllHolidayMasters()
	{
		return this.emrDaoInterf.getAllHolidayMaster();
	}
	

	//service layer to get block master by id
	@Override
	public Optional<BlockDetailsMasterDB> getBlockMasterById(Long id)
	{
		return this.emrDaoInterf.getBlockDetailsById(id);
	}

	//service layer to get block masters by ids
	@Override
	public List<BlockDetailsMasterDB> getBlockMasterByIds (List<Long> ids)
	{
		return this.emrDaoInterf.getBlocksByIds(ids);
	}

	//service layer to get all block masters
	@Override
	public List<BlockDetailsMasterDB> getAllBlockMasters()
	{
		return this.emrDaoInterf.getAllBlockMasters();
	}
	
	
	//service layer to fetch prefix masters
	@Override
	public Flux<PrefixMasterSearch> fetchAllPrefixMasters(Boolean isActive)
	{
	  return emrDaoInterf.fetchAllPrefixMaster(isActive);
	}
	
	//service layer to fetch prefix master using id
	@Override
	public Optional<PrefixMasterDB> fetchPrefixMasterUsingId(Long id)
	{
	  return emrDaoInterf.fetchPrefixMasterById(id);
	}

}
