package com.soul.patient.model.repository.jparepository.patientrepository;

import com.soul.patient.model.entity.modelpatient.patientregistrationdb.PatientConsultationDB;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PatientConsultationRepository extends JpaRepository<PatientConsultationDB, Long>
{
	@Transactional
	@Query("SELECT o FROM PatientConsultationDB o WHERE o.doctorCode = :doctorCode AND o.type = :patientType AND (:date IS NULL OR o.encounterDate = :date) AND o.consultationStatus = :consultationStatus")
	Page <PatientConsultationDB> findByEmployeeInfoDB_EmployeeID(@Param("doctorCode") String doctorCode, @Param("patientType") String patientType, @Param("date")LocalDate date, @Param("consultationStatus") String consultationStatus, Pageable pageable);
	
	// Query to count the records based on optional encounterDates and type
	@Transactional
	@Query("SELECT COUNT(p) FROM PatientConsultationDB p WHERE (:type IS NULL OR p.type = :type) AND p.doctorCode = :doctorCode")
	long findByPatientConsultationCount(@Param("type") String type, @Param("doctorCode") String doctorCode);
	
	//Query to fetch patient count for graph with gender and encounterDate
	@Transactional
	@Query("SELECT p.patientDetailDB.gender, p.encounterDate, COUNT(p) FROM PatientConsultationDB p WHERE (:type IS NULL OR p.type = :type) AND p.doctorCode = :doctorCode AND p.encounterDate >= :currentDate AND p.encounterDate < :currentDate GROUP BY p.patientDetailDB.gender, p.encounterDate")
	List <Object[]> findPatientCountGenderAndEncounterDate(@Param("type") String type, @Param("doctorCode") String doctorCode, @Param("currentDate") LocalDate currentDate);
	
	
	//Query to fetch patient count based on consultation type
	@Transactional
	@Query("SELECT COUNT(p) FROM PatientConsultationDB p WHERE p.consultationType IN :consultationTypes AND p.doctorCode = :doctorCode AND p.encounterDate = :currentDate")
	long findPatientCountBasedOnConsultationType(@Param("consultationTypes") List<String> consultationTypes, @Param("doctorCode") String doctorCode, @Param("currentDate") LocalDate currentDate);
	
}
