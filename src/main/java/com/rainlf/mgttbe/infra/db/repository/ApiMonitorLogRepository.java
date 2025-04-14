package com.rainlf.mgttbe.infra.db.repository;

import com.rainlf.mgttbe.infra.db.dataobj.ApiMonitorLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiMonitorLogRepository extends JpaRepository<ApiMonitorLog, Long> {
}