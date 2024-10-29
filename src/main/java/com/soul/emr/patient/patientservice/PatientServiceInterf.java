package com.soul.emr.patient.patientservice;

import com.soul.emr.model.entity.abhaentity.graphqlEntity.AbhaGenerateOtpInput;
import com.soul.emr.model.entity.abhaentity.graphqlEntity.AbhaValidateOtpInput;
import com.soul.emr.model.entity.abhaentity.response.AbhaGenerateOtpResponse;
import com.soul.emr.model.entity.abhaentity.response.AbhaValidateOtpResponse;
import com.soul.emr.model.entity.modelpatient.graphqlentity.PatientDetailsInput;
import com.soul.emr.model.entity.modelpatient.patientregistrationdb.PatientDetailsDB;

import java.util.Optional;

public interface PatientServiceInterf
{
	PatientDetailsDB savePatientDetails(PatientDetailsInput patientDetailsInput);

	Optional<AbhaGenerateOtpResponse> abhaGenerateOtp(AbhaGenerateOtpInput abhaGenerateOtpInput);

	Optional<AbhaValidateOtpResponse> abhaValidateOtp(AbhaValidateOtpInput abhaValidateOtpInput);

}
