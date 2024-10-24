package com.soul.emr.model.repository.jparepository.patientrepository;

import com.soul.emr.model.entity.modelpatient.patientregistrationdb.PatientDetailsDB;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PatientDetailsRepository extends JpaRepository<PatientDetailsDB, Long>
{
	@Transactional
	@Query("SELECT a FROM PatientDetailsDB a WHERE a.mrno = :mrno")
	Optional <PatientDetailsDB> findPatientDetailsDBByMrno(@Param("mrno") String mrno);
}
