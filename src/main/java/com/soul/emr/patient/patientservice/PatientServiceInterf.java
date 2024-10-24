package com.soul.emr.patient.patientservice;

import com.soul.emr.model.entity.modelpatient.graphqlentity.PatientDetailsInput;
import com.soul.emr.model.entity.modelpatient.patientregistrationdb.PatientDetailsDB;

public interface PatientServiceInterf
{
	PatientDetailsDB savePatientDetails(PatientDetailsInput patientDetailsInput);

}
