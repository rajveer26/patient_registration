package com.soul.emr.patient.patientcontroller;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import com.soul.emr.model.entity.abhaentity.graphqlEntity.AbhaGenerateOtpInput;
import com.soul.emr.model.entity.abhaentity.graphqlEntity.AbhaValidateOtpInput;
import com.soul.emr.model.entity.abhaentity.response.AbhaGenerateOtpResponse;
import com.soul.emr.model.entity.abhaentity.response.AbhaValidateOtpResponse;
import com.soul.emr.model.entity.modelpatient.graphqlentity.PatientDetailsInput;
import com.soul.emr.model.entity.modelpatient.patientregistrationdb.PatientDetailsDB;
import com.soul.emr.patient.patientservice.PatientServiceInterf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.GraphQlRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import reactor.core.publisher.Mono;

import java.lang.classfile.Opcode;
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
	
}
