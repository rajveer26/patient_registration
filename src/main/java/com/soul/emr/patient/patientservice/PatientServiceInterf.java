package com.soul.emr.patient.patientservice;

import com.soul.emr.model.entity.abhaentity.graphqlEntity.AbhaGenerateOtpInput;
import com.soul.emr.model.entity.abhaentity.graphqlEntity.AbhaValidateOtpInput;
import com.soul.emr.model.entity.abhaentity.response.AbhaGenerateOtpResponse;
import com.soul.emr.model.entity.abhaentity.response.AbhaValidateOtpResponse;
import com.soul.emr.model.entity.elasticsearchentity.PatientSearch;
import com.soul.emr.model.entity.email.EmailEntity;
import com.soul.emr.model.entity.modelonetimepassword.graphqlentity.OneTimePasswordInput;
import com.soul.emr.model.entity.modelpatient.graphqlentity.PatientDetailsInput;
import com.soul.emr.model.entity.modelpatient.patientregistrationdb.PatientConsultationDB;
import com.soul.emr.model.entity.modelpatient.patientregistrationdb.PatientDetailsDB;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PatientServiceInterf
{
	PatientDetailsDB savePatientDetails(PatientDetailsInput patientDetailsInput);
	
	Mono<AbhaGenerateOtpResponse> abhaGenerateOtp(AbhaGenerateOtpInput abhaGenerateOtpInput);
	
	Mono <AbhaValidateOtpResponse> abhaValidateOtp(AbhaValidateOtpInput abhaValidateOtpInput);

	List<PatientDetailsDB> getAllPatientDetails();
	
	//for mail
	static EmailEntity getEmailEntity(OneTimePasswordInput otpInfo, Integer otp) {
		
		//creating a new object of type EmailEntity
		EmailEntity emailEntity = new EmailEntity();
		
		emailEntity.setTo(otpInfo.getCommunicationInfoDB().getEmailId().trim());
		emailEntity.setSubject("OTP Verification");
		emailEntity.setMessageHeader(otpInfo.getCommunicationInfoDB().getPatientDetails().parallelStream().map(PatientDetailsInput::getFirstName).findFirst().orElse("User"));
		emailEntity.setMessageBodyP1(String.valueOf(otp).concat(" ").concat(" is the OTP to validate your account information. OTPs are SECRET, DO NOT SHARE this code with anyone."));
		
		//returning object
		return emailEntity;
	}
	
	
	//about patients
	List <Object[]> fetchPatientCountByGenderAndEncounterDate(Optional <String> type, String doctorCode, LocalDate currentDate);
	long fetchPatientCountByDoctorNumberAndType(Optional <String> type, String doctorCode);
	long fetchPatientCountByGenderAndEncounterDate(List<String> consultationTypes, String doctorCode, LocalDate currentDate);
	List <PatientSearch> searchPatient(String query);
	long patientCountBasedOnTypeAndDoctorCode(Optional <String> type, String doctorCode);
	Page <PatientConsultationDB> fetchPatientConsultation(String doctorCode, String type, Integer page, Integer size, Optional <LocalDate> date, String consultationStatus);
	Map <String, Object> generateOtp(OneTimePasswordInput oneTimePasswordInput);
	Map <String, Object> verifyOtp(OneTimePasswordInput oneTimePasswordEntity);
	
}
