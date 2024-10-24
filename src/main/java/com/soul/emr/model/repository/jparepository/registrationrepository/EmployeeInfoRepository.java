package com.soul.emr.model.repository.jparepository.registrationrepository;

import com.soul.emr.model.entity.modelemployee.registrationdb.EmployeeInfoDB;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeInfoRepository extends JpaRepository <EmployeeInfoDB, Long>
{
	
	@Transactional
	@Query("SELECT a FROM EmployeeInfoDB a WHERE (:email IS NULL OR a.userCredentialsDB.username = :email)")
	Optional <EmployeeInfoDB> findByEmail(@Param("email") String email);

	@Transactional
	@Query("SELECT a FROM EmployeeInfoDB a WHERE a.userCredentialsDB.username = :username")
	Optional <EmployeeInfoDB> findByUserDetailsUserName(@Param("username") String username);
	
	
}
