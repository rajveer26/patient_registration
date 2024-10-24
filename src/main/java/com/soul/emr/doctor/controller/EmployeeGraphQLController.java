package com.soul.emr.doctor.controller;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import com.soul.emr.doctor.service.EmployeeService;
import com.soul.emr.model.entity.masterentity.elasticsearchentity.PatientSearch;
import com.soul.emr.model.entity.modelemployee.graphqlentity.EmployeeInfoInput;
import com.soul.emr.model.entity.modelemployee.registrationdb.EmployeeInfoDB;
import com.soul.emr.model.entity.modelemployee.registrationdb.EmployeeScheduleDB;
import com.soul.emr.model.entity.modelpatient.patientregistrationdb.PatientConsultationDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.GraphQlRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@GraphQlRepository
@DgsComponent
@CrossOrigin
public class EmployeeGraphQLController{
	private final EmployeeService employeeService;
	
	@Autowired
	public EmployeeGraphQLController(EmployeeService employeeService){
		super();
		this.employeeService = employeeService;
	}
	
	
	//This authorization is added as one Admin can create another admin, so initially one admin will be created with default admin
	@DgsMutation
	public EmployeeInfoDB createUserInfo(@InputArgument(value = "employeeInfoInput") EmployeeInfoInput employeeInfoInput){
		// Call your service to create the student
		return employeeService.registerUser(employeeInfoInput);
	}
	
	//This authorization is added as one Admin can create another admin, so initially one admin will be created with default admin
	@DgsMutation
	public EmployeeInfoDB updateUserInfo(@InputArgument(value = "employeeInfoInput") EmployeeInfoInput employeeInfoInput){
		// Call your service to create the student
		return employeeService.updateUser(employeeInfoInput);
	}
	
	//query for fetching employee schedules by id
	@DgsQuery
	public Optional<EmployeeScheduleDB> getAllEmployeeSchedulesById(@InputArgument(value = "empScheduleId") Long empScheduleId) {
		return this.employeeService.getEmployeeSchedulesById(empScheduleId);
	}

	//query for fetching employee schedules by ids
	@DgsQuery
	public List<EmployeeScheduleDB> getAllEmployeeSchedulesByIds(@InputArgument(value = "empScheduleIds") List<Long> empScheduleIds) {
		return this.employeeService.getEmployeeSchedulesByIds(empScheduleIds);
	}

	//query to fetch all employee schedules
	@DgsQuery
	public List<EmployeeScheduleDB> getAllEmployeeSchedules() {
		return this.employeeService.getAllEmployeeSchedules();
	}
	
	
	//query to fetch patient registration
	@DgsQuery
	public Page <PatientConsultationDB> fetchPatientConsultation(@InputArgument(value = "doctorCode") String doctorCode, @InputArgument(value = "type") String type, @InputArgument("page") Integer page, @InputArgument(value = "size") Integer size, @InputArgument(value = "date") Optional <LocalDate> date, @InputArgument(value = "consultationStatus") String consultationStatus)
	{
		return employeeService.fetchPatientConsultation(doctorCode, type, page, size, date, consultationStatus);
	}
	
	//query to fetch patient registration
	@DgsQuery
	public long patientCountBasedOnTypeAndDoctorCode(@InputArgument(value = "type") Optional<String> type, @InputArgument(value = "doctorCode") String doctorCode)
	{
		return employeeService.patientCountBasedOnTypeAndDoctorCode(type, doctorCode);
	}
	
	@DgsQuery
	public List <PatientSearch> patientSearchList(@InputArgument(value = "patientSearchQuery") String patientSearchQuery){
		
		return employeeService.searchPatient(patientSearchQuery);
	}
	
	//query to fetch patient count for graph with gender and encounterDate

}

