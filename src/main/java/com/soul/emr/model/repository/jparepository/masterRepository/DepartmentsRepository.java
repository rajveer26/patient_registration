package com.soul.emr.model.repository.jparepository.masterRepository;

import com.soul.emr.model.entity.masterentity.masterdb.DepartmentMasterDB;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentsRepository extends JpaRepository<DepartmentMasterDB, Long>
{

}
