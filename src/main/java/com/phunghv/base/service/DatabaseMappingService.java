package com.phunghv.base.service;

import com.phunghv.base.persistent.primary.entity.DatabaseMapping;
import com.phunghv.base.persistent.primary.repo.DatabaseMappingRepo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DatabaseMappingService {

    private final DatabaseMappingRepo databaseMappingRepo;

    public List<DatabaseMapping> listAllActiveDatabases() {
        return databaseMappingRepo.findAllByActive(true);
    }


    public DatabaseMapping getDatabaseMapping(String dbKey) {
        return databaseMappingRepo.findFirstByDbKeyAndActive(dbKey, true);
    }
}
