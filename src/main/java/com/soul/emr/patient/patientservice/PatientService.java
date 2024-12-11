package com.soul.emr.patient.patientservice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;
import com.soul.emr.dao.EmrDaoInterf;
import com.soul.emr.helper.HelperInterf;
import com.soul.emr.model.entity.abhaentity.graphqlEntity.AbhaGenerateOtpInput;
import com.soul.emr.model.entity.abhaentity.graphqlEntity.AbhaValidateOtpInput;
import com.soul.emr.model.entity.abhaentity.response.AbhaGenerateOtpResponse;
import com.soul.emr.model.entity.abhaentity.response.AbhaValidateOtpResponse;
import com.soul.emr.model.entity.communication.communicationinfodb.CommunicationInfoDB;
import com.soul.emr.model.entity.communication.graphqlentity.CommunicationInfoInput;
import com.soul.emr.model.entity.masterentity.masterdb.DepartmentMasterDB;
import com.soul.emr.model.entity.modelemployee.registrationdb.EmployeeInfoDB;
import com.soul.emr.model.entity.modelemployee.registrationdb.RolesDB;
import com.soul.emr.model.entity.modelpatient.graphqlentity.PatientAppointmentInput;
import com.soul.emr.model.entity.modelpatient.graphqlentity.PatientDetailsInput;
import com.soul.emr.model.entity.modelpatient.graphqlentity.PatientConsultationInput;
import com.soul.emr.model.entity.modelpatient.patientregistrationdb.PatientAppointmentDB;
import com.soul.emr.model.entity.modelpatient.patientregistrationdb.PatientConsultationDB;
import com.soul.emr.model.entity.modelpatient.patientregistrationdb.PatientDetailsDB;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service("patientService")
public class PatientService implements PatientServiceInterf{
	private final EmrDaoInterf daoInterf;
	private final HelperInterf helperInterf;
	private final Environment environment;
	
	//logger
	private final Logger logger = LogManager.getLogger(PatientService.class);
	
	@Autowired
	public PatientService(EmrDaoInterf daoInterf, HelperInterf helperInterf, Environment environment){
		this.daoInterf    = daoInterf;
		this.helperInterf = helperInterf;
		this.environment  = environment;
	}
	
	//method to save patientDetails
	@Override
	public PatientDetailsDB savePatientDetails(PatientDetailsInput patientDetailsInput){
		try{

			Optional <PatientDetailsDB> existingPatientByDobAndName = this.daoInterf.findDuplicatePatientDetails(patientDetailsInput.getFirstName(), Optional.ofNullable(patientDetailsInput.getDob()), Optional.empty(), Optional.empty());
			Optional <PatientDetailsDB> existingPatientByAadhaar = this.daoInterf.findDuplicatePatientDetails(null, Optional.empty(), Optional.ofNullable(patientDetailsInput.getAadhaarNumber()), Optional.empty());
			Optional<PatientDetailsDB> existingPatientByMobileNumber = this.daoInterf.findDuplicatePatientDetails(null, Optional.empty(), Optional.empty(),
                    patientDetailsInput.getCommunicationInfoDB()
							.stream()
							.map(CommunicationInfoInput::getMobileNumber)
                            .findFirst()
			);

			//if an object is present
			if (existingPatientByDobAndName.isPresent() || existingPatientByAadhaar.isPresent() || existingPatientByMobileNumber.isPresent()) {
				//throwing a new RuntimeException
				throw new RuntimeException("Patient already exists");
			} else {
				//creating a new PatientDetailsDB object
				PatientDetailsDB newPatientDetails = new PatientDetailsDB();
				
				//calling current class setPatientDetails() method
				PatientDetailsDB patientDetailsDB = this.setPatientDetails(newPatientDetails, patientDetailsInput);
				
				//calling savePatientDetails() method to save patientDetailsDB
				return this.daoInterf.savePatientDetails(patientDetailsDB);
			}
		}
		//catch block
		catch(Exception e){
			
			//logging exception
			logger.error(e.fillInStackTrace());
			logger.catching(e);
			
			throw new RuntimeException(e);
		}
	}
	
	//method to set patientDetails
	private PatientDetailsDB setPatientDetails(PatientDetailsDB patientDetailsDB, PatientDetailsInput patientDetailsInput) throws RuntimeException{
		
		// Generation of MRNO
		patientDetailsDB.setMrno(patientDetailsInput.getMrno());
		
		
		// Setting data members for patientDetailsDB from patientDetailsInput
		patientDetailsDB.setFirstName(patientDetailsInput.getFirstName());
		patientDetailsDB.setMiddleName(patientDetailsInput.getMiddleName());
		patientDetailsDB.setLastName(patientDetailsInput.getLastName());
		
		StringBuilder fullName = new StringBuilder();
		if (patientDetailsInput.getPrefixMasterDB().getPrefixName() != null && !patientDetailsInput.getPrefixMasterDB().getPrefixName().isEmpty()) {
			if (!fullName.isEmpty()) fullName.append(" ");
			fullName.append(patientDetailsInput.getPrefixMasterDB().getPrefixName());
		}
		if (patientDetailsInput.getFirstName() != null && !patientDetailsInput.getFirstName().isEmpty()) {
			if (!fullName.isEmpty()) fullName.append(" ");
			fullName.append(patientDetailsInput.getFirstName());
		}
		if (patientDetailsInput.getMiddleName() != null && !patientDetailsInput.getMiddleName().isEmpty()) {
			if (!fullName.isEmpty()) fullName.append(" ");
			fullName.append(patientDetailsInput.getMiddleName());
		}
		if (patientDetailsInput.getLastName() != null && !patientDetailsInput.getLastName().isEmpty()) {
			if (!fullName.isEmpty()) fullName.append(" ");
			fullName.append(patientDetailsInput.getLastName());
		}
		patientDetailsDB.setPatientName(fullName.toString());
		
		patientDetailsDB.setGender(patientDetailsInput.getGender());
		patientDetailsDB.setMaritalStatus(patientDetailsInput.getMaritalStatus());
		patientDetailsDB.setDob(patientDetailsInput.getDob());
		patientDetailsDB.setAge(patientDetailsInput.getAge());
		patientDetailsDB.setPatientImage(patientDetailsInput.getPatientImage());
		patientDetailsDB.setRegisteredOn(LocalDate.now());
		patientDetailsDB.setSmartCardId(patientDetailsInput.getSmartCardId());
		patientDetailsDB.setEsiIpNumber(patientDetailsDB.getEsiIpNumber());
		
		// Setting the prefix
		if (!Objects.isNull(patientDetailsInput.getPrefixMasterDB())) {
			
			patientDetailsDB.setPrefixMasterDB(daoInterf.fetchPrefixMasterById(patientDetailsInput.getPrefixMasterDB().getPrefixMasterId()).orElse(null));
		}
		
		//checking if communicationInfoDB is null or empty or not
		if (!Objects.isNull(patientDetailsInput.getCommunicationInfoDB()) && !patientDetailsInput.getCommunicationInfoDB().isEmpty()) {
			
			//extracting a list of CommunicationInfoDB from patientDetailsDB object
			Set <CommunicationInfoDB> communicationInfoDBList = patientDetailsDB.getCommunicationInfoDB();
			
			//using for-each
			patientDetailsInput.getCommunicationInfoDB().forEach(communicationInfoInput -> {
				
				//checking if CommunicationInfoDB is already present inside a db or not using communicationInfoId
				Optional <CommunicationInfoDB> existingCommunication = communicationInfoDBList.stream().filter(comuniInfo -> !Objects.isNull(communicationInfoInput.getCommunicationInfoId()) && Objects.equals(communicationInfoInput.getCommunicationInfoId(), comuniInfo.getCommunicationInfoId())).findFirst();
				{
					//if an object is present
					if (existingCommunication.isPresent()) {
						
						//calling setCommunicationDataMembers() method present in helperInterf
						CommunicationInfoDB updatedCommunicationInfoDB = this.helperInterf.setCommunicationDataMembers(existingCommunication.get(), communicationInfoInput);
						
						//setting reference
						updatedCommunicationInfoDB.setPatientDetails(Set.of(patientDetailsDB));
						
					} else {
						
						//creating a new CommunicationInfoDB object
						CommunicationInfoDB newCommunicationInfoDB = new CommunicationInfoDB();
						
						//calling setCommunicationDataMembers() method present in helperInterf
						CommunicationInfoDB communicationInfoDB = this.helperInterf.setCommunicationDataMembers(newCommunicationInfoDB, communicationInfoInput);
						
						//setting reference
						communicationInfoDB.setPatientDetails(Set.of(patientDetailsDB));
						
						//adding an object in a list
						communicationInfoDBList.add(communicationInfoDB);
					}
				}
			});
			//setting the object
			patientDetailsDB.setCommunicationInfoDB(communicationInfoDBList);
		}
		
		//checking patientRegistrations is null or empty or not
		if (!Objects.isNull(patientDetailsInput.getPatientRegistrations()) && !patientDetailsInput.getPatientRegistrations().isEmpty()) {
			
			//extracting a list of PatientConsultationDB object from patientDetailsDB
			List <PatientConsultationDB> patientConsultationDBS = patientDetailsDB.getPatientRegistrations();
			
			//using for-each
			patientDetailsInput.getPatientRegistrations().forEach(patientConsultationInput -> {
				
				//checking if PatientConsultationDB object is present inside an object or not
				Optional <PatientConsultationDB> existingPatientRegistration = patientConsultationDBS.stream().filter(patientRegis -> !Objects.isNull(patientConsultationInput.getPatientConsultationId()) && Objects.equals(patientConsultationInput.getPatientConsultationId(), patientRegis.getPatientConsultationId())).findFirst();
				{
					//if an object is present
					if (existingPatientRegistration.isPresent()) {
						
						//extracting index of an object
						int index = patientConsultationDBS.indexOf(existingPatientRegistration.get());
						
						//extracting PatientConsultationDB object from Optional
						PatientConsultationDB patientConsultationDB = existingPatientRegistration.get();
						
						//calling current class setPatientRegistration() method
						PatientConsultationDB updatedPatientConsultationDB = this.setPatientRegistration(patientConsultationDB, patientConsultationInput);
						
						//setting reference
						updatedPatientConsultationDB.setPatientDetailDB(patientDetailsDB);
						
						//updating the object in a list
						patientConsultationDBS.set(index, updatedPatientConsultationDB);
					} else {
						
						//crating a new PatientConsultationDB object
						PatientConsultationDB newPatientRegistration = new PatientConsultationDB();
						
						//calling current class setPatientRegistration() method
						PatientConsultationDB updatedPatientConsultationDB = this.setPatientRegistration(newPatientRegistration, patientConsultationInput);
						
						//setting reference
						updatedPatientConsultationDB.setPatientDetailDB(patientDetailsDB);
						
						//adding an object in a list
						patientConsultationDBS.add(updatedPatientConsultationDB);
					}
				}
			});
			//setting the object
			patientDetailsDB.setPatientRegistrations(patientConsultationDBS);
		}
		
		
		//checking patientAppointments is null or empty or not
		if (!Objects.isNull(patientDetailsInput.getPatientAppointments()) && !patientDetailsInput.getPatientAppointments().isEmpty()) {
			
			//extracting a list of PatientAppointmentDB object from patientDetailsDB
			List <PatientAppointmentDB> patientAppointmentDBS = patientDetailsDB.getPatientAppointments();
			
			//using for-each
			patientDetailsInput.getPatientAppointments().forEach(patientAppointmentInput -> {
				
				//checking if PatientAppointmentDB object is present inside an object or not
				Optional <PatientAppointmentDB> existingPatientAppointment = patientAppointmentDBS.stream().filter(patientRegis -> !Objects.isNull(patientAppointmentInput.getPatientAppointmentId()) && Objects.equals(patientAppointmentInput.getPatientAppointmentId(), patientRegis.getPatientAppointmentId())).findFirst();
				{
					//if an object is present
					if (existingPatientAppointment.isPresent()) {
						
						//extracting index of an object
						int index = patientAppointmentDBS.indexOf(existingPatientAppointment.get());
						
						//extracting PatientAppointmentDB object from Optional
						PatientAppointmentDB patientAppointmentDB = existingPatientAppointment.get();
						
						//calling current class setPatientAppointment() method
						PatientAppointmentDB updatedPatientAppointmentDB = this.setPatientAppointment(patientAppointmentDB, patientAppointmentInput);
						
						//setting reference
						updatedPatientAppointmentDB.setPatientDetailDB(patientDetailsDB);
						
						//updating the object in a list
						patientAppointmentDBS.set(index, updatedPatientAppointmentDB);
					} else {
						
						//crating a new PatientAppointmentDB object
						PatientAppointmentDB newPatientAppointment = new PatientAppointmentDB();
						
						//calling current class setPatientAppointment() method
						PatientAppointmentDB updatedPatientAppointmentDB = this.setPatientAppointment(newPatientAppointment, patientAppointmentInput);
						
						//setting reference
						updatedPatientAppointmentDB.setPatientDetailDB(patientDetailsDB);
						
						//adding an object in a list
						patientAppointmentDBS.add(updatedPatientAppointmentDB);
					}
				}
			});
			//setting the object
			patientDetailsDB.setPatientAppointments(patientAppointmentDBS);
		}
		
		
		//checking roles is null or empty or not
		if (!Objects.isNull(patientDetailsInput.getRoles()) && !patientDetailsInput.getRoles().isEmpty()) {
			//extracting RolesDB object from patientDetailsDB
			Set <RolesDB> rolesDBSet = patientDetailsDB.getRoles();
			
			//using for-each
			patientDetailsInput.getRoles().forEach(rolesDB -> {
				
				//checking if RolesDB object is present inside a db or not
				Optional <RolesDB> existingRole = rolesDBSet.stream().filter(role -> !Objects.isNull(rolesDB.getRolesId()) && Objects.equals(rolesDB.getRolesId(), role.getRolesId())).findFirst();
				
				//if an object is present
				if (existingRole.isPresent()) {
					//calling setRoles() method present in helperInterface
					RolesDB rolesDB1 = this.helperInterf.setRoles(existingRole.get(), rolesDB);
					
					//removing an object from the set
					rolesDBSet.remove(existingRole.get());
					
					//setting reference
					rolesDB1.setPatientDetailsDBSet(Set.of(patientDetailsDB));
					
					//adding an object in a set
					rolesDBSet.add(rolesDB1);
				} else {
					//creating a new RolesDB object
					RolesDB newRole = new RolesDB();
					
					//calling setRoles() method present in helperInterf
					RolesDB rolesDB1 = this.helperInterf.setRoles(newRole, rolesDB);
					
					//setting reference
					rolesDB1.setPatientDetailsDBSet(Set.of(patientDetailsDB));
					
					//adding an object in a set
					rolesDBSet.add(rolesDB1);
				}
			});
			
			//updating the set of roles
			patientDetailsDB.setRoles(rolesDBSet);
		}
		
		//returning patientDetailsDB
		return patientDetailsDB;
	}
	
	//to set patient registration
	private PatientConsultationDB setPatientRegistration(PatientConsultationDB patientConsultationDB, PatientConsultationInput patientConsultationInput) throws RuntimeException{
		
		// Setting data members for patientConsultationDB from patientConsultationInput
		patientConsultationDB.setVisitId(patientConsultationInput.getVisitId());
		patientConsultationDB.setVisitNo(patientConsultationInput.getVisitNo());
		patientConsultationDB.setType(patientConsultationInput.getType());
		patientConsultationDB.setStatus(patientConsultationInput.getStatus());
		patientConsultationDB.setEncounterId(patientConsultationInput.getEncounterId());
		patientConsultationDB.setServiceCenterId(patientConsultationInput.getServiceCenterId());
		patientConsultationDB.setStoreCode(patientConsultationInput.getStoreCode());
		patientConsultationDB.setStoreId(patientConsultationInput.getStoreId());
		patientConsultationDB.setVisitTypeId(patientConsultationInput.getVisitTypeId());
		patientConsultationDB.setVisitTypeCode(patientConsultationInput.getVisitTypeCode());
		patientConsultationDB.setEncounterDate(patientConsultationInput.getEncounterDate());
		patientConsultationDB.setUnitHead(patientConsultationInput.getUnitHead());
		patientConsultationDB.setUnitName(patientConsultationInput.getUnitName());
		patientConsultationDB.setPvsId(patientConsultationInput.getPvsId());
		patientConsultationDB.setBranchName(patientConsultationInput.getBranchName());
		patientConsultationDB.setConsultationStatus(patientConsultationInput.getConsultationStatus());
		patientConsultationDB.setBranchCode(patientConsultationInput.getBranchCode());
		patientConsultationDB.setPatientStatus(patientConsultationInput.getPatientStatus());
		patientConsultationDB.setQueueNo(patientConsultationInput.getQueueNo());
		patientConsultationDB.setConsultationType(patientConsultationInput.getConsultationType());
		patientConsultationDB.setIsDeleted(patientConsultationInput.getIsDeleted());
		patientConsultationDB.setSiteId(daoInterf.getOrganization(patientConsultationInput.getSiteId().getOrganizationMasterId()).orElseThrow(() -> new DgsEntityNotFoundException("Organization not found")));
		
		//checking if doctorInfoInput is null or not
		if (!Objects.isNull(patientConsultationInput.getEmployeeInfoDB())) {
			
			//checking if EmployeeInfoDB is present inside db or not
			Optional <EmployeeInfoDB> doctorInfoDB = this.daoInterf.getUserInfoById(patientConsultationInput.getEmployeeInfoDB().getUserDetailsId());
			
			//if object is present and code is DOCTOR_MASTER_ROLE_1
			if (doctorInfoDB.isPresent() && doctorInfoDB.get().getRoles().stream().anyMatch(doct -> Objects.equals(doct.getRoleMaster().getRoleMasterCode().trim().toUpperCase(Locale.ENGLISH), "DOCTOR_MASTER_ROLE_1"))) {
				patientConsultationDB.setEmployeeInfoDB(doctorInfoDB.get());
			} else {
				throw new RuntimeException("PLEASE SELECT THE CONSULTANT");
			}
		}

		//whose who is a column
		patientConsultationDB.setCreatedBy(patientConsultationInput.getCreatedBy());
		patientConsultationDB.setCreationTimeStamp(Objects.isNull(patientConsultationDB.getCreationTimeStamp()) ? LocalDateTime.now() : patientConsultationInput.getCreationTimeStamp());
		patientConsultationDB.setUpdationTimeStamp(LocalDateTime.now());
		patientConsultationDB.setUpdatedBy(patientConsultationInput.getUpdatedBy());
		
		//returning patientConsultationDB
		return patientConsultationDB;
	}
	
	
	//to set patient appointment
	private PatientAppointmentDB setPatientAppointment(PatientAppointmentDB patientAppointmentDB, PatientAppointmentInput patientAppointmentInput) throws RuntimeException{
		
		// Setting data members for patientConsultationDB from patientConsultationInput
		patientAppointmentDB.setAppointmentDate(patientAppointmentInput.getAppointmentDate());
		patientAppointmentDB.setAppointmentType(patientAppointmentInput.getAppointmentType());
		patientAppointmentDB.setIsActive(patientAppointmentInput.getIsActive());
		
		//checking if departmentMasterInput is null or not
		if (!Objects.isNull(patientAppointmentInput.getDepartmentMasterInput())) {
			
			//checking if EmployeeInfoDB is present inside db or not
			Optional <DepartmentMasterDB> departmentMasterDB = this.daoInterf.getDepartmentMasterById(patientAppointmentInput.getDepartmentMasterInput().getDepartmentMasterId());
			
			//if object is present and code is DOCTOR_MASTER_ROLE_1
			if (departmentMasterDB.isPresent()) {
				patientAppointmentDB.setDepartmentMasterDB(departmentMasterDB.get());
			} else {
				throw new RuntimeException("PLEASE SELECT THE DEPARTMENT");
			}
		}
		
		//checking if doctorInfoInput is null or not
		if (!Objects.isNull(patientAppointmentInput.getEmployeeInfoInput())) {
			
			//checking if EmployeeInfoDB is present inside db or not
			Optional <EmployeeInfoDB> doctorInfoDB = this.daoInterf.getUserInfoById(patientAppointmentInput.getEmployeeInfoInput().getUserDetailsId());
			
			//if object is present and code is DOCTOR_MASTER_ROLE_1
			if (doctorInfoDB.isPresent() && doctorInfoDB.get().getRoles().stream().anyMatch(doct -> Objects.equals(doct.getRoleMaster().getRoleMasterCode().trim().toUpperCase(Locale.ENGLISH), "DOCTOR_MASTER_ROLE_1"))) {
				patientAppointmentDB.setEmployeeInfoDB(doctorInfoDB.get());
			} else {
				throw new RuntimeException("PLEASE SELECT THE CONSULTANT");
			}
		}

		//whose who is a column
		patientAppointmentDB.setCreatedBy(patientAppointmentInput.getCreatedBy());
		patientAppointmentDB.setCreationTimeStamp(Objects.isNull(patientAppointmentDB.getCreationTimeStamp()) ? LocalDateTime.now() : patientAppointmentInput.getCreationTimeStamp());
		patientAppointmentDB.setUpdationTimeStamp(LocalDateTime.now());
		patientAppointmentDB.setUpdatedBy(patientAppointmentInput.getUpdatedBy());

		//returning patientConsultationDB
		return patientAppointmentDB;
	}
	
	
	//method to generate abha otp
	@Override
	public Mono <AbhaGenerateOtpResponse> abhaGenerateOtp(AbhaGenerateOtpInput abhaGenerateOtpInput){
		
		
		System.out.println(abhaGenerateOtpInput);
		
		// Building URI for ABHA GENERATE OTP the patient
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(environment.getProperty("spring.org.externalAbhaBaseUrl") + "send_otp");
		
		// POST API OF ABHA GENERATE OTP
		ResponseEntity <Mono <String>> abhaGenerateOtpResponseEntity = helperInterf.postAPIMono(AbhaGenerateOtpInput.class, Optional.of(abhaGenerateOtpInput), uriComponentsBuilder);
		
		// creating Gson instance
		Gson gson = new GsonBuilder().create();
		
		// Processing the Mono response
		return Objects.requireNonNull(abhaGenerateOtpResponseEntity.getBody()).flatMap(responseEntity -> {
			if (responseEntity != null && responseEntity.startsWith("{")) {
				// Deserialize the response to AbhaValidateOtpResponse class
				AbhaGenerateOtpResponse abhaResponse = gson.fromJson(responseEntity, AbhaGenerateOtpResponse.class);
				return Mono.just(abhaResponse);
			} else {
				return Mono.error(new JsonSyntaxException("Unexpected response format: " + responseEntity));
			}
		}).onErrorResume(e -> {
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException("FAILED TO GENERATE OTP");
		});
		
	}
	
	
	//method to validate abha otp and fetch details
	@Override
	public Mono <AbhaValidateOtpResponse> abhaValidateOtp(AbhaValidateOtpInput abhaValidateOtpInput){
		
		
		// Building URI for ABHA VALIDATE OTP
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(environment.getProperty("spring.org.externalAbhaBaseUrl") + "verify_otp");
		
		// POST API OF ABHA VALIDATE OTP
		ResponseEntity <Mono <String>> abhaValidateOtpResponseMono = helperInterf.postAPIMono(AbhaValidateOtpInput.class, Optional.of(abhaValidateOtpInput), uriComponentsBuilder);
		
		// Creating Gson instance
		Gson gson = new GsonBuilder().create();
		
		// Processing the Mono response
		return Objects.requireNonNull(abhaValidateOtpResponseMono.getBody()).flatMap(responseEntity -> {
			if (responseEntity != null && responseEntity.startsWith("{")) {
				// Deserialize the response to AbhaValidateOtpResponse class
				AbhaValidateOtpResponse abhaResponse = gson.fromJson(responseEntity, AbhaValidateOtpResponse.class);
				return Mono.just(abhaResponse);
			} else {
				return Mono.error(new JsonSyntaxException("Unexpected response format: " + responseEntity));
			}
		}).onErrorResume(e -> {
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException("FAILED ABHA VALIDATE OTP");
		});
	}
	
}

