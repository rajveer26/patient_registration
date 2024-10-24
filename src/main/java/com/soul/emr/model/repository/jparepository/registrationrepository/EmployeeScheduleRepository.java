package com.soul.emr.model.repository.jparepository.registrationrepository;

import com.soul.emr.model.entity.modelemployee.registrationdb.EmployeeScheduleDB;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeScheduleRepository extends JpaRepository<EmployeeScheduleDB, Long> {

}
