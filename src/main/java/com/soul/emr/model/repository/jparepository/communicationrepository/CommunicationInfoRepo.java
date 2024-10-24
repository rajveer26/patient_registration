package com.soul.emr.model.repository.jparepository.communicationrepository;

import com.soul.emr.model.entity.communication.communicationinfodb.CommunicationInfoDB;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommunicationInfoRepo extends JpaRepository<CommunicationInfoDB, Long>
{
	@Transactional
	@Query("SELECT a FROM CommunicationInfoDB a WHERE a.contactNumber = :mobileNo")
	Optional <CommunicationInfoDB> findPatientDetailsDBByMobileNo(@Param("mobileNo") String mobileNo);
	
}