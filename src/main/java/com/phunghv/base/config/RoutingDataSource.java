package com.phunghv.base.config;

import com.phunghv.base.constant.RequestHeaderConstants;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@AllArgsConstructor
public class RoutingDataSource extends AbstractRoutingDataSource {

    private final DBConfigDataFactory dbConfigDataFactory;

    @Override
    protected Object determineCurrentLookupKey() {
        var dbKey = dbConfigDataFactory.getDefaultDB().getDbKey();
        if (RequestContextHolder.getRequestAttributes() != null) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .getRequest();
            dbKey = (String) request.getAttribute(RequestHeaderConstants.DATABASE_KEY);
        }
        return dbKey;
    }

    @Override
    protected DataSource determineTargetDataSource() {
        Assert.notNull(this.getResolvedDataSources(), "DataSource router not initialized");
        Object lookupKey = this.determineCurrentLookupKey();
        DataSource dataSource = null;
        if (this.getResolvedDataSources().containsKey(lookupKey)) {
            dataSource = this.getResolvedDataSources().get(lookupKey);
        } else {
            dataSource = dbConfigDataFactory.initDatasource((String) lookupKey);
            if (dataSource != null) {
                updateTargetDataSources(lookupKey, dataSource);
            }
        }

        if (dataSource == null) {
            dataSource = this.getResolvedDefaultDataSource();
        }
        if (dataSource == null) {
            throw new IllegalStateException("Cannot determine target DataSource for lookup key [" + lookupKey + "]");
        } else {
            return dataSource;
        }
    }

    private synchronized void updateTargetDataSources(Object lookupKey, DataSource dataSource) {
        Map<Object, Object> dsMap = new HashMap<Object, Object>(this.getResolvedDataSources());
        dsMap.put(lookupKey, dataSource);
        this.setTargetDataSources(dsMap);
        this.afterPropertiesSet();
    }

    public void initDataSources() {
        Map<Object, Object> datasourceMap = new HashMap<Object, Object>();
        var allDb = dbConfigDataFactory.getAllDatabases();
        for (var entry : allDb.entrySet()) {
            var datasource = dbConfigDataFactory.initDatasource(entry.getKey());
            datasourceMap.put(entry.getKey(), datasource);
            if (entry.getValue().getIsDefault()) {
                this.setDefaultTargetDataSource(datasource);
            }
        }
        this.setTargetDataSources(datasourceMap);
    }
}
