package com.soul.patient.patient.patientcontroller;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import com.soul.patient.model.entity.abhaentity.graphqlEntity.AbhaGenerateOtpInput;
import com.soul.patient.model.entity.abhaentity.graphqlEntity.AbhaValidateOtpInput;
import com.soul.patient.model.entity.abhaentity.response.AbhaGenerateOtpResponse;
import com.soul.patient.model.entity.abhaentity.response.AbhaValidateOtpResponse;
import com.soul.patient.model.entity.elasticsearchentity.PatientSearch;
import com.soul.patient.model.entity.enummaster.*;
import com.soul.patient.model.entity.modelpatient.graphqlentity.PatientDetailsInput;
import com.soul.patient.model.entity.modelpatient.patientregistrationdb.PatientConsultationDB;
import com.soul.patient.model.entity.modelpatient.patientregistrationdb.PatientDetailsDB;
import com.soul.patient.patient.patientservice.PatientServiceInterf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.GraphQlRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@GraphQlRepository
@DgsComponent
@CrossOrigin
public class PatientController
{
	private final PatientServiceInterf patientServiceInterf;
	
	@Autowired
	public PatientController(PatientServiceInterf patientServiceInterf){
		this.patientServiceInterf = patientServiceInterf;
	}

	//mutation to save patient details
	@DgsMutation
	public Mono<AbhaGenerateOtpResponse> abhaGenerateOtp(@InputArgument(value = "abhaGenerateOtpInput") AbhaGenerateOtpInput abhaGenerateOtpInput){

		return patientServiceInterf.abhaGenerateOtp(abhaGenerateOtpInput);
	}

	//mutation to save patient details
	@DgsMutation
	public Mono <AbhaValidateOtpResponse> abhaValidateOtp(@InputArgument(value = "abhaValidateOtpInput") AbhaValidateOtpInput abhaValidateOtpInput){

		return patientServiceInterf.abhaValidateOtp(abhaValidateOtpInput);
	}
	
	//mutation to save patient details
	@DgsMutation
	public PatientDetailsDB savePatientDetails(@InputArgument(value = "patientDetailsInput") PatientDetailsInput patientDetailsInput){
		
		return patientServiceInterf.savePatientDetails(patientDetailsInput);
	}

	@DgsQuery
	public List<PatientDetailsDB> getAllPatientDetails(){

		return patientServiceInterf.getAllPatientDetails();
	}

	@DgsQuery
	public List<Gender> getAllGenders() {
		return Arrays.asList(Gender.values());
	}

	@DgsQuery
	public List<PatientType> getAllPatientTypes() {
		return Arrays.asList(PatientType.values());
	}

	@DgsQuery
	public List<MaritalStatus> getAllMaritalStatus() {
		return Arrays.asList(MaritalStatus.values());
	}

	@DgsQuery
	public List<Relationship> getAllRelationship() {
		return Arrays.asList(Relationship.values());
	}

	@DgsQuery
	public List<InsuranceType> getAllInsuranceType() {
		return Arrays.asList(InsuranceType.values());
	}
	
	//query to fetch patient registration
	@DgsQuery
	public Page <PatientConsultationDB> fetchPatientConsultation(@InputArgument(value = "doctorCode") String doctorCode, @InputArgument(value = "type") String type, @InputArgument("page") Integer page, @InputArgument(value = "size") Integer size, @InputArgument(value = "date") Optional <LocalDate> date, @InputArgument(value = "consultationStatus") String consultationStatus)
	{
		return patientServiceInterf.fetchPatientConsultation(doctorCode, type, page, size, date, consultationStatus);
	}
	
	//query to fetch patient registration
	@DgsQuery
	public long patientCountBasedOnTypeAndDoctorCode(@InputArgument(value = "type") Optional<String> type, @InputArgument(value = "doctorCode") String doctorCode)
	{
		return patientServiceInterf.patientCountBasedOnTypeAndDoctorCode(type, doctorCode);
	}
	
	@DgsQuery
	public List <PatientSearch> patientSearchList(@InputArgument(value = "patientSearchQuery") String patientSearchQuery){
		
		return patientServiceInterf.searchPatient(patientSearchQuery);
	}
	
}
