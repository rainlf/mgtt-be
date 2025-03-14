package com.rainlf.mgttbe.infra.db.manager;

import com.rainlf.mgttbe.infra.db.dataobj.MgttLog;
import com.rainlf.mgttbe.infra.db.repository.MgttLogRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MgttLogManager {
    @Getter
    private static MgttLogManager instance;

    @PostConstruct
    public void init() {
        instance = this;
    }

    @Autowired
    private MgttLogRepository mgttLogRepository;

    public void saveLog(MgttLog log) {
        mgttLogRepository.save(log);
    }
}
