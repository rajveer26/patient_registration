package com.soul.emr.dao;

import com.soul.emr.model.entity.masterentity.elasticsearchentity.*;
import com.soul.emr.model.entity.enummaster.Gender;
import com.soul.emr.model.entity.masterentity.masterdb.*;
import com.soul.emr.model.entity.communication.communicationinfodb.CommunicationInfoDB;
import com.soul.emr.model.entity.masterentity.masterdb.DepartmentMasterDB;
import com.soul.emr.model.entity.masterentity.masterdb.RoleMasterDB;
import com.soul.emr.model.entity.modelbusinessgroup.businessgroupdb.BusinessGroupDB;
import com.soul.emr.model.entity.modelbusinessgroup.organization.organizationdb.OrganizationDB;
import com.soul.emr.model.entity.modelemployee.registrationdb.EmployeeScheduleDB;
import com.soul.emr.model.entity.modelonetimepassword.onetimepassworddb.OneTimePasswordEntityDB;
import com.soul.emr.model.entity.modelemployee.registrationdb.UserCredentialsDB;
import com.soul.emr.model.entity.modelemployee.registrationdb.EmployeeInfoDB;
import com.soul.emr.model.entity.modelpatient.patientregistrationdb.PatientDetailsDB;
import com.soul.emr.model.entity.modelpatient.patientregistrationdb.PatientConsultationDB;
import com.soul.emr.model.repository.elasticsearchrepository.*;
import com.soul.emr.model.repository.jparepository.businessgrouprepository.BusinessGroupRepo;
import com.soul.emr.model.repository.jparepository.businessgrouprepository.organizationrepository.OrganizationRepo;
import com.soul.emr.model.repository.jparepository.communicationrepository.CommunicationInfoRepo;
import com.soul.emr.model.repository.jparepository.masterRepository.*;
import com.soul.emr.model.repository.jparepository.onetimepasswordrepository.OneTimePasswordRepo;
import com.soul.emr.model.repository.jparepository.patientrepository.PatientDetailsRepository;
import com.soul.emr.model.repository.jparepository.registrationrepository.EmployeeScheduleRepository;
import com.soul.emr.model.repository.jparepository.patientrepository.PatientConsultationRepository;
import com.soul.emr.model.repository.jparepository.registrationrepository.UserCredentialsRepository;
import com.soul.emr.model.repository.jparepository.registrationrepository.EmployeeInfoRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository("lmsDao")
@CacheConfig(cacheNames = "emrCache")
public class EmrDao implements EmrDaoInterf{
	
	private final EmployeeInfoRepository userRepository;
	private final UserCredentialsRepository userCredentialsRepository;
	private final OneTimePasswordRepo oneTimePasswordRepo;
	private final BusinessGroupRepo businessGroupRepo;
	private final OrganizationRepo organizationRepo;
	private final RoleMasterRepository roleMasterRepository;
	private final DepartmentsRepository departmentsRepository;
	private final PatientDetailsRepository patientDetailsRepository;
	private final PatientConsultationRepository patientConsultationRepository;
	private final CommunicationInfoRepo communicationInfoRepo;
	private final ConceptMasterSearchRepository conceptMasterSearchRepository;
	private final DiagnosisMasterSearchRepository diagnosisMasterSearchRepository;
	private final NameSpaceMasterSearchRepository nameSpaceMasterSearch;
	private final ServiceMasterSearchRepository serviceMasterSearchRepository;
	private final MedicationMasterSearchRepository medicationMasterSearchRepository;
	private final PatientSearchRepository patientSearchRepository;
	private final HolidayMasterRepository holidayMasterRepository;
	private final BlockMasterRepository blockMasterRepository;
	private final EmployeeScheduleRepository employeeScheduleRepository;
	private final PrefixMasterRepository prefixMasterRepository;
	private final PrefixMasterSearchRepository prefixMasterSearchRepository;
	
	@Autowired
	public EmrDao(EmployeeInfoRepository userRepository, UserCredentialsRepository userCredentialsRepository, OneTimePasswordRepo oneTimePasswordRepo, BusinessGroupRepo businessGroupRepo, OrganizationRepo organizationRepo, RoleMasterRepository roleMasterRepository, DepartmentsRepository departmentsRepository, PatientDetailsRepository patientDetailsRepository, PatientConsultationRepository patientConsultationRepository, CommunicationInfoRepo communicationInfoRepo, ConceptMasterSearchRepository conceptMasterSearchRepository, DiagnosisMasterSearchRepository diagnosisMasterSearchRepository, NameSpaceMasterSearchRepository nameSpaceMasterSearch, ServiceMasterSearchRepository serviceMasterSearchRepository, MedicationMasterSearchRepository medicationMasterSearchRepository, PatientSearchRepository patientSearchRepository, HolidayMasterRepository holidayMasterRepository, BlockMasterRepository blockMasterRepository, EmployeeScheduleRepository employeeScheduleRepository, PrefixMasterRepository prefixMasterRepository, PrefixMasterSearchRepository prefixMasterSearchRepository){
		
		super();
		this.userRepository                      = userRepository;
		this.userCredentialsRepository           = userCredentialsRepository;
		this.oneTimePasswordRepo                 = oneTimePasswordRepo;
		this.businessGroupRepo                   = businessGroupRepo;
		this.organizationRepo                    = organizationRepo;
		this.roleMasterRepository                = roleMasterRepository;
		this.departmentsRepository               = departmentsRepository;
		this.patientDetailsRepository         = patientDetailsRepository;
		this.patientConsultationRepository    = patientConsultationRepository;
		this.communicationInfoRepo            = communicationInfoRepo;
		this.conceptMasterSearchRepository    = conceptMasterSearchRepository;
		this.diagnosisMasterSearchRepository     = diagnosisMasterSearchRepository;
		this.nameSpaceMasterSearch               = nameSpaceMasterSearch;
		this.serviceMasterSearchRepository       = serviceMasterSearchRepository;
		this.medicationMasterSearchRepository    = medicationMasterSearchRepository;
		this.patientSearchRepository             = patientSearchRepository;
		this.holidayMasterRepository             = holidayMasterRepository;
		this.blockMasterRepository               = blockMasterRepository;
		this.employeeScheduleRepository          = employeeScheduleRepository;
		this.prefixMasterRepository       = prefixMasterRepository;
		this.prefixMasterSearchRepository = prefixMasterSearchRepository;
	}
	
	//logger
	private final Logger logger = LogManager.getLogger(EmrDao.class);
	
	//dao layer to save user info
	@CacheEvict(cacheNames = {"userByUserName", "userByUserEmail", "userCredentials", "userByUserId", "communicationById"}, allEntries = true)
	@Transactional
	@Override
	public EmployeeInfoDB saveUserInfo(EmployeeInfoDB userInfo){
		try{
			//calling saveAndFlush() method present in userRepository
			return userRepository.saveAndFlush(userInfo);
		}
		//catch block
		catch(Exception e){
			
			//logger
			logger.error(e.fillInStackTrace());
			logger.catching(e);
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//method to get user by userId
	@Transactional(readOnly = true)
	@Cacheable(cacheNames = {"userByUserId"}, key = "id")
	@Override
	public Optional <EmployeeInfoDB> getUserInfoById(Long id){
		try{
			//checking if id is null or not and that id exists in the db or not
			if (!Objects.isNull(id) && userRepository.existsById(id)) return userRepository.findById(id);
				
				//if not returning empty optional
			else return Optional.empty();
		}
		//catch block
		catch(Exception e){
			
			//logging exception
			logger.error(e.fillInStackTrace());
			logger.catching(e);
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//dao layer to fetch EmployeeInfoDB using userName
	@Transactional(readOnly = true)
	@Cacheable(cacheNames = {"userByUserName"}, key = "userName")
	@Override
	public Optional <EmployeeInfoDB> getUserInfo(String userName){
		try{
			//calling findByUserDetailsUserName() method present in userRepository
			return userRepository.findByUserDetailsUserName(userName);
		}
		
		//catch block
		catch(Exception e){
			
			//logging exception
			logger.error(e.fillInStackTrace());
			logger.catching(e);
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//dao layer to fetch user by email
	@Transactional(readOnly = true)
	@Cacheable(cacheNames = {"userByUserEmail"}, key = "userEmail")
	@Override
	public Optional <EmployeeInfoDB> getUserInfoByEmail(String userEmail){
		try{
			return userRepository.findByUserDetailsUserName(userEmail);
		}
		catch(Exception e){
			logger.error(e.fillInStackTrace());
			logger.catching(e);
			
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//dao layer to fetch user credentials by userName
	@Override
	@Cacheable(cacheNames = {"userCredentials"}, key = "userName")
	@Transactional(readOnly = true)
	public Optional <UserCredentialsDB> getUserCredentials(String userName){
		try{
			return userCredentialsRepository.findByUsername(userName);
		}
		
		catch(Exception e){
			logger.error(e.fillInStackTrace());
			logger.catching(e);
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//dao layer to save otp
	@Override
	@CacheEvict(cacheNames = {"getOtpUsingId", "getOtpUsingCommunicationId"}, allEntries = true)
	@Transactional
	public OneTimePasswordEntityDB saveOtp(OneTimePasswordEntityDB oneTimePasswordEntity){
		try{
			System.err.println(Thread.currentThread().getName());
			return oneTimePasswordRepo.save(oneTimePasswordEntity);
		}
		catch(Exception e){
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//dao layer to fetch otp by otpId
	@Override
	@Cacheable(cacheNames = {"getOtpUsingId"}, key = "#identifier + '_' + #otpId")
	@Transactional(readOnly = true)
	public Optional <OneTimePasswordEntityDB> getOtpInfo(String identifier, Long otpId){
		try{
			return oneTimePasswordRepo.findByIdentifier(identifier.trim(), otpId);
			
		}
		catch(Exception e){
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//to fetch OneTimePasswordEntityDB object using communicationId
	@Override
	@Cacheable(cacheNames = {"getOtpUsingCommunicationId"}, key = "communicationId")
	@Transactional(readOnly = true)
	public Optional <OneTimePasswordEntityDB> getOtpInfoByCommunicationInfoId(Long communicationId){
		try{
			return oneTimePasswordRepo.findByCommunicationInfoId(communicationId);
			
		}
		catch(Exception e){
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//to fetch OneTimePasswordEntityDB object using communicationId
	@Override
	@Cacheable(cacheNames = {"getOtpUsingUserId"}, key = "userDetailId")
	@Transactional(readOnly = true)
	public Optional <OneTimePasswordEntityDB> getOtpInfoByUserDetailId(Long userDetailId){
		try{
			return oneTimePasswordRepo.findOtpByUserId(userDetailId);
			
		}
		catch(Exception e){
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//dao layer to fetch a business group by user input
	@Override
	@Transactional(readOnly = true)
	@Cacheable(cacheNames = {"getBusinessGroups"})
	public Page <BusinessGroupDB> getBusinessGroup(Pageable pageable){
		try{
			return businessGroupRepo.findAll(pageable);
			
		}
		catch(Exception e){
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//dao layer to fetch a business group by id
	@Override
	@Cacheable(cacheNames = {"businessGroupById"}, key = "id")
	@Transactional(readOnly = true)
	public Optional <BusinessGroupDB> getBusinessGroupId(Long id){
		try{
			if (!Objects.isNull(id) && businessGroupRepo.existsById(id)) return businessGroupRepo.findById(id);
			
			else return Optional.empty();
			
		}
		//catch block
		catch(Exception e){
			
			//logging exception
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	
	//dao layer to fetch organization by id
	@Override
	@Cacheable(cacheNames = {"organizationById"}, key = "id")
	@Transactional(readOnly = true)
	public Optional <OrganizationDB> getOrganization(Long id){
		try{
			if (!Objects.isNull(id) && organizationRepo.existsById(id)) return organizationRepo.findById(id);
			
			else return Optional.empty();
		}
		
		//catch block
		catch(Exception e){
			
			//logging exception
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//dao layer to fetch all organization masters by list of ids
	@Override
	@Cacheable(cacheNames = {"organizationByIds"}, key = "id")
	@Transactional(readOnly = true)
	public List <OrganizationDB> getOrganizationByIds(Iterable <Long> id){
		try{
			return organizationRepo.findAllById(id);
		}
		
		//catch block
		catch(Exception e){
			
			//logging exception
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//dao layer to fetch role master by id
	@Override
	@Cacheable(cacheNames = {"roleMasterById"}, key = "id")
	@Transactional(readOnly = true)
	public Optional <RoleMasterDB> getRoleMaster(Long id){
		try{
			if (!Objects.isNull(id) && roleMasterRepository.existsById(id)) return roleMasterRepository.findById(id);
			
			else return Optional.empty();
		}
		//catch block
		catch(Exception e){
			
			//logging exception
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//dao layer to fetch all role masters
	@Override
	@Cacheable(cacheNames = {"roleMasters"})
	@Transactional(readOnly = true)
	public List <RoleMasterDB> getAllRoleMaster(){
		try{
			return roleMasterRepository.findAll();
		}
		//catch block
		catch(Exception e){
			
			//logging exception
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//dao layer to fetch department by id
	@Override
	@Cacheable(cacheNames = {"departmentById"}, key = "id")
	@Transactional(readOnly = true)
	public Optional <DepartmentMasterDB> getDepartmentMasterById(Long id){
		try{
			if (!Objects.isNull(id) && departmentsRepository.existsById(id)) return departmentsRepository.findById(id);
			
			else return Optional.empty();
		}
		//catch block
		catch(Exception e){
			
			//logging exception
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//dao layer to fetch all departments
	@Override
	@Cacheable(cacheNames = {"departments"})
	@Transactional(readOnly = true)
	public List <DepartmentMasterDB> getAllDepartments(){
		try{
			return departmentsRepository.findAll();
		}
		
		//catch block
		catch(Exception e){
			
			//logging exception
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//to fetch all departments by list of ids
	@Override
	@Cacheable(cacheNames = {"departmentByIds"}, key = "ids")
	@Transactional(readOnly = true)
	public List <DepartmentMasterDB> getDepartmentByIds(Iterable <Long> ids){
		try{
			return departmentsRepository.findAllById(ids);
		}
		
		//catch block
		catch(Exception e){
			
			//logging exception
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//dao layer to search medication master
	@Override
	@Transactional(readOnly = true)
	public List <MedicationMasterSearch> searchMedicationMaster(String query, Boolean isActive){
		try{
			return medicationMasterSearchRepository.findByMedicationCodeLikeIgnoreCaseOrMedicationNameLikeIgnoreCaseOrMedicationGroupLikeIgnoreCaseAndIsActive(query, query, query, isActive);
		}
		
		//catch block
		catch(Exception e){
			//logging exception
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//dao layer to save patientDetails
	@Override
	@CacheEvict(cacheNames = {"patientDetailsById", "allPatientDetails", "patientDetailsByIds", "patientDetailsByMobileNo", "patientDetailsByMrno", "communicationById", "patientConsultationDetailsByDoctorCode", "communicationInfoUsingMobileNo", "patientByDoctorNumberAndType", "patientByDoctorCodeAndCurrentDate"}, allEntries = true)
	@Transactional
	public PatientDetailsDB savePatientDetails(PatientDetailsDB patientDetailsDB){
		try{
			
			return patientDetailsRepository.saveAndFlush(patientDetailsDB);
		}
		catch(Exception e){
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//dao layer to search patient
	@Override
	@Transactional(readOnly = true)
	public List <PatientSearch> searchPatient(String query){
		try{
			
			return patientSearchRepository.findByPatientName(query, query, query, query);
		}
		//catch block
		catch(Exception e){
			//logging exception
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//dao layer to fetch patientDetails by id
	@Override
	@Cacheable(cacheNames = {"patientDetailsById"}, key = "id")
	@Transactional(readOnly = true)
	public Optional <PatientDetailsDB> getPatientDetailById(Long id){
		try{
			if (!Objects.isNull(id) && patientDetailsRepository.existsById(id))
				return patientDetailsRepository.findById(id);
			
			else return Optional.empty();
		}
		
		//catch block
		catch(Exception e){
			
			//logging exception
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//dao layer to fetch patientConsultation by type, doctorCode, date, pagination and consultationStatus
	@Override
	@Cacheable(cacheNames = {"patientConsultationDetailsByDoctorCode"}, key = "#doctorCode + '_' + #type + '_' + (#date.isPresent() ? #date.get().toString() : 'null') + '_' + #consultationStatus")
	@Transactional(readOnly = true)
	public Page <PatientConsultationDB> getPatientRegistrationDetailByDoctorCodeAndType(String doctorCode, String type, Optional <LocalDate> date, String consultationStatus, Pageable pageable){
		try{
			
			return patientConsultationRepository.findByEmployeeInfoDB_EmployeeID(doctorCode, type, date.orElse(null), consultationStatus, pageable);
			
		}
		
		//catch block
		catch(Exception e){
			
			//logging exception
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//dao layer to fetch all patient details
	@Override
	@Cacheable(cacheNames = {"allPatientDetails"})
	@Transactional(readOnly = true)
	public List <PatientDetailsDB> getAllPatientDetails(){
		try{
			return patientDetailsRepository.findAll();
		}
		
		//catch block
		catch(Exception e){
			
			//logging exception
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//dao layer to delete patient details by using ids
	@Override
	@CacheEvict(cacheNames = {"allPatientDetails", "patientDetailsById", "patientDetailsByIds", "patientDetailsByMobileNo", "patientDetailsByMrno", "communicationById", "patientConsultationDetailsByDoctorCode", "communicationInfoUsingMobileNo"}, allEntries = true)
	@Transactional
	public Boolean deletePatientDetailsByIds(List <Long> ids){
		try{
			
			patientDetailsRepository.deleteAllById(ids);
			
			return Boolean.TRUE;
		}
		
		//catch block
		catch(Exception e){
			
			//logging exception
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//to fetch patient details by list of ids
	@Override
	@Cacheable(cacheNames = {"patientDetailsByIds"}, key = "ids")
	@Transactional(readOnly = true)
	public List <PatientDetailsDB> getPatientDetailsByIds(Iterable <Long> ids){
		try{
			return patientDetailsRepository.findAllById(ids);
		}
		
		//catch block
		catch(Exception e){
			
			//logging exception
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//to fetch communication by id
	@Override
	@Cacheable(cacheNames = {"communicationById"}, key = "id")
	@Transactional(readOnly = true)
	public Optional <CommunicationInfoDB> getCommunicationById(Long id){
		try{
			return communicationInfoRepo.findById(id);
		}
		
		//catch block
		catch(Exception e){
			
			//logging exception
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//to fetch communication details by mobileNo
	@Override
	@Cacheable(cacheNames = {"communicationInfoUsingMobileNo"}, key = "mobileNo")
	@Transactional(readOnly = true)
	public Optional <CommunicationInfoDB> getPatientDetailsByMobileNo(String mobileNo){
		try{
			return communicationInfoRepo.findPatientDetailsDBByMobileNo(mobileNo);
		}
		catch(Exception e){
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//to fetch patient details by mrno
	@Override
	@Cacheable(cacheNames = {"patientDetailsByMrno"}, key = "mrno")
	@Transactional(readOnly = true)
	public Optional <PatientDetailsDB> getPatientDetailsByMrno(String mrno){
		try{
			return patientDetailsRepository.findPatientDetailsDBByMrno(mrno);
		}
		
		//catch block
		catch(Exception e){
			
			//logging exception
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//dao layer to search package-service master using gender and query
	@Override
	@Transactional(readOnly = true)
	public List <ServiceMasterSearch> searchServiceMaster(Gender gender, String query){
		try{
			return this.serviceMasterSearchRepository.findByApplicableGenderIgnoreCaseAndServiceCategoryContainingIgnoreCaseOrServiceMasterNameContainingIgnoreCaseOrServiceMasterCodeContainingIgnoreCase(query, query, query, gender.name());
		}
		//catch block
		catch(Exception e){
			//logging exception
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	
	//dao layer to search conceptMaster
	@Override
	@Transactional(readOnly = true)
	public List <ConceptMasterSearch> searchConceptMaster(String query, String type){
		try{
			return conceptMasterSearchRepository.searchByConceptMasterTypeAndConceptMasterCodeContainingIgnoreCaseOrConceptMasterNameContainingIgnoreCase(type, query, query);
		}
		
		//catch block
		catch(Exception e){
			
			//logging exception
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//dao layer for nameSpace search
	@Override
	@Transactional(readOnly = true)
	public List <NamespaceMasterSearch> namespaceMasterSearchList(String query, Boolean isActive){
		try{
			return this.nameSpaceMasterSearch.searchByNamespaceCodeOrNamespaceName(query, query, isActive);
		}
		
		//catch block
		catch(Exception e){
			//logging exception
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	
	//dao layer to search diagnosis
	@Override
	@Transactional(readOnly = true)
	public List <DiagnosisMasterSearch> searchDiagnosisMaster(String query){
		try{
			return diagnosisMasterSearchRepository.findDiagnosisMasterSearchByLongDescriptionContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query);
		}
		
		//catch block
		catch(Exception e){
			
			//logging exception
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//dao layer to fetch holidayMaster by id
	@Override
	@Cacheable(cacheNames = {"holidayMasterById"}, key = "id")
	@Transactional(readOnly = true)
	public Optional <HolidayMasterDB> getHolidayMasterById(Long id){
		try{
			if (!Objects.isNull(id) && holidayMasterRepository.existsById(id))
				return holidayMasterRepository.findById(id);
			
			else return Optional.empty();
		}
		//catch block
		catch(Exception e){
			
			//logging exception
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//dao layer to fetch all holidayMasters
	@Override
	@Cacheable(cacheNames = {"holidayMasters"})
	@Transactional(readOnly = true)
	public List <HolidayMasterDB> getAllHolidayMaster(){
		try{
			return holidayMasterRepository.findAll();
		}
		
		//catch block
		catch(Exception e){
			
			//logging exception
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//dao layer to fetch holidayMaster by list of ids
	@Override
	@Cacheable(cacheNames = {"holidayMasterByIds"}, key = "ids")
	@Transactional(readOnly = true)
	public List <HolidayMasterDB> getHolidayMasterByIds(Iterable <Long> ids){
		try{
			return holidayMasterRepository.findAllById(ids);
		}
		catch(Exception e){
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	
	//dao layer to fetch blocksMaster by id
	@Override
	@Cacheable(cacheNames = {"blockMasterById"}, key = "id")
	@Transactional(readOnly = true)
	public Optional <BlockDetailsMasterDB> getBlockDetailsById(Long id){
		try{
			if (!Objects.isNull(id) && blockMasterRepository.existsById(id)) return blockMasterRepository.findById(id);
			
			else return Optional.empty();
		}
		catch(Exception e){
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//dao layer to fetch all block masters
	@Override
	@Cacheable(cacheNames = "blockMasters")
	@Transactional(readOnly = true)
	public List <BlockDetailsMasterDB> getAllBlockMasters(){
		try{
			return blockMasterRepository.findAll();
		}
		catch(Exception e){
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//dao layer to fetch block masters by Ids
	@Override
	@Cacheable(cacheNames = "blockMastersByIds", key = "id")
	@Transactional(readOnly = true)
	public List <BlockDetailsMasterDB> getBlocksByIds(Iterable <Long> id){
		try{
			return blockMasterRepository.findAllById(id);
		}
		catch(Exception e){
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//dao layer to fetch employee schedules by id
	@Override
	@Cacheable(cacheNames = "empScheduleById", key = "id")
	@Transactional(readOnly = true)
	public Optional <EmployeeScheduleDB> getEmployeeSchedulesById(Long id){
		try{
			if (!Objects.isNull(id) && employeeScheduleRepository.existsById(id))
				return employeeScheduleRepository.findById(id);
			else return Optional.empty();
		}
		catch(Exception e){
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//dao layer to fetch all employee schedules
	@Override
	@Cacheable(cacheNames = "empSchedules")
	@Transactional(readOnly = true)
	public List <EmployeeScheduleDB> getAllEmployeeSchedules(){
		try{
			return employeeScheduleRepository.findAll();
		}
		catch(Exception e){
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//dao layer to fetch employee schedules by list of ids
	@Override
	@Cacheable(cacheNames = "empSchedulesByIds", key = "ids")
	@Transactional(readOnly = true)
	public List <EmployeeScheduleDB> getEmployeeScheduleByIds(Iterable <Long> ids){
		try{
			return employeeScheduleRepository.findAllById(ids);
		}
		catch(Exception e){
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	
	//dao layer to fetch patient count based on doctorNumber and type
	@Override
	@Cacheable(cacheNames = "patientByDoctorNumberAndType", key = "(#type.isPresent() ? #type.get() : 'null') + '_' + #doctorCode")
	@Transactional(readOnly = true)
	public long fetchPatientCountByDoctorNumberAndType(Optional <String> type, String doctorCode){
		try{
			return patientConsultationRepository.findByPatientConsultationCount(type.orElse(null), doctorCode);
		}
		catch(Exception e){
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
			
		}
	}
	
	//dao layer to fetch patient count for graph with gender and encounterDate
	@Override
	@Cacheable(cacheNames = "patientByDoctorCodeAndCurrentDate", key = "(#type.isPresent() ? #type.get() : 'null') + '_' + #doctorCode + '_' + #currentDate")
	@Transactional(readOnly = true)
	public List<Object[]> fetchPatientCountBasedOnConsultationType(Optional <String> type, String doctorCode, LocalDate currentDate){
		try{
			return patientConsultationRepository.findPatientCountGenderAndEncounterDate(type.orElse(null), doctorCode, currentDate);
		}
		catch(Exception e){
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
			
		}
	}
	
	//dao layer to fetch patient consultation based on id
	@Override
	@Cacheable(cacheNames = "patientConsultationBasedOnId", key = "id")
	@Transactional(readOnly = true)
	public Optional<PatientConsultationDB> fetchPatientConsultationBasedOnId(Long id){
		try{
			return patientConsultationRepository.findById(id);
		}
		catch(Exception e){
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
			
		}
	}
	
	//dao layer to fetch patient count based on consultationType
	@Override
	@Cacheable(cacheNames = "patientCountBasedOnConsultationType", key = "#consultationTypes + '_' + #doctorCode + '_' + #currentDate")
	@Transactional(readOnly = true)
	public long fetchPatientCountBasedOnConsultationType(List<String> consultationTypes, String doctorCode, LocalDate currentDate){
		try{
			return patientConsultationRepository.findPatientCountBasedOnConsultationType(consultationTypes, doctorCode, currentDate);
		}
		catch(Exception e){
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
			
		}
	}
	
	//service layer to search all prefix masters
	@Override
	@Transactional(readOnly = true)
	public Flux<PrefixMasterSearch> fetchAllPrefixMaster(Boolean isActive)
	{
		try
		{
			return prefixMasterSearchRepository.fetchAllPrefix(isActive);
		}
		catch(Exception e)
		{
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//service layer to search all prefix masters
	@Override
	@Transactional(readOnly = true)
	public Optional<PrefixMasterDB> fetchPrefixMasterById(Long id)
	{
		try
		{
			return prefixMasterRepository.findById(id);
		}
		catch(Exception e)
		{
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException(e.getMessage());
		}
	}
}
