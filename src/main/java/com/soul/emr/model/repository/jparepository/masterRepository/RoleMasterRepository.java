package com.soul.emr.model.repository.jparepository.masterRepository;

import com.soul.emr.model.entity.masterentity.masterdb.RoleMasterDB;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleMasterRepository extends JpaRepository<RoleMasterDB, Long>
{

}
