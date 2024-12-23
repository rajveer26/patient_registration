package com.soul.patient.dao;

import com.soul.patient.model.entity.elasticsearchentity.*;
import com.soul.patient.model.entity.communication.communicationinfodb.CommunicationInfoDB;
import com.soul.patient.model.entity.modelonetimepassword.onetimepassworddb.OneTimePasswordEntityDB;
import com.soul.patient.model.entity.modelpatient.patientregistrationdb.PatientDetailsDB;
import com.soul.patient.model.entity.modelpatient.patientregistrationdb.PatientConsultationDB;
import com.soul.patient.model.repository.elasticsearchrepository.PatientSearchRepository;
import com.soul.patient.model.repository.jparepository.communicationrepository.CommunicationInfoRepo;
import com.soul.patient.model.repository.jparepository.onetimepasswordrepository.OneTimePasswordRepo;
import com.soul.patient.model.repository.jparepository.patientrepository.PatientDetailsRepository;
import com.soul.patient.model.repository.jparepository.patientrepository.PatientConsultationRepository;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository("lmsDao")
@CacheConfig(cacheNames = "emrCache")
public class PatientDao implements PatientDaoInterf{
	
	private final OneTimePasswordRepo oneTimePasswordRepo;
	private final PatientDetailsRepository patientDetailsRepository;
	private final PatientConsultationRepository patientConsultationRepository;
	private final CommunicationInfoRepo communicationInfoRepo;
	private final PatientSearchRepository patientSearchRepository;
	
	@Autowired
	public PatientDao(OneTimePasswordRepo oneTimePasswordRepo, PatientDetailsRepository patientDetailsRepository, PatientConsultationRepository patientConsultationRepository, CommunicationInfoRepo communicationInfoRepo, PatientSearchRepository patientSearchRepository){
		
		super();
		this.oneTimePasswordRepo                 = oneTimePasswordRepo;
		this.patientDetailsRepository         = patientDetailsRepository;
		this.patientConsultationRepository    = patientConsultationRepository;
		this.communicationInfoRepo            = communicationInfoRepo;
		this.patientSearchRepository             = patientSearchRepository;
	}
	
	//logger
	private final Logger logger = LogManager.getLogger(PatientDao.class);
	
	
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


	//to fetch patient details dubpilcates
	@Override
	@Cacheable(cacheNames = {"patientDetailsDuplicates"}, key = "{#firstName, #dob, #aadhaarNumber, #mobileNumber}")
	@Transactional(readOnly = true)
	public Optional <PatientDetailsDB> findDuplicatePatientDetails(String firstName, Optional<LocalDate> dob, Optional<String> aadhaarNumber, Optional<String> mobileNumber){
		try{
			return patientDetailsRepository.findDuplicatePatientDetails(firstName,dob.orElse(null),aadhaarNumber.orElse(null),mobileNumber.orElse(null));
		}

		//catch block
		catch(Exception e){

			//logging exception
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
}
