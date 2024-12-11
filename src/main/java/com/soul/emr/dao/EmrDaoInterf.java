package com.soul.emr.dao;

import com.soul.emr.model.entity.masterentity.elasticsearchentity.*;
import com.soul.emr.model.entity.enummaster.Gender;
import com.soul.emr.model.entity.masterentity.masterdb.*;
import com.soul.emr.model.entity.communication.communicationinfodb.CommunicationInfoDB;
import com.soul.emr.model.entity.masterentity.masterdb.DepartmentMasterDB;
import com.soul.emr.model.entity.masterentity.masterdb.RoleMasterDB;
import com.soul.emr.model.entity.modelbusinessgroup.businessgroupdb.BusinessGroupDB;
import com.soul.emr.model.entity.modelbusinessgroup.organization.organizationdb.OrganizationDB;
import com.soul.emr.model.entity.modelemployee.registrationdb.EmployeeInfoDB;
import com.soul.emr.model.entity.modelemployee.registrationdb.EmployeeScheduleDB;
import com.soul.emr.model.entity.modelonetimepassword.onetimepassworddb.OneTimePasswordEntityDB;
import com.soul.emr.model.entity.modelemployee.registrationdb.UserCredentialsDB;
import com.soul.emr.model.entity.modelpatient.patientregistrationdb.PatientConsultationDB;
import com.soul.emr.model.entity.modelpatient.patientregistrationdb.PatientDetailsDB;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmrDaoInterf{
	
	EmployeeInfoDB saveUserInfo(EmployeeInfoDB userInfo);
	Optional <EmployeeInfoDB> getUserInfoById(Long id);
	Optional <EmployeeInfoDB> getUserInfo(String userName);
	Optional <EmployeeInfoDB> getUserInfoByEmail(String userEmail);
	Optional <UserCredentialsDB> getUserCredentials(String userName);
	OneTimePasswordEntityDB saveOtp(OneTimePasswordEntityDB oneTimePasswordEntity);
	Optional <OneTimePasswordEntityDB> getOtpInfo(String identifier, Long otpId);
	
	//for business group
	Page <BusinessGroupDB> getBusinessGroup(Pageable pageable);
	Optional <BusinessGroupDB> getBusinessGroupId(Long id);
	
	//for organization
	Optional <OrganizationDB> getOrganization(Long id);
	List <OrganizationDB> getOrganizationByIds(Iterable <Long> id);
	
	//for role master
	Optional <RoleMasterDB> getRoleMaster(Long id);
	List <RoleMasterDB> getAllRoleMaster();
	
	//for departments
	Optional <DepartmentMasterDB> getDepartmentMasterById(Long id);
	List <DepartmentMasterDB> getAllDepartments();
	List <DepartmentMasterDB> getDepartmentByIds(Iterable <Long> id);
	
	//for medication master
	List<MedicationMasterSearch> searchMedicationMaster(String query, Boolean isActive);
	
	//for patient details
	List <PatientDetailsDB> getPatientDetailsByIds(Iterable <Long> id);
	Boolean deletePatientDetailsByIds(List <Long> ids);
	List <PatientDetailsDB> getAllPatientDetails();
	Optional <PatientDetailsDB> getPatientDetailById(Long id);
	PatientDetailsDB savePatientDetails(PatientDetailsDB patientDetailsDB);
	Optional<PatientDetailsDB> getPatientDetailsByMrno(String mrno);
	Optional<PatientDetailsDB> findDuplicatePatientDetails(String firstName, Optional<LocalDate> dob, Optional<String> aadhaarNumber, Optional<String> mobileNumber);
	List<PatientSearch> searchPatient(String query);
	long fetchPatientCountByDoctorNumberAndType(Optional<String> type, String doctorCode);
	List<Object[]> fetchPatientCountBasedOnConsultationType(Optional <String> type, String doctorCode, LocalDate currentDate);
	long fetchPatientCountBasedOnConsultationType(List<String> consultationTypes, String doctorCode, LocalDate currentDate);
		
	//for patient registration
	Page <PatientConsultationDB> getPatientRegistrationDetailByDoctorCodeAndType(String doctorCode, String type, Optional<LocalDate> date, String consultationStatus, Pageable pageable);
	Optional<PatientConsultationDB> fetchPatientConsultationBasedOnId(Long id);


	//for communication
	Optional<CommunicationInfoDB> getPatientDetailsByMobileNo(String mobileNo);
	Optional <CommunicationInfoDB> getCommunicationById(Long id);
	Optional <OneTimePasswordEntityDB> getOtpInfoByCommunicationInfoId(Long communicationId);
	Optional <OneTimePasswordEntityDB> getOtpInfoByUserDetailId(Long userDetailId);

	//For PackageService Master
	List<ServiceMasterSearch> searchServiceMaster(Gender gender, String query);
	
	//For Concept Master
	List <ConceptMasterSearch> searchConceptMaster(String query, String type);
	
	//For Namespace Master
	List<NamespaceMasterSearch> namespaceMasterSearchList(String query, Boolean isActive);

	//For Diagnosis Master
	List<DiagnosisMasterSearch> searchDiagnosisMaster(String query);

	//For Holiday Master
	Optional<HolidayMasterDB> getHolidayMasterById (Long id);
	List<HolidayMasterDB> getAllHolidayMaster();
	List<HolidayMasterDB> getHolidayMasterByIds(Iterable<Long> id);

	//For Blocks Master
	Optional<BlockDetailsMasterDB> getBlockDetailsById(Long id);
	List<BlockDetailsMasterDB> getAllBlockMasters();
	List<BlockDetailsMasterDB> getBlocksByIds(Iterable<Long> id);

	//For Employee Schedules
	Optional<EmployeeScheduleDB> getEmployeeSchedulesById (Long id);
	List<EmployeeScheduleDB> getAllEmployeeSchedules();
	List<EmployeeScheduleDB> getEmployeeScheduleByIds(Iterable<Long> ids);
	
	//for prefix master
	Flux<PrefixMasterSearch> fetchAllPrefixMaster(Boolean isActive);
	Optional<PrefixMasterDB> fetchPrefixMasterById(Long id);
	
}
