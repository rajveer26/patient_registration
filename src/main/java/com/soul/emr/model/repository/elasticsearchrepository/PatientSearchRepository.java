package com.soul.emr.model.repository.elasticsearchrepository;

import com.soul.emr.model.entity.elasticsearchentity.PatientSearch;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PatientSearchRepository extends ElasticsearchRepository<PatientSearch, Long>
{
	
	@Query("{ \"bool\": { \"should\": [" +
			"{ \"match\": { \"patient_name\": { \"query\": \"?1\", \"operator\": \"and\" }}}, " + // Match for patient_name
			"{ \"match\": { \"mrno\": { \"query\": \"?2\" }}}, " + // Match for mrno
			"{ \"nested\": { \"path\": \"communicationInfoDB\", " +
			"\"query\": { \"bool\": { \"should\": [" +
			"{ \"match\": { \"communicationInfoDB.contact_number\": { \"query\": \"?0\", \"operator\": \"and\" }}}, " + // Match for email_id
			"{ \"match\": { \"communicationInfoDB.email_id\": { \"query\": \"?3\" }} } " + // Match for contact_number
			"]}}}}" +
			"]}}")
	List<PatientSearch> findByPatientName(@Param("contactNumber") String contactNumber, @Param("patientName") String patientName, @Param("mrno") String mrno, @Param("emailId") String emailId);
}
