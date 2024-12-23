package com.soul.patient.dao;

import com.soul.patient.model.entity.elasticsearchentity.*;
import com.soul.patient.model.entity.communication.communicationinfodb.CommunicationInfoDB;
import com.soul.patient.model.entity.modelonetimepassword.onetimepassworddb.OneTimePasswordEntityDB;
import com.soul.patient.model.entity.modelpatient.patientregistrationdb.PatientConsultationDB;
import com.soul.patient.model.entity.modelpatient.patientregistrationdb.PatientDetailsDB;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PatientDaoInterf{
	
	
	OneTimePasswordEntityDB saveOtp(OneTimePasswordEntityDB oneTimePasswordEntity);
	Optional <OneTimePasswordEntityDB> getOtpInfo(String identifier, Long otpId);
	
	
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
	
	
}
