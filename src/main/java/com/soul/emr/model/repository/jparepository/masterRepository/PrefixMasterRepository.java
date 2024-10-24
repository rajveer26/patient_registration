package com.soul.emr.model.repository.jparepository.masterRepository;

import com.soul.emr.model.entity.masterentity.masterdb.PrefixMasterDB;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrefixMasterRepository extends JpaRepository<PrefixMasterDB, Long>
{

}
