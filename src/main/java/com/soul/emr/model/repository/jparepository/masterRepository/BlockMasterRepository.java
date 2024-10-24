package com.soul.emr.model.repository.jparepository.masterRepository;

import com.soul.emr.model.entity.masterentity.masterdb.BlockDetailsMasterDB;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockMasterRepository extends JpaRepository<BlockDetailsMasterDB, Long> {
}
