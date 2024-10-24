package com.soul.emr.model.repository.jparepository.businessgrouprepository.organizationrepository;

import com.soul.emr.model.entity.modelbusinessgroup.organization.organizationdb.OrganizationDB;
import org.springframework.data.jpa.repository.JpaRepository;
public interface OrganizationRepo extends JpaRepository<OrganizationDB, Long>
{
}
