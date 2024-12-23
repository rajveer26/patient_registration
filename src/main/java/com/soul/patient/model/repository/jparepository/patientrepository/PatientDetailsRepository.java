package com.soul.patient.model.repository.jparepository.patientrepository;

import com.soul.patient.model.entity.modelpatient.patientregistrationdb.PatientDetailsDB;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface PatientDetailsRepository extends JpaRepository<PatientDetailsDB, Long>
{

	@Transactional
	@Query("SELECT a FROM PatientDetailsDB a LEFT JOIN a.communicationInfoDB c WHERE a.firstName = :firstName AND (:dob IS NULL OR a.dob = :dob) AND (:aadhaarNumber IS NULL OR a.aadhaarNumber = :aadhaarNumber) AND (:mobileNumber IS NULL OR c.mobileNumber = :mobileNumber)")
	Optional <PatientDetailsDB> findDuplicatePatientDetails(@Param("firstName") String firstName, @Param("dob") LocalDate dob, @Param("aadhaarNumber") String aadhaarNumber, @Param("mobileNumber") String mobileNumber);


	@Transactional
	@Query("SELECT a FROM PatientDetailsDB a WHERE a.mrno = :mrno")
	Optional <PatientDetailsDB> findPatientDetailsDBByMrno(@Param("mrno") String mrno);
}
