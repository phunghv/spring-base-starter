package com.phunghv.base.config;

import com.phunghv.base.persistent.primary.entity.DatabaseMapping;
import com.phunghv.base.service.DatabaseMappingService;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DBConfigDataFactory {

    @Autowired
    private Environment env;
    private static final Map<String, DatabaseMapping> ALL_DATABASES = new ConcurrentHashMap<>();
    private final DatabaseMappingService databaseMappingService;

    private void init() {
        databaseMappingService.listAllActiveDatabases().forEach(db -> ALL_DATABASES.put(db.getDbKey(), db));
    }

    public Map<String, DatabaseMapping> getAllDatabases() {
        init();
        return ALL_DATABASES;
    }

    public DatabaseMapping getDBInfo(String dbKey) {
        return ALL_DATABASES.get(dbKey);
    }

    public DatabaseMapping getDefaultDB() {
        return ALL_DATABASES.values().stream().filter(DatabaseMapping::getIsDefault).findFirst().orElse(null);
    }


    public DataSource initDatasource(String dbKey) {
        var dbInfo = getDBInfo(dbKey);
        return initDatasource(dbInfo);
    }

    public DataSource initDatasource(DatabaseMapping dbInfo) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        var host = Optional.ofNullable(dbInfo.getHost()).orElse(env.getProperty("spring.datasource.default.host", "localhost"));
        var port = Optional.ofNullable(dbInfo.getPort()).orElse(env.getProperty("spring.datasource.default.port", Integer.class, 3306));
        var username = Optional.ofNullable(dbInfo.getUser()).orElse(env.getProperty("spring.datasource.default.username"));
        var password = Optional.ofNullable(dbInfo.getPassword()).orElse(env.getProperty("spring.datasource.default.password"));
        var url = String.format("jdbc:mysql://%s:%s/%s", host, port, dbInfo.getDbName());
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    public String validateDBKey(String db) {
        if (ALL_DATABASES.containsKey(db)) {
            return db;
        }
        var databaseMapping = databaseMappingService.getDatabaseMapping(db);
        if (databaseMapping == null) {
            return null;
        }
        ALL_DATABASES.put(databaseMapping.getDbKey(), databaseMapping);
        return db;
    }
}
