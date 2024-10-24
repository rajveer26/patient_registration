package com.soul.emr.doctor.service;

import com.soul.emr.model.entity.email.EmailEntity;
import com.soul.emr.model.entity.masterentity.elasticsearchentity.PatientSearch;
import com.soul.emr.model.entity.modelbusinessgroup.businessgroupdb.BusinessGroupDB;
import com.soul.emr.model.entity.modelbusinessgroup.graphqlentity.BusinessGroupInput;
import com.soul.emr.model.entity.modelemployee.graphqlentity.EmployeeInfoInput;
import com.soul.emr.model.entity.modelemployee.registrationdb.EmployeeInfoDB;
import com.soul.emr.model.entity.modelemployee.registrationdb.EmployeeScheduleDB;
import com.soul.emr.model.entity.modelonetimepassword.graphqlentity.OneTimePasswordInput;
import com.soul.emr.model.entity.modelpatient.patientregistrationdb.PatientConsultationDB;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface EmployeeServiceInterf
{
    //for user
    EmployeeInfoDB registerUser(EmployeeInfoInput employeeInfoInput);
    EmployeeInfoDB updateUser(EmployeeInfoInput employeeInfoInput);
    Map<String,Object> generateOtp(OneTimePasswordInput oneTimePasswordInput);
    Map<String, Object> verifyOtp(OneTimePasswordInput oneTimePasswordEntity);
    EmployeeInfoDB fetchUserDetails(String userName);
    
    //for mail
    static EmailEntity getEmailEntity(OneTimePasswordInput otpInfo, Integer otp) {
        
        //creating a new object of type EmailEntity
        EmailEntity emailEntity = new EmailEntity();
        
        emailEntity.setTo(otpInfo.getCommunicationInfoDB().getEmailId().trim());
        emailEntity.setSubject("OTP Verification");
        emailEntity.setMessageHeader(otpInfo.getCommunicationInfoDB().getEmployeeInfoDB().getFullName());
        emailEntity.setMessageBodyP1(String.valueOf(otp).concat(" ").concat(" is the OTP to validate your account information. OTPs are SECRET, DO NOT SHARE this code with anyone."));
        
        //returning object
        return emailEntity;
    }
    
    //for business group
    Page <BusinessGroupDB> getBusinessGroup(Integer page, Integer size);
    
    
    //for employee schedules
    List<EmployeeScheduleDB> getAllEmployeeSchedules();
    List<EmployeeScheduleDB> getEmployeeSchedulesByIds(List<Long> ids);
    Optional<EmployeeScheduleDB> getEmployeeSchedulesById(Long id);
    
    //about patients
    List <Object[]> fetchPatientCountByGenderAndEncounterDate(Optional <String> type, String doctorCode, LocalDate currentDate);
    long fetchPatientCountByDoctorNumberAndType(Optional <String> type, String doctorCode);
    long fetchPatientCountByGenderAndEncounterDate(List<String> consultationTypes, String doctorCode, LocalDate currentDate);
    List <PatientSearch> searchPatient(String query);
    long patientCountBasedOnTypeAndDoctorCode(Optional <String> type, String doctorCode);
    Page <PatientConsultationDB> fetchPatientConsultation(String doctorCode, String type, Integer page, Integer size, Optional <LocalDate> date, String consultationStatus);
    
    
}
