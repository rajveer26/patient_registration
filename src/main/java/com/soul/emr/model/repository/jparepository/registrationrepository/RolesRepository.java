package com.soul.emr.model.repository.jparepository.registrationrepository;


import com.soul.emr.model.entity.modelemployee.registrationdb.RolesDB;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<RolesDB, Long>
{

}
