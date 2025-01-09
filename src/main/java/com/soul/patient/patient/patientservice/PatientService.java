package com.soul.patient.patient.patientservice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;
import com.soul.patient.dao.PatientDaoInterf;
import com.soul.patient.helper.HelperInterf;
import com.soul.patient.model.entity.abhaentity.graphqlEntity.AbhaGenerateOtpInput;
import com.soul.patient.model.entity.abhaentity.graphqlEntity.AbhaValidateOtpInput;
import com.soul.patient.model.entity.abhaentity.response.AbhaGenerateOtpResponse;
import com.soul.patient.model.entity.abhaentity.response.AbhaValidateOtpResponse;
import com.soul.patient.model.entity.communication.communicationinfodb.CommunicationInfoDB;
import com.soul.patient.model.entity.communication.graphqlentity.CommunicationInfoInput;
import com.soul.patient.model.entity.elasticsearchentity.PatientSearch;
import com.soul.patient.model.entity.email.EmailEntity;
import com.soul.patient.model.entity.enummaster.Medium;
import com.soul.patient.model.entity.modelonetimepassword.graphqlentity.OneTimePasswordInput;
import com.soul.patient.model.entity.modelonetimepassword.onetimepassworddb.OneTimePasswordEntityDB;
import com.soul.patient.model.entity.modelpatient.graphqlentity.*;
import com.soul.patient.model.entity.modelpatient.patientregistrationdb.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service("patientService")
public class PatientService implements PatientServiceInterf{
	private final PatientDaoInterf daoInterf;
	private final HelperInterf helperInterf;
	private final Environment environment;
	private final PatientDaoInterf patientDaoInterf;
	private final PasswordEncoder passwordEncoder;
	
	//logger
	private final Logger logger = LogManager.getLogger(PatientService.class);
	
	@Autowired
	public PatientService(PatientDaoInterf daoInterf, HelperInterf helperInterf, Environment environment, PatientDaoInterf patientDaoInterf, PasswordEncoder passwordEncoder){
		this.daoInterf        = daoInterf;
		this.helperInterf     = helperInterf;
		this.environment      = environment;
		this.patientDaoInterf = patientDaoInterf;
		this.passwordEncoder  = passwordEncoder;
	}
	
	//method to save patientDetails
	@Override
	public PatientDetailsDB savePatientDetails(PatientDetailsInput patientDetailsInput){
		try{

			Optional <PatientDetailsDB> existingPatient = this.daoInterf.findDuplicatePatientDetails(patientDetailsInput.getFirstName(), Optional.ofNullable(patientDetailsInput.getDob()), Optional.ofNullable(patientDetailsInput.getAadhaarNumber()), Optional.of(patientDetailsInput.getCommunicationInfoDB().parallelStream().sequential().map(CommunicationInfoInput::getMobileNumber).findFirst()).orElse(null));
			
			//if an object is present
			if (existingPatient.isPresent()) {
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
		
		patientDetailsDB.setPrefixMasterDB(patientDetailsInput.getPrefixMasterDB());
		patientDetailsDB.setRoleMasterId(patientDetailsInput.getRoleMasterId());
		patientDetailsDB.setGender(patientDetailsInput.getGender());
		patientDetailsDB.setMaritalStatus(patientDetailsInput.getMaritalStatus());
		patientDetailsDB.setDob(patientDetailsInput.getDob());
		patientDetailsDB.setAge(patientDetailsInput.getAge());
		patientDetailsDB.setPatientImage(patientDetailsInput.getPatientImage());
		patientDetailsDB.setRegisteredOn(LocalDate.now());
		patientDetailsDB.setSmartCardId(patientDetailsInput.getSmartCardId());
		patientDetailsDB.setEsiIpNumber(patientDetailsDB.getEsiIpNumber());
		
		
		//checking if communicationInfoDB is null or empty or not
		if (!Objects.isNull(patientDetailsInput.getCommunicationInfoDB()) && !patientDetailsInput.getCommunicationInfoDB().isEmpty()) {
			
			//extracting a list of CommunicationInfoDB from patientDetailsDB object
			Set <CommunicationInfoDB> communicationInfoDBList = patientDetailsDB.getCommunicationInfoDB();
			
			//using for-each
			patientDetailsInput.getCommunicationInfoDB().parallelStream().forEach(communicationInfoInput -> {
				
				//checking if CommunicationInfoDB is already present inside a db or not using communicationInfoId
				Optional <CommunicationInfoDB> existingCommunication = communicationInfoDBList.parallelStream().filter(comuniInfo -> !Objects.isNull(communicationInfoInput.getCommunicationInfoId()) && Objects.equals(communicationInfoInput.getCommunicationInfoId(), comuniInfo.getCommunicationInfoId())).findFirst();
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
			patientDetailsInput.getPatientRegistrations().parallelStream().forEach(patientConsultationInput -> {
				
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


		//checking patientEmergencyContacts is null or empty or not
		if (!Objects.isNull(patientDetailsInput.getPatientEmergencyContacts()) && !patientDetailsInput.getPatientEmergencyContacts().isEmpty()) {

			//extracting a list of PatientEmergencyContactDB object from patientDetailsDB
			List <PatientEmergencyContacts> patientEmergencyContactDBS = patientDetailsDB.getPatientEmergencyContacts();

			//using for-each
			patientDetailsInput.getPatientEmergencyContacts().parallelStream().forEach(patientEmergencyContactInput -> {

				//checking if PatientEmergencyContactDB object is present inside an object or not
				Optional <PatientEmergencyContacts> existingPatientEmergencyContact = patientEmergencyContactDBS.parallelStream().filter(patientRegis -> !Objects.isNull(patientEmergencyContactInput.getEmergencyContactInfoId()) && Objects.equals(patientEmergencyContactInput.getEmergencyContactInfoId(), patientRegis.getEmergencyContactInfoId())).findFirst();
				{
					//if an object is present
					if (existingPatientEmergencyContact.isPresent()) {

						//extracting index of an object
						int index = patientEmergencyContactDBS.indexOf(existingPatientEmergencyContact.get());

						//extracting PatientEmergencyContactDB object from Optional
						PatientEmergencyContacts patientEmergencyContactDB = existingPatientEmergencyContact.get();

						//calling current class setPatientEmergencyContact() method
						PatientEmergencyContacts updatedPatientEmergencyContactDB = this.setPatientEmergencyContact(patientEmergencyContactDB, patientEmergencyContactInput);

						//setting reference
						updatedPatientEmergencyContactDB.setPatientDetailDB(patientDetailsDB);

						//updating the object in a list
						patientEmergencyContactDBS.set(index, updatedPatientEmergencyContactDB);
					} else {

						//crating a new PatientEmergencyContactDB object
						PatientEmergencyContacts newPatientEmergencyContact = new PatientEmergencyContacts();

						//calling current class setPatientEmergencyContact() method
						PatientEmergencyContacts updatedPatientEmergencyContactDB = this.setPatientEmergencyContact(newPatientEmergencyContact, patientEmergencyContactInput);

						//setting reference
						updatedPatientEmergencyContactDB.setPatientDetailDB(patientDetailsDB);

						//adding an object in a list
						patientEmergencyContactDBS.add(updatedPatientEmergencyContactDB);
					}
				}
			});
			//setting the object
			patientDetailsDB.setPatientEmergencyContacts(patientEmergencyContactDBS);
		}


		//checking patientInsuranceDetails is null or empty or not
		if (!Objects.isNull(patientDetailsInput.getPatientInsuranceDetails()) && !patientDetailsInput.getPatientInsuranceDetails().isEmpty()) {

			//extracting a list of patientInsuranceDB object from patientDetailsDB
			List <PatientInsuranceDB> patientInsuranceDBS = patientDetailsDB.getPatientInsuranceDetails();

			//using for-each
			patientDetailsInput.getPatientInsuranceDetails().parallelStream().forEach(patientInsuranceInput -> {

				//checking if patientInsuranceDB object is present inside an object or not
				Optional <PatientInsuranceDB> existingPatientInsurance = patientInsuranceDBS.parallelStream().filter(patientInsurance -> !Objects.isNull(patientInsuranceInput.getPatientInsuranceInfoId()) && Objects.equals(patientInsuranceInput.getPatientInsuranceInfoId(), patientInsurance.getPatientInsuranceInfoId())).findFirst();
				{
					//if an object is present
					if (existingPatientInsurance.isPresent()) {

						//extracting index of an object
						int index = patientInsuranceDBS.indexOf(existingPatientInsurance.get());

						//extracting patientInsuranceDB object from Optional
						PatientInsuranceDB patientInsuranceDB = existingPatientInsurance.get();

						//calling current class setPatientInsuranceDetails() method
						PatientInsuranceDB updatedPatientInsuranceDB = this.setPatientInsuranceDetails(patientInsuranceDB, patientInsuranceInput);

						//setting reference
						updatedPatientInsuranceDB.setPatientDetailDB(patientDetailsDB);

						//updating the object in a list
						patientInsuranceDBS.set(index, updatedPatientInsuranceDB);
					} else {

						//crating a new patientInsuranceDB object
						PatientInsuranceDB newPatientInsurance = new PatientInsuranceDB();

						//calling current class setPatientInsuranceDetails() method
						PatientInsuranceDB updatedPatientInsuranceDB = this.setPatientInsuranceDetails(newPatientInsurance, patientInsuranceInput);

						//setting reference
						updatedPatientInsuranceDB.setPatientDetailDB(patientDetailsDB);

						//adding an object in a list
						patientInsuranceDBS.add(updatedPatientInsuranceDB);
					}
				}
			});
			//setting the object
			patientDetailsDB.setPatientInsuranceDetails(patientInsuranceDBS);
		}


		//checking patientMrnLinks is null or empty or not
		if (!Objects.isNull(patientDetailsInput.getPatientMrnLinks()) && !patientDetailsInput.getPatientMrnLinks().isEmpty()) {

			//extracting a list of patientInsuranceDB object from patientDetailsDB
			List <PatientMrnLinkDB> patientMrnLinkDBS = patientDetailsDB.getPatientMrnLinks();

			//using for-each
			patientDetailsInput.getPatientMrnLinks().parallelStream().forEach(patientMrnLinkInput -> {

				//checking if patientInsuranceDB object is present inside an object or not
				Optional <PatientMrnLinkDB> existingPatientMrnLink = patientMrnLinkDBS.parallelStream().filter(patientMrnLink -> !Objects.isNull(patientMrnLinkInput.getPatientMrnLinkId()) && Objects.equals(patientMrnLinkInput.getPatientMrnLinkId(), patientMrnLink.getPatientMrnLinkId())).findFirst();
				{
					//if an object is present
					if (existingPatientMrnLink.isPresent()) {

						//extracting index of an object
						int index = patientMrnLinkDBS.indexOf(existingPatientMrnLink.get());

						//extracting patientInsuranceDB object from Optional
						PatientMrnLinkDB patientMrnLinkDB = existingPatientMrnLink.get();

						//calling current class setPatientInsuranceDetails() method
						PatientMrnLinkDB updatedPatientMrnLinkDB = this.setPatientMrnLinkDetails(patientMrnLinkDB, patientMrnLinkInput);

						//setting reference
						updatedPatientMrnLinkDB.setPatientDetailDB(patientDetailsDB);

						//updating the object in a list
						patientMrnLinkDBS.set(index, updatedPatientMrnLinkDB);
					} else {

						//crating a new patientInsuranceDB object
						PatientMrnLinkDB newPatientMrnLink = new PatientMrnLinkDB();

						//calling current class setPatientInsuranceDetails() method
						PatientMrnLinkDB updatedPatientMrnLinkDB = this.setPatientMrnLinkDetails(newPatientMrnLink, patientMrnLinkInput);

						//setting reference
						updatedPatientMrnLinkDB.setPatientDetailDB(patientDetailsDB);

						//adding an object in a list
						patientMrnLinkDBS.add(updatedPatientMrnLinkDB);
					}
				}
			});
			//setting the object
			patientDetailsDB.setPatientMrnLinks(patientMrnLinkDBS);
		}
		
		
		//checking patientAppointments is null or empty or not
		if (!Objects.isNull(patientDetailsInput.getPatientAppointments()) && !patientDetailsInput.getPatientAppointments().isEmpty()) {
			
			//extracting a list of PatientAppointmentDB object from patientDetailsDB
			List <PatientAppointmentDB> patientAppointmentDBS = patientDetailsDB.getPatientAppointments();
			
			//using for-each
			patientDetailsInput.getPatientAppointments().parallelStream().forEach(patientAppointmentInput -> {
				
				//checking if PatientAppointmentDB object is present inside an object or not
				Optional <PatientAppointmentDB> existingPatientAppointment = patientAppointmentDBS.parallelStream().filter(patientRegis -> !Objects.isNull(patientAppointmentInput.getPatientAppointmentId()) && Objects.equals(patientAppointmentInput.getPatientAppointmentId(), patientRegis.getPatientAppointmentId())).findFirst();
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
		patientConsultationDB.setSiteId(patientConsultationInput.getSiteId());
		patientConsultationDB.setDoctorCode(patientConsultationInput.getDoctorCode());

		//whose who is a column
		patientConsultationDB.setCreatedBy(patientConsultationInput.getCreatedBy());
		patientConsultationDB.setCreationTimeStamp(Objects.isNull(patientConsultationDB.getCreationTimeStamp()) ? LocalDateTime.now() : patientConsultationInput.getCreationTimeStamp());
		patientConsultationDB.setUpdationTimeStamp(LocalDateTime.now());
		patientConsultationDB.setUpdatedBy(patientConsultationInput.getUpdatedBy());
		
		//returning patientConsultationDB
		return patientConsultationDB;
	}

	private PatientEmergencyContacts setPatientEmergencyContact(PatientEmergencyContacts patientEmergencyContactDB, PatientEmergencyContactInput patientEmergencyContactInput) throws RuntimeException {

		// Set values from the input object to the database object
		patientEmergencyContactDB.setEmergencyContactInfoId(patientEmergencyContactInput.getEmergencyContactInfoId());
		patientEmergencyContactDB.setContactPersonRelation(patientEmergencyContactInput.getContactPersonRelation());
		patientEmergencyContactDB.setContactPersonName(patientEmergencyContactInput.getContactPersonName());
		patientEmergencyContactDB.setContactPersonMobileNumber(patientEmergencyContactInput.getContactPersonMobileNumber());
		patientEmergencyContactDB.setContactPersonWhatsappNumber(patientEmergencyContactInput.getContactPersonWhatsappNumber());
		patientEmergencyContactDB.setContactPersonEmailId(patientEmergencyContactInput.getContactPersonEmailId());

		// whose who is a column
		patientEmergencyContactDB.setCreatedBy(patientEmergencyContactInput.getCreatedBy());
		patientEmergencyContactDB.setCreationTimeStamp(Objects.isNull(patientEmergencyContactDB.getCreationTimeStamp()) ? LocalDateTime.now() : patientEmergencyContactInput.getCreationTimeStamp());
		patientEmergencyContactDB.setUpdationTimeStamp(LocalDateTime.now());
		patientEmergencyContactDB.setUpdatedBy(patientEmergencyContactInput.getUpdatedBy());

		return patientEmergencyContactDB;
	}

	private PatientInsuranceDB setPatientInsuranceDetails(PatientInsuranceDB patientInsuranceDB, PatientInsuranceDetailInput patientInsuranceDetailInput) throws RuntimeException {

		// Map input properties to the database entity
		patientInsuranceDB.setPatientInsuranceInfoId(patientInsuranceDetailInput.getPatientInsuranceInfoId());
		patientInsuranceDB.setInsuranceType(patientInsuranceDetailInput.getInsuranceType());
		patientInsuranceDB.setInsuranceName(patientInsuranceDetailInput.getInsuranceName());
		patientInsuranceDB.setInsuranceNumber(patientInsuranceDetailInput.getInsuranceNumber());
		patientInsuranceDB.setInsuranceValidFrom(patientInsuranceDetailInput.getInsuranceValidFrom());
		patientInsuranceDB.setInsuranceValidTo(patientInsuranceDetailInput.getInsuranceValidTo());

		// whose who is a column
		patientInsuranceDB.setCreatedBy(patientInsuranceDetailInput.getCreatedBy());
		patientInsuranceDB.setCreationTimeStamp(Objects.isNull(patientInsuranceDB.getCreationTimeStamp()) ? LocalDateTime.now() : patientInsuranceDetailInput.getCreationTimeStamp());
		patientInsuranceDB.setUpdationTimeStamp(LocalDateTime.now());
		patientInsuranceDB.setUpdatedBy(patientInsuranceDetailInput.getUpdatedBy());

		return patientInsuranceDB;
	}

	private PatientMrnLinkDB setPatientMrnLinkDetails(PatientMrnLinkDB patientMrnLinkDB, PatientMrnLinkInput patientMrnLinkInput) throws RuntimeException{

		// Map input properties to the database entity
		patientMrnLinkDB.setPatientMrnLinkId(patientMrnLinkInput.getPatientMrnLinkId());
		patientMrnLinkDB.setFamilyMrn(patientMrnLinkInput.getFamilyMrn());
		patientMrnLinkDB.setFamilyMrnRelation(patientMrnLinkInput.getFamilyMrnRelation());

		// whose who is a column
		patientMrnLinkDB.setCreatedBy(patientMrnLinkInput.getCreatedBy());
		patientMrnLinkDB.setCreationTimeStamp(Objects.isNull(patientMrnLinkDB.getCreationTimeStamp()) ? LocalDateTime.now() : patientMrnLinkInput.getCreationTimeStamp());
		patientMrnLinkDB.setUpdationTimeStamp(LocalDateTime.now());
		patientMrnLinkDB.setUpdatedBy(patientMrnLinkInput.getUpdatedBy());

		return patientMrnLinkDB;
	}
	
	
	//to set patient appointment
	private PatientAppointmentDB setPatientAppointment(PatientAppointmentDB patientAppointmentDB, PatientAppointmentInput patientAppointmentInput) throws RuntimeException{
		
		// Setting data members for patientConsultationDB from patientConsultationInput
		patientAppointmentDB.setAppointmentDate(patientAppointmentInput.getAppointmentDate());
		patientAppointmentDB.setAppointmentType(patientAppointmentInput.getAppointmentType());
		patientAppointmentDB.setIsActive(patientAppointmentInput.getIsActive());
		

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


	@Override
	public List<PatientDetailsDB> getAllPatientDetails(){

		try {

			return Optional.ofNullable(daoInterf.getAllPatientDetails()).orElse(new ArrayList<>());

		}
		catch (Exception e) {
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			throw new RuntimeException("FAILED FETCHING PATIENT DETAILS");
		}
	}
	
	@Override
	public Map <String, Object> generateOtp(OneTimePasswordInput oneTimePasswordInput){
		
		//creating a new HashMap object
		ConcurrentHashMap <String, Object> responseMap = new ConcurrentHashMap <>();
		
		try{
			//checking if identifier is of type email or not
			if (Objects.equals(oneTimePasswordInput.getMedium(), Medium.EMAIL)) {
				
				//generating otp
				Integer otp = helperInterf.generateOTP();
				
				//checking if the mail is valid or not
				Boolean isValidEmail = helperInterf.isValidEmail(oneTimePasswordInput.getCommunicationInfoDB().getEmailId());
				
				//if email is not valid
				if (!isValidEmail) {
					responseMap.put("message", "Not a Valid Email");
					responseMap.put("status", Boolean.FALSE);
					
					//returning response in a map
					return responseMap;
				}
				else {
					
					//declaring OneTimePasswordEntityDB
					OneTimePasswordEntityDB oneTimePassword;
					
					//checking if a OneTimePasswordEntityDB object is already present in the db or not using communicationInfoId
					Optional<OneTimePasswordEntityDB> existingOTP = this.patientDaoInterf.getOtpInfoByCommunicationInfoId(oneTimePasswordInput.getCommunicationInfoDB().getCommunicationInfoId());
					
					//if an object is present
					if(existingOTP.isPresent())
					{
						//calling current class setOneTimePassword to update data-members
						oneTimePassword = this.setOneTimePassword(existingOTP.get(), oneTimePasswordInput);
						
						//setting reference
						oneTimePassword.setOtp(passwordEncoder.encode(String.valueOf(otp)));
					}
					else {
						//creating a new OneTimePasswordEntityDB object
						OneTimePasswordEntityDB oneTimePasswordEntity = new OneTimePasswordEntityDB();
						
						//calling current class setOneTimePassword to set data-members
						oneTimePassword = this.setOneTimePassword(oneTimePasswordEntity, oneTimePasswordInput);
						
						//setting reference
						oneTimePassword.setOtp(passwordEncoder.encode(String.valueOf(otp)));
					}
					
					//saving OTP in a DB
					OneTimePasswordEntityDB otpEntity = patientDaoInterf.saveOtp(oneTimePassword);
					
					//checking if an object is null or not
					if (!Objects.isNull(otpEntity.getOtpId())) {
						
						//calling getEmailEntity method which is a static method present in EmployeeServiceInterf emailEntity to send mail
						EmailEntity emailEntity = PatientServiceInterf.getEmailEntity(oneTimePasswordInput, otp);
						System.err.println(Thread.currentThread().getName());
						//calling sendTemplateEmail() method present in helperInterf
						helperInterf.sendTemplateEmail(emailEntity);
						
						//putting response in a map
						responseMap.put("message", "email send");
						responseMap.put("status", Boolean.TRUE);
					} else {
						
						//putting response in a map
						responseMap.put("message", "OTP IS NULL");
						responseMap.put("status", Boolean.FALSE);
					}
					//returning response
					return responseMap;
					
				}
			} else {
				//for mobile otp
				return null;
			}
			
		}
		//catch block
		catch(Exception e){
			
			//logging exception
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			
			//throwing a runTimeException with a message
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//method to set oneTimePassword data-members
	private OneTimePasswordEntityDB setOneTimePassword(OneTimePasswordEntityDB oneTimePasswordEntityDB, OneTimePasswordInput oneTimePasswordInput)
	{
		oneTimePasswordEntityDB.setMedium(oneTimePasswordInput.getMedium());
		
		//checking if the enum is EMAIL
		if(Objects.equals(oneTimePasswordInput.getMedium(), Medium.EMAIL))
		{
			oneTimePasswordEntityDB.setIdentifier(oneTimePasswordInput.getCommunicationInfoDB().getEmailId());
		}
		else
		{
			oneTimePasswordEntityDB.setIdentifier(String.valueOf(oneTimePasswordInput.getCommunicationInfoDB().getMobileNumber()));
			
		}
		oneTimePasswordEntityDB.setValidUpto(oneTimePasswordInput.getValidUpto());
		oneTimePasswordEntityDB.setIsValidated(oneTimePasswordInput.getIsValidated());
		oneTimePasswordEntityDB.setCommunicationInfoDB(this.patientDaoInterf.getCommunicationById(oneTimePasswordInput.getCommunicationInfoDB().getCommunicationInfoId()).orElseThrow(() -> new DgsEntityNotFoundException("Communication entity not found")));
		oneTimePasswordEntityDB.setCreationTimeStamp(LocalDateTime.now());
		
		return oneTimePasswordEntityDB;
	}
	
	@Override
	public Map <String, Object> verifyOtp(OneTimePasswordInput oneTimePasswordEntity){
		
		//creating a hashMap object
		ConcurrentHashMap <String, Object> responseMap = new ConcurrentHashMap <>();
		
		try{
			//extracting a OneTimePasswordEntityDB object from DB
			Optional <OneTimePasswordEntityDB> otpInfo = patientDaoInterf.getOtpInfo(oneTimePasswordEntity.getIdentifier(), oneTimePasswordEntity.getCommunicationInfoDB().getCommunicationInfoId());
			
			//if an object is present
			if (otpInfo.isPresent()) {
				
				//checking if identifier, otp is same or not and validUpto is in the range or not
				if(Objects.equals(oneTimePasswordEntity.getIdentifier(), otpInfo.get().getIdentifier()) && passwordEncoder.matches(oneTimePasswordEntity.getOtp(), otpInfo.get().getOtp()) && otpInfo.get().getValidUpto().isBefore(LocalDateTime.now().plusMinutes(5)) && otpInfo.get().getValidUpto().isAfter(LocalDateTime.now().minusMinutes(5)))
				{
					//setting isValidated to true
					otpInfo.get().setIsValidated(Boolean.TRUE);
					
					//saving object in a db by calling saveOtp() method present in dao layer
					OneTimePasswordEntityDB otpEntity = patientDaoInterf.saveOtp(otpInfo.get());
					
					//if an object is successfully updated in a db
					if(!Objects.isNull(otpEntity.getOtpId()))
					{
						//putting response in a map
						responseMap.put("message", "OTP IS VALIDATED");
						responseMap.put("status", Boolean.TRUE);
					}
					else
					{
						//putting response in a map
						responseMap.put("message", "OTP IS NOT VALIDATED");
						responseMap.put("status", Boolean.FALSE);
					}
					
					//returning response
					return responseMap;
				}
				
				//throwing a new runtimeException
				throw new RuntimeException("ENTITY_NOT_FOUND");
				
			} else {
				//throwing a new runtimeException
				throw new RuntimeException("OTP_INFO_NOT_FOUND");
			}
		}
		//catch block
		catch(Exception e){
			//logging exception
			logger.catching(e);
			logger.error(e.fillInStackTrace());
			
			//putting an entry object
			responseMap.put("exception", e.getMessage());
			responseMap.put("status", Boolean.FALSE);
			
			//returning response as a map
			return responseMap;
		}
	}
	
	@Override
	public List <PatientSearch> searchPatient(String query){
		return this.patientDaoInterf.searchPatient(query);
	}
	
	@Override
	public long patientCountBasedOnTypeAndDoctorCode(Optional <String> type, String doctorCode){
		try{
			return patientDaoInterf.fetchPatientCountByDoctorNumberAndType(type, doctorCode);
		}
		catch(Exception e){
			logger.error(e.fillInStackTrace());
			logger.catching(e);
			
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//service layer to fetch patient consultation
	@Override
	public Page <PatientConsultationDB> fetchPatientConsultation(String doctorCode, String type, Integer page, Integer size, Optional <LocalDate> date, String consultationStatus){
		//using pageable to fetch according to the page
		Pageable pageable = PageRequest.of(page, size);
		
		//calling getPatientRegistrationDetailByDoctorCodeAndType() method present in dao layer to fetch patients
		return patientDaoInterf.getPatientRegistrationDetailByDoctorCodeAndType(doctorCode, type, date, consultationStatus, pageable);
	}
	
	//service layer to fetch patient count based on doctorNumber and type
	@Override
	public long fetchPatientCountByDoctorNumberAndType(Optional <String> type, String doctorCode)
	{
		return patientDaoInterf.fetchPatientCountByDoctorNumberAndType(type, doctorCode);
	}
	
	//service layer to fetch patient count for graph with gender and encounterDate
	@Override
	public List <Object[]> fetchPatientCountByGenderAndEncounterDate(Optional <String> type, String doctorCode, LocalDate currentDate){
		return patientDaoInterf.fetchPatientCountBasedOnConsultationType(type, doctorCode, currentDate);
	}
	
	//service layer to fetch patient count based on consultationTypes
	@Override
	public long fetchPatientCountByGenderAndEncounterDate(List<String> consultationTypes, String doctorCode, LocalDate currentDate)
	{
		return patientDaoInterf.fetchPatientCountBasedOnConsultationType(consultationTypes, doctorCode, currentDate);
	}
	
}

