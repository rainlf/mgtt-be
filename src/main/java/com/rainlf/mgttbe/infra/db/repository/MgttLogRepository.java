package com.rainlf.mgttbe.infra.db.repository;

import com.rainlf.mgttbe.infra.db.dataobj.MgttLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MgttLogRepository extends JpaRepository<MgttLog, Integer> {

}
