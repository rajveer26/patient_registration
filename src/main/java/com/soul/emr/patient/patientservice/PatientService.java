package com.soul.emr.patient.patientservice;

import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;
import com.soul.emr.dao.EmrDaoInterf;
import com.soul.emr.helper.HelperInterf;
import com.soul.emr.model.entity.communication.communicationinfodb.CommunicationInfoDB;
import com.soul.emr.model.entity.modelemployee.registrationdb.EmployeeInfoDB;
import com.soul.emr.model.entity.modelemployee.registrationdb.RolesDB;
import com.soul.emr.model.entity.modelpatient.graphqlentity.PatientDetailsInput;
import com.soul.emr.model.entity.modelpatient.graphqlentity.PatientConsultationInput;
import com.soul.emr.model.entity.modelpatient.patientregistrationdb.PatientConsultationDB;
import com.soul.emr.model.entity.modelpatient.patientregistrationdb.PatientDetailsDB;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service("patientService")
public class PatientService implements PatientServiceInterf{
	private final EmrDaoInterf daoInterf;
	private final HelperInterf helperInterf;

	//logger
	private final Logger logger = LogManager.getLogger(PatientService.class);

	@Autowired
	public PatientService(EmrDaoInterf daoInterf, HelperInterf helperInterf){
		this.daoInterf            = daoInterf;
		this.helperInterf         = helperInterf;
	}

	//method to save patientDetails
	@Override
	public PatientDetailsDB savePatientDetails(PatientDetailsInput patientDetailsInput){
		try{
			//calling getPatientDetailsByMrno() method present in daoInterf
			Optional <PatientDetailsDB> existingPatient = this.daoInterf.getPatientDetailsByMrno(patientDetailsInput.getMrno());

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

		//returning patientConsultationDB
		return patientConsultationDB;
	}

}

