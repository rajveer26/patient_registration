package com.soul.emr.patient.patientcontroller;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import com.soul.emr.model.entity.modelpatient.graphqlentity.PatientDetailsInput;
import com.soul.emr.model.entity.modelpatient.patientregistrationdb.PatientDetailsDB;
import com.soul.emr.patient.patientservice.PatientServiceInterf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.GraphQlRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

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
	public PatientDetailsDB savePatientDetails(@InputArgument(value = "patientDetailsInput") PatientDetailsInput patientDetailsInput){
		
		return patientServiceInterf.savePatientDetails(patientDetailsInput);
	}
	
}
