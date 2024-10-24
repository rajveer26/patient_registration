package com.soul.emr.model.repository.jparepository.masterRepository;

import com.soul.emr.model.entity.masterentity.masterdb.HolidayMasterDB;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HolidayMasterRepository extends JpaRepository<HolidayMasterDB, Long>
{

}