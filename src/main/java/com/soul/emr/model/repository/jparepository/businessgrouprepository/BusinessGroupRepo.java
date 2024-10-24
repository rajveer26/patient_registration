package com.soul.emr.model.repository.jparepository.businessgrouprepository;

import com.soul.emr.model.entity.modelbusinessgroup.businessgroupdb.BusinessGroupDB;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessGroupRepo extends JpaRepository<BusinessGroupDB, Long>
{

}
