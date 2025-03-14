package com.rainlf.mgttbe.infra.db.repository;

import com.rainlf.mgttbe.infra.db.dataobj.MgttConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MgttConfigRepository extends JpaRepository<MgttConfig, Integer> {

    MgttConfig findByKey(String key);
}
