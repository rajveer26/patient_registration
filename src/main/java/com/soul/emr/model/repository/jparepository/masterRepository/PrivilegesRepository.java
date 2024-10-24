package com.soul.emr.model.repository.jparepository.masterRepository;

import com.soul.emr.model.entity.masterentity.masterdb.PrivilegesMasterDB;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegesRepository extends JpaRepository<PrivilegesMasterDB, Long>
{

}
