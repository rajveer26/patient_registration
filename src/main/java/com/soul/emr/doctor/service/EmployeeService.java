package com.soul.emr.doctor.service;

import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;
import com.netflix.graphql.dgs.exceptions.MissingDgsEntityFetcherException;
import com.soul.emr.dao.EmrDaoInterf;
import com.soul.emr.model.entity.communication.communicationinfodb.CommunicationInfoDB;
import com.soul.emr.model.entity.enummaster.Medium;
import com.soul.emr.model.entity.masterentity.elasticsearchentity.PatientSearch;
import com.soul.emr.model.entity.masterentity.graphqlentity.DepartmentMasterInput;
import com.soul.emr.model.entity.masterentity.masterdb.DepartmentMasterDB;
import com.soul.emr.model.entity.modelbusinessgroup.businessgroupdb.BusinessGroupDB;
import com.soul.emr.model.entity.modelbusinessgroup.graphqlentity.BusinessGroupInput;
import com.soul.emr.model.entity.modelbusinessgroup.organization.graphqlentity.OrganizationGroupInput;
import com.soul.emr.model.entity.modelbusinessgroup.organization.organizationdb.OrganizationDB;
import com.soul.emr.model.entity.modelemployee.registrationdb.EmployeeInfoDB;
import com.soul.emr.model.entity.modelemployee.registrationdb.EmployeeScheduleDB;
import com.soul.emr.model.entity.modelonetimepassword.onetimepassworddb.OneTimePasswordEntityDB;
import com.soul.emr.model.entity.modelemployee.graphqlentity.*;
import com.soul.emr.model.entity.modelemployee.registrationdb.RolesDB;
import com.soul.emr.model.entity.modelemployee.registrationdb.UserCredentialsDB;
import com.soul.emr.model.entity.modelonetimepassword.graphqlentity.OneTimePasswordInput;
import com.soul.emr.model.entity.email.EmailEntity;
import com.soul.emr.helper.HelperInterf;
import com.soul.emr.model.entity.modelpatient.patientregistrationdb.PatientConsultationDB;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@Service("registrationService")
public class EmployeeService implements EmployeeServiceInterf{
	
	private final EmrDaoInterf emrDaoInterf;
	private final PasswordEncoder passwordEncoder;
	private final HelperInterf helperInterf;
	
	@Autowired
	public EmployeeService(EmrDaoInterf emrDaoInterf, PasswordEncoder passwordEncoder, HelperInterf helperInterf){
		super();
		this.emrDaoInterf         = emrDaoInterf;
		this.passwordEncoder      = passwordEncoder;
		this.helperInterf         = helperInterf;
	}
	
	
	//logger
	private final Logger logger = LogManager.getLogger(EmployeeService.class);
	
	
	//service layer to register a user for emr
	@Override
	public EmployeeInfoDB registerUser(EmployeeInfoInput employeeInfoInput){
		
		try{
			//checking whether the email already exists or not
			Optional <EmployeeInfoDB> userInfoExists = emrDaoInterf.getUserInfo(employeeInfoInput.getUserCredentialsInput().getUsername());
			
			//if an object is present
			if (userInfoExists.isPresent()) {
				
				//returning bad request
				throw new DgsEntityNotFoundException("UserName Or Password already in use");
			} else {
				
				//checking if userCredentials is empty or not
				if (!Objects.isNull(employeeInfoInput.getUserCredentialsInput())) {
					//checking if mail is valid or not
					if (!helperInterf.isValidEmail(employeeInfoInput.getUserCredentialsInput().getUsername()))
						throw new RuntimeException("INVALID_EMAIL");
				} else {
					throw new MissingDgsEntityFetcherException("userName is empty");
				}
				
				//creating a new EmployeeInfoDB object
				EmployeeInfoDB newUserObject = new EmployeeInfoDB();
				
				//calling current class setDoctorDetails() method
				EmployeeInfoDB newUser = this.setDoctorDetails(employeeInfoInput, newUserObject);
				
				//checking if a business group is null or not
				if (!Objects.isNull(employeeInfoInput.getOrganizationDBSet()) && !employeeInfoInput.getOrganizationDBSet().isEmpty()) {
					
					//storing a list of organizationMasterId
					List <Long> organizationIds = employeeInfoInput.getOrganizationDBSet().stream().map(OrganizationGroupInput::getOrganizationMasterId).toList();
					
					//calling getOrganizationByIds() method present in emrDaoInterf
					List <OrganizationDB> organizationDBS = this.emrDaoInterf.getOrganizationByIds(organizationIds).stream().peek(organizationDB -> organizationDB.setEmployeeInfoDB(Set.of(newUser))).toList();
					
					//setting reference
					newUser.setOrganizationDBSet(new HashSet <>(organizationDBS));
				}
				
				//checking if communication is null or empty or not
				if (!Objects.isNull(employeeInfoInput.getCommunicationInfoDB()) && !employeeInfoInput.getCommunicationInfoDB().isEmpty()) {
					//creating a new arrayList object of type CommunicationInfoDB
					ArrayList <CommunicationInfoDB> communicationInfoDBArrayList = new ArrayList <>();
					
					//using for-each
					employeeInfoInput.getCommunicationInfoDB().forEach(communicationInfoInput -> {
						
						//creating a new CommunicationInfoDB object
						CommunicationInfoDB newCommunicationInfo = new CommunicationInfoDB();
						
						//calling current class setCommunicationDataMembers() method
						CommunicationInfoDB communicationInfoDB = this.helperInterf.setCommunicationDataMembers(newCommunicationInfo, communicationInfoInput);
						
						//setting reference
						communicationInfoDB.setEmployeeInfoDB(newUser);
						
						//adding an object in a list
						communicationInfoDBArrayList.add(communicationInfoDB);
						
						//timing the size of the list
						communicationInfoDBArrayList.trimToSize();
					});
					
					//setting communicationInfoDBArrayList in newUser
					newUser.setCommunicationInfoDB(communicationInfoDBArrayList);
				}
				
				//setting credentials
				//creating a new UserCredentialsDB object
				UserCredentialsDB newUserCredentials = new UserCredentialsDB();
				
				//calling current class setCredentials method
				UserCredentialsDB credentialsDB = this.setCredentials(newUserCredentials, employeeInfoInput.getUserCredentialsInput());
				
				//setting reference
				credentialsDB.setEmployeeInfo(newUser);
				
				//setting reference
				newUser.setUserCredentialsDB(newUserCredentials);
				
				//setting roles
				HashSet <RolesDB> newRoles = new HashSet <>();
				
				//using for-each
				employeeInfoInput.getRoles().forEach(rolesDB -> {
					
					//creating a new RoleDB object
					RolesDB newRole = new RolesDB();
					
					//calling current class setRoles method
					RolesDB rolesDB1 = this.helperInterf.setRoles(newRole, rolesDB);
					
					//checking is rolesDB1 os null or not
					if (!Objects.isNull(rolesDB1)) {
						//setting parent reference
						rolesDB1.setEmployeeInfoDB(Set.of(newUser));
						
						//adding an object in a set
						newRoles.add(rolesDB1);
					}
					
				});
				
				//setting roles in newUser object
				newUser.setRoles(newRoles);
				
				//if a department object is not null and the department list is not empty
				if (!Objects.isNull(employeeInfoInput.getDepartmentMasterDBSet()) && !employeeInfoInput.getDepartmentMasterDBSet().isEmpty()) {
					//extracting a list of departmentIds
					List <Long> departmentIds = employeeInfoInput.getDepartmentMasterDBSet().parallelStream().map(DepartmentMasterInput::getDepartmentId).toList();
					
					//extraction list of DepartmentMasterDB
					List <DepartmentMasterDB> departments = this.emrDaoInterf.getDepartmentByIds(departmentIds).parallelStream().peek(departmentMasterDB -> departmentMasterDB.setEmployeeInfoDB(Set.of(newUser))).toList();
					
					//setting a set of departments in newUser
					newUser.setDepartmentMasterDBSet(new HashSet <>(departments));
				}
				
				//returning response
				return this.emrDaoInterf.saveUserInfo(newUser);
			}
		}
		
		//catch block
		catch(Exception e){
			logger.error(e.fillInStackTrace());
			logger.catching(e);
			
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//service layer to update a user
	@Override
	public EmployeeInfoDB updateUser(EmployeeInfoInput employeeInfoInput){
		
		try{
			//checking whether the email already exists or not
			Optional <EmployeeInfoDB> userInfoExists = emrDaoInterf.getUserInfo(employeeInfoInput.getUserCredentialsInput().getUsername());
			
			//if an object is present
			if (userInfoExists.isEmpty()) {
				
				//returning bad request
				throw new DgsEntityNotFoundException("User does not exist");
			} else {
				
				//calling current class setDoctorDetails() method to update the object
				EmployeeInfoDB updatedDetail = this.setDoctorDetails(employeeInfoInput, userInfoExists.get());
				
				//checking if a business group is null or not
				if (!Objects.isNull(employeeInfoInput.getOrganizationDBSet()) && !employeeInfoInput.getOrganizationDBSet().isEmpty()) {
					
					//extracting a set of OrganizationDB from the db
					Set <OrganizationDB> organizationDBSet = updatedDetail.getOrganizationDBSet();
					
					//using for-each
					employeeInfoInput.getOrganizationDBSet().forEach(organizationInput -> {
						
						//checking if the object is already present in the db or not using organizationMasterId
						Optional <OrganizationDB> existingOrganization = organizationDBSet.parallelStream().filter(organizationDB -> !Objects.isNull(organizationInput.getOrganizationMasterId()) && Objects.equals(organizationDB.getOrganizationMasterId(), organizationInput.getOrganizationMasterId())).findFirst();
						
						//if an object is present
						if (existingOrganization.isPresent()) {
							//calling current class setOrganizationDataMembers() method to update organization of a user
							this.setOrganizationDataMembers(existingOrganization.get(), organizationInput);
							
						} else {
							//creating a new OrganizationDB object
							OrganizationDB newOrganization = new OrganizationDB();
							
							//calling current class setOrganizationDataMembers() method to update organization of a user
							OrganizationDB updatedOrganization = this.setOrganizationDataMembers(newOrganization, organizationInput);
							
							//setting reference
							updatedOrganization.setEmployeeInfoDB(Set.of(updatedDetail));
							
							//adding an object in a set
							organizationDBSet.add(updatedOrganization);
							
						}
					});
					
					//setting a organization set
					updatedDetail.setOrganizationDBSet(organizationDBSet);
				}
				
				//checking if communication is null or empty or not
				if (!Objects.isNull(employeeInfoInput.getCommunicationInfoDB()) && !employeeInfoInput.getCommunicationInfoDB().isEmpty()) {
					//extracting a list of CommunicationInfoDB for the user from db
					List <CommunicationInfoDB> communicationInfoDBList = updatedDetail.getCommunicationInfoDB();
					
					//using for-each
					employeeInfoInput.getCommunicationInfoDB().forEach(communicationInfoInput -> {
						
						//checking if an object is already present in a db or not using communicationInfoId
						Optional <CommunicationInfoDB> existingCommunication = communicationInfoDBList.parallelStream().filter(communicationInfoDB -> !Objects.isNull(communicationInfoInput.getCommunicationInfoId()) && Objects.equals(communicationInfoInput.getCommunicationInfoId(), communicationInfoDB.getCommunicationInfoId())).findFirst();
						
						//if an object is present
						if (existingCommunication.isPresent()) {
							//extracting index of an object
							int index = communicationInfoDBList.indexOf(existingCommunication.get());
							
							//calling setCommunicationDataMembers() method present in helperInterf
							CommunicationInfoDB communicationInfoDB = this.helperInterf.setCommunicationDataMembers(existingCommunication.get(), communicationInfoInput);
							
							//setting reference
							communicationInfoDB.setEmployeeInfoDB(updatedDetail);
							
							//updating the object
							communicationInfoDBList.set(index, communicationInfoDB);
						} else {
							//creating a new CommunicationInfoDB object
							CommunicationInfoDB newCommunicationInfo = new CommunicationInfoDB();
							
							//calling current class setCommunicationDataMembers() method
							CommunicationInfoDB communicationInfoDB = this.helperInterf.setCommunicationDataMembers(newCommunicationInfo, communicationInfoInput);
							
							//setting reference
							communicationInfoDB.setEmployeeInfoDB(updatedDetail);
							
							//adding an object in a list
							communicationInfoDBList.add(communicationInfoDB);
							
						}
					});
					
					//setting communicationInfoDBArrayList in updatedDetail of a user
					updatedDetail.setCommunicationInfoDB(communicationInfoDBList);
				}
				
				//checking if userCredentialsInput object is null or not
				if (!Objects.isNull(employeeInfoInput.getUserCredentialsInput())) {

					//fetching oneTimePassword object from db using userId
					Optional <OneTimePasswordEntityDB> oneTimePasswordEntityDB = this.emrDaoInterf.getOtpInfoByUserDetailId(userInfoExists.get().getUserDetailsId());

					//if an object is present
					if (oneTimePasswordEntityDB.isPresent()) {

						//if otp is validated and is between currentDateTime -5 and currentDateTime +5
						if (oneTimePasswordEntityDB.get().getIsValidated() && oneTimePasswordEntityDB.get().getValidUpto().isBefore(LocalDateTime.now().minusMinutes(5)) && oneTimePasswordEntityDB.get().getValidUpto().isAfter(LocalDateTime.now().minusMinutes(5))) {
							//setting credentials
							UserCredentialsDB existingUserCredentials = updatedDetail.getUserCredentialsDB();

							//calling current class setCredentials method
							UserCredentialsDB credentialsDB = this.setCredentials(existingUserCredentials, employeeInfoInput.getUserCredentialsInput());

							//setting reference
							credentialsDB.setEmployeeInfo(updatedDetail);

							//setting reference
							updatedDetail.setUserCredentialsDB(credentialsDB);
						}
						else
						{
							//throwing a runTimeException with a message
							throw new RuntimeException("OTP IS NOT VALID PLEASE REGENERATE NEW OTP");
						}
					}
					else
					{
						//throwing a runTimeException with a message
						throw new RuntimeException("PLEASE VALIDATE THE OTP FIRST");
					}
				}
				//checking if roles object is null or empty or not
				if (!Objects.isNull(employeeInfoInput.getRoles()) && !employeeInfoInput.getRoles().isEmpty()) {
					
					//extracting a set of roles for the user from db
					Set <RolesDB> rolesDBSet = updatedDetail.getRoles();
					
					//using for-each
					employeeInfoInput.getRoles().forEach(roleInput -> {
						
						//checking if a role object is already present in a db or not
						Optional <RolesDB> existingRole = rolesDBSet.parallelStream().filter(role -> !Objects.isNull(roleInput.getRolesId()) && Objects.equals(roleInput.getRolesId(), role.getRolesId())).findFirst();
						
						//if existingRole is already present
						if (existingRole.isPresent()) {
							//calling setRoles() method present in helperInterf
							this.helperInterf.setRoles(existingRole.get(), roleInput);
						} else {
							//creating a new RoleDB object
							RolesDB newRole = new RolesDB();
							
							//calling current class setRoles method
							RolesDB rolesDB1 = this.helperInterf.setRoles(newRole, roleInput);
							
							//checking is rolesDB1 os null or not
							if (!Objects.isNull(rolesDB1)) {
								//setting parent reference
								rolesDB1.setEmployeeInfoDB(Set.of(updatedDetail));
								
								//adding an object in a set
								rolesDBSet.add(rolesDB1);
							}
						}
						
					});
					
					//updating a set of roles
					updatedDetail.setRoles(rolesDBSet);
					
				}
				
				//if a department object is not null and the department list is not empty
				if (!Objects.isNull(employeeInfoInput.getDepartmentMasterDBSet()) && !employeeInfoInput.getDepartmentMasterDBSet().isEmpty()) {
					//extracting a set of DepartmentMasterDB for a user from db
					Set <DepartmentMasterDB> departmentMasterDBSet = updatedDetail.getDepartmentMasterDBSet();
					
					//using for-each
					employeeInfoInput.getDepartmentMasterDBSet().forEach(departmentInput -> {
						
						//checking if an object is already present in a db or not
						Optional <DepartmentMasterDB> existingDepartment = departmentMasterDBSet.parallelStream().filter(departmentMasterDB -> !Objects.isNull(departmentInput.getDepartmentMasterId()) && Objects.equals(departmentMasterDB.getDepartmentMasterId(), departmentInput.getDepartmentMasterId())).findFirst();
						
						//if an object is present
						if (existingDepartment.isPresent()) {
							//calling setDepartmentMaster() method present in mastersServiceInterf
							emrDaoInterf.getDepartmentMasterById(departmentInput.getDepartmentMasterId());
						} else {
							
							
							//adding an object in a set
							departmentMasterDBSet.add(emrDaoInterf.getDepartmentMasterById(departmentInput.getDepartmentMasterId()).orElseThrow(() -> new DgsEntityNotFoundException("Department master not found")));
						}
						
					});
					
					//updating departmentMasterDBSet
					updatedDetail.setDepartmentMasterDBSet(departmentMasterDBSet);
				}
				
				//returning response
				return emrDaoInterf.saveUserInfo(updatedDetail);
			}
		}
		
		//catch block
		catch(Exception e){
			logger.error(e.fillInStackTrace());
			logger.catching(e);
			
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//setting user details
	private EmployeeInfoDB setDoctorDetails(EmployeeInfoInput employeeInfoInput, EmployeeInfoDB employeeInfoDB){
		//setting data-members
		employeeInfoDB.setDob(employeeInfoInput.getDob());
		employeeInfoDB.setFullName(employeeInfoInput.getFullName());
		employeeInfoDB.setGender(employeeInfoInput.getGender());
		employeeInfoDB.setUserImage(employeeInfoInput.getUserImage());
		employeeInfoDB.setDoctorCode(employeeInfoInput.getDoctorCode());
		employeeInfoDB.setUserSignature(employeeInfoInput.getUserSignature());
		employeeInfoDB.setProvider(employeeInfoInput.getProvider());
		employeeInfoDB.setIsActive(employeeInfoInput.getIsActive());
		employeeInfoDB.setPrefixMasterDB(emrDaoInterf.fetchPrefixMasterById(employeeInfoInput.getPrefixMasterDB().getPrefixMasterId()).orElseThrow(() -> new DgsEntityNotFoundException("Prefix Master not found")));
		
		//whose who is a column
		employeeInfoDB.setCreationTimeStamp(Objects.isNull(employeeInfoDB.getCreationTimeStamp()) ? LocalDateTime.now() : employeeInfoDB.getCreationTimeStamp());
		employeeInfoDB.setCreatedBy(Objects.isNull(employeeInfoDB.getCreatedBy()) ? employeeInfoInput.getCreatedBy() : employeeInfoDB.getCreatedBy());
		employeeInfoDB.setUpdationTimeStamp(LocalDateTime.now());
		employeeInfoDB.setUpdatedBy(employeeInfoInput.getUpdatedBy());
		
		
		//returning employeeInfoDB object
		return employeeInfoDB;
	}
	
	
	//setting credentials
	private UserCredentialsDB setCredentials(UserCredentialsDB dpCredentials, UserCredentialsInput jsonCredentials){
		//setting data-members
		dpCredentials.setUsername(jsonCredentials.getUsername());
		dpCredentials.setPassword(passwordEncoder.encode(jsonCredentials.getPassword()));
		
		//whose who is a column
		dpCredentials.setCreatedBy(Objects.isNull(dpCredentials.getCreatedBy()) ? jsonCredentials.getCreatedBy() : dpCredentials.getCreatedBy());
		dpCredentials.setCreationTimeStamp(Objects.isNull(dpCredentials.getCreationTimeStamp()) ? LocalDateTime.now() : dpCredentials.getCreationTimeStamp());
		dpCredentials.setUpdationTimeStamp(LocalDateTime.now());
		dpCredentials.setUpdatedBy(jsonCredentials.getUpdatedBy());
		
		//returning dpCredentials object
		return dpCredentials;
	}
	
	
	//service layer to get a business group
	@Override
	public Page <BusinessGroupDB> getBusinessGroup(Integer page, Integer size){
		//declaring pageable of type Pageable
		Pageable pageable = PageRequest.of(page, size);
		
		//calling getBusinessGroup() method present in emrDaoInterf
		return emrDaoInterf.getBusinessGroup(pageable);
	}
	
	//service layer to set a business group
	private BusinessGroupDB setBusinessGroup(BusinessGroupDB businessGroupDB, BusinessGroupInput businessGroupInput){
		//setting data-members
		businessGroupDB.setBusinessGroupId(!Objects.isNull(businessGroupInput.getBusinessGroupId()) ? businessGroupInput.getBusinessGroupId() : null);
		businessGroupDB.setBusinessGroupCode(businessGroupInput.getBusinessGroupCode());
		businessGroupDB.setBusinessGroupName(businessGroupInput.getBusinessGroupName());
		businessGroupDB.setBusinessGroupTIN(businessGroupInput.getBusinessGroupTIN());
		businessGroupDB.setBusinessGroupGSTNumber(businessGroupInput.getBusinessGroupGSTNumber());
		businessGroupDB.setIsActive(businessGroupInput.getIsActive());
		
		//for setting organization
		//checking if the getCommunicationInfoDB s null or empty or not
		if (!Objects.isNull(businessGroupInput.getOrganizationDBList()) && !businessGroupInput.getOrganizationDBList().isEmpty()) {
			//extracting a list of OrganizationDB from businessGroupDB
			List <OrganizationDB> organizationDBArrayList = businessGroupDB.getOrganizationDBList();
			
			//using for-each
			businessGroupInput.getOrganizationDBList().forEach(organizationGroupInput -> {
				
				//checking if an object is present in db or not using organizationMasterId
				Optional <OrganizationDB> existingOrganization = organizationDBArrayList.parallelStream().filter(organizationDB -> !Objects.isNull(organizationDB.getOrganizationMasterId()) && Objects.equals(organizationDB.getOrganizationMasterId(), organizationGroupInput.getOrganizationMasterId())).findFirst();
				
				//if an object is present
				if (existingOrganization.isPresent()) {
					//extracting index of an object
					int index = organizationDBArrayList.indexOf(existingOrganization.get());
					
					//calling current class setOrganizationDataMembers() method
					OrganizationDB organizationDb = this.setOrganizationDataMembers(existingOrganization.get(), organizationGroupInput);
					
					//setting reference
					organizationDb.setBusinessGroupDB(businessGroupDB);
					
					//updating an object in a list
					organizationDBArrayList.set(index, organizationDb);
				} else {
					//creating a new OrganizationDB object
					OrganizationDB newOrganizationDb = new OrganizationDB();
					
					//calling current class setOrganizationDataMembers() method
					OrganizationDB organizationDb = this.setOrganizationDataMembers(newOrganizationDb, organizationGroupInput);
					
					//setting reference
					organizationDb.setBusinessGroupDB(businessGroupDB);
					
					//adding an object in a list
					organizationDBArrayList.add(organizationDb);
					
				}
			});
			
			//setting organizationList in a newBusinessGroup
			businessGroupDB.setOrganizationDBList(organizationDBArrayList);
		}
		
		//whose who is column
		businessGroupDB.setCreationTimeStamp(Objects.isNull(businessGroupDB.getCreationTimeStamp()) ? LocalDateTime.now() : businessGroupDB.getCreationTimeStamp());
		businessGroupDB.setCreatedBy(Objects.isNull(businessGroupDB.getCreatedBy()) ? businessGroupInput.getCreatedBy() : businessGroupDB.getCreatedBy());
		businessGroupDB.setUpdationTimeStamp(LocalDateTime.now());
		businessGroupDB.setUpdatedBy(businessGroupInput.getUpdatedBy());
		
		//returning businessGroupDB object
		return businessGroupDB;
	}
	
	//method to set organization data members
	private OrganizationDB setOrganizationDataMembers(OrganizationDB organizationDB, OrganizationGroupInput organizationGroupInput){
		//setting data-members
		organizationDB.setOrganizationName(organizationGroupInput.getOrganizationName());
		organizationDB.setOrganizationCode(organizationGroupInput.getOrganizationCode());
		organizationDB.setOrganizationLatitude(organizationGroupInput.getOrganizationLatitude());
		organizationDB.setOrganizationLongitude(organizationGroupInput.getOrganizationLongitude());
		
		//for setting communication
		//checking if the getCommunicationInfoDB s null or empty or not
		if (!Objects.isNull(organizationGroupInput.getCommunicationInfoDB()) && !organizationGroupInput.getCommunicationInfoDB().isEmpty()) {
			//extracting a list of CommunicationInfoDB form organizationDB
			List <CommunicationInfoDB> communicationInfoDBList = organizationDB.getCommunicationInfoDB();
			
			//using for-each
			organizationGroupInput.getCommunicationInfoDB().forEach(communicationInfoInput -> {
				
				//checking if an object is present in db or not using communicationInfoId
				Optional <CommunicationInfoDB> existingCommunication = communicationInfoDBList.parallelStream().filter(communicationInfoDB -> !Objects.isNull(communicationInfoDB.getCommunicationInfoId()) && !Objects.equals(communicationInfoDB.getCommunicationInfoId(), communicationInfoInput.getCommunicationInfoId())).findFirst();
				
				//if an object is present
				if (existingCommunication.isPresent()) {
					//extracting index of an object
					int index = communicationInfoDBList.indexOf(existingCommunication.get());
					
					//calling current class setCommunicationDataMembers() method
					CommunicationInfoDB communicationInfoDB = this.helperInterf.setCommunicationDataMembers(existingCommunication.get(), communicationInfoInput);
					
					//setting reference
					communicationInfoDB.setOrganizationDB(organizationDB);
					
					//updating an object in a list
					communicationInfoDBList.set(index, communicationInfoDB);
				} else {
					//creating a new CommunicationInfoDB object
					CommunicationInfoDB newCommunicationInfoDB = new CommunicationInfoDB();
					
					//calling current class setCommunicationDataMembers() method
					CommunicationInfoDB communicationInfoDB = this.helperInterf.setCommunicationDataMembers(newCommunicationInfoDB, communicationInfoInput);
					
					//setting reference
					communicationInfoDB.setOrganizationDB(organizationDB);
					
					//adding an object in a list
					communicationInfoDBList.add(communicationInfoDB);
				}
			});
			
			//setting communicationList in a newBusinessGroup
			organizationDB.setCommunicationInfoDB(communicationInfoDBList);
		}
		
		//whose who is a column
		organizationDB.setCreatedBy(Objects.isNull(organizationDB.getCreatedBy()) ? organizationGroupInput.getCreatedBy() : organizationDB.getCreatedBy());
		organizationDB.setCreationTimeStamp(Objects.isNull(organizationDB.getCreationTimeStamp()) ? LocalDateTime.now() : organizationDB.getCreationTimeStamp());
		organizationDB.setUpdationTimeStamp(LocalDateTime.now());
		organizationDB.setUpdatedBy(organizationGroupInput.getUpdatedBy());
		
		//returning organizationDB object
		return organizationDB;
	}
	
	@Override
	public Map <String, Object> generateOtp(OneTimePasswordInput oneTimePasswordInput){
		
		//creating a new HashMap object
		HashMap <String, Object> responseMap = new HashMap <>();
		
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
					Optional<OneTimePasswordEntityDB> existingOTP = this.emrDaoInterf.getOtpInfoByCommunicationInfoId(oneTimePasswordInput.getCommunicationInfoDB().getCommunicationInfoId());
					
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
					OneTimePasswordEntityDB otpEntity = emrDaoInterf.saveOtp(oneTimePassword);
					
					//checking if an object is null or not
					if (!Objects.isNull(otpEntity.getOtpId())) {
						
						//calling getEmailEntity method which is a static method present in EmployeeServiceInterf emailEntity to send mail
						EmailEntity emailEntity = EmployeeServiceInterf.getEmailEntity(oneTimePasswordInput, otp);
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
			oneTimePasswordEntityDB.setIdentifier(String.valueOf(oneTimePasswordInput.getCommunicationInfoDB().getContactPersonMobileNumber()));
			
		}
		oneTimePasswordEntityDB.setValidUpto(oneTimePasswordInput.getValidUpto());
		oneTimePasswordEntityDB.setIsValidated(oneTimePasswordInput.getIsValidated());
		oneTimePasswordEntityDB.setCommunicationInfoDB(this.emrDaoInterf.getCommunicationById(oneTimePasswordInput.getCommunicationInfoDB().getCommunicationInfoId()).orElseThrow(() -> new DgsEntityNotFoundException("Communication entity not found")));
		oneTimePasswordEntityDB.setCreationTimeStamp(LocalDateTime.now());
		
		return oneTimePasswordEntityDB;
	}
	
	@Override
	public Map <String, Object> verifyOtp(OneTimePasswordInput oneTimePasswordEntity){
		
		//creating a hashMap object
		HashMap <String, Object> responseMap = new HashMap <>();
		
		try{
			//extracting a OneTimePasswordEntityDB object from DB
			Optional <OneTimePasswordEntityDB> otpInfo = emrDaoInterf.getOtpInfo(oneTimePasswordEntity.getIdentifier(), oneTimePasswordEntity.getCommunicationInfoDB().getCommunicationInfoId());
			
			//if an object is present
			if (otpInfo.isPresent()) {
				
				//checking if identifier, otp is same or not and validUpto is in the range or not
			 if(Objects.equals(oneTimePasswordEntity.getIdentifier(), otpInfo.get().getIdentifier()) && passwordEncoder.matches(oneTimePasswordEntity.getOtp(), otpInfo.get().getOtp()) && otpInfo.get().getValidUpto().isBefore(LocalDateTime.now().plusMinutes(5)) && otpInfo.get().getValidUpto().isAfter(LocalDateTime.now().minusMinutes(5)))
			 {
				 //setting isValidated to true
				 otpInfo.get().setIsValidated(Boolean.TRUE);
				 
				 //saving object in a db by calling saveOtp() method present in dao layer
				 OneTimePasswordEntityDB otpEntity = emrDaoInterf.saveOtp(otpInfo.get());
				 
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
	
	
	//Method to fetch userDetails using email
	@Override
	public EmployeeInfoDB fetchUserDetails(String userName){
		
		return emrDaoInterf.getUserInfo(userName.trim()).orElseThrow(() -> new DgsEntityNotFoundException("No USER FOUND WITH THIS EMAIL"));
		
	}
	//service layer to get employee schedule details by id
	@Override
	public Optional<EmployeeScheduleDB> getEmployeeSchedulesById(Long id)
	{
		return this.emrDaoInterf.getEmployeeSchedulesById(id);
	}

	//service layer to get employee schedule by ids
	@Override
	public List<EmployeeScheduleDB> getEmployeeSchedulesByIds(List<Long> ids)
	{
		return this.emrDaoInterf.getEmployeeScheduleByIds(ids);
	}

	//service layer to fetch all employee schedules
	@Override
	public List<EmployeeScheduleDB> getAllEmployeeSchedules()
	{
		return this.emrDaoInterf.getAllEmployeeSchedules();
	}
	
	
	@Override
	public List <PatientSearch> searchPatient(String query){
		return this.emrDaoInterf.searchPatient(query);
	}
	
	@Override
	public long patientCountBasedOnTypeAndDoctorCode(Optional <String> type, String doctorCode){
		try{
			return emrDaoInterf.fetchPatientCountByDoctorNumberAndType(type, doctorCode);
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
		return emrDaoInterf.getPatientRegistrationDetailByDoctorCodeAndType(doctorCode, type, date, consultationStatus, pageable);
	}
	
	//service layer to fetch patient count based on doctorNumber and type
	@Override
	public long fetchPatientCountByDoctorNumberAndType(Optional <String> type, String doctorCode)
	{
		return emrDaoInterf.fetchPatientCountByDoctorNumberAndType(type, doctorCode);
	}
	
	//service layer to fetch patient count for graph with gender and encounterDate
	@Override
	public List <Object[]> fetchPatientCountByGenderAndEncounterDate(Optional <String> type, String doctorCode, LocalDate currentDate){
		return emrDaoInterf.fetchPatientCountBasedOnConsultationType(type, doctorCode, currentDate);
	}
	
	//service layer to fetch patient count based on consultationTypes
	@Override
	public long fetchPatientCountByGenderAndEncounterDate(List<String> consultationTypes, String doctorCode, LocalDate currentDate)
	{
		return emrDaoInterf.fetchPatientCountBasedOnConsultationType(consultationTypes, doctorCode, currentDate);
	}
	
}
