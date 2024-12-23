package com.soul.patient.model.repository.jparepository.onetimepasswordrepository;

import com.soul.patient.model.entity.modelonetimepassword.onetimepassworddb.OneTimePasswordEntityDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OneTimePasswordRepo extends JpaRepository<OneTimePasswordEntityDB, Long> {

    @Query("SELECT o FROM OneTimePasswordEntityDB o WHERE o.identifier = :identifier AND o.communicationInfoDB.CommunicationInfoId = :communicationInfoId")
    Optional<OneTimePasswordEntityDB> findByIdentifier(@Param("identifier") String identifier, @Param("communicationInfoId") Long communicationInfoId);
    
    @Query("SELECT o FROM OneTimePasswordEntityDB o WHERE o.communicationInfoDB.CommunicationInfoId = :communicationId")
    Optional<OneTimePasswordEntityDB> findByCommunicationInfoId(@Param("communicationId") Long communicationId);
    
    @Query("SELECT o FROM OneTimePasswordEntityDB o WHERE o.createdBy = :userDetailId")
    Optional<OneTimePasswordEntityDB> findOtpByUserId(@Param("userDetailId") Long userDetailId);
    
}
