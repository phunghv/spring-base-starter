package com.phunghv.base.config;

import com.phunghv.base.persistent.primary.entity.PrimaryBaseEntity;
import java.util.Properties;
import javax.sql.DataSource;
import org.hibernate.dialect.MySQL5Dialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(basePackages = {"com.phunghv.base.persistent.primary"},
        entityManagerFactoryRef = "primaryEntityManager",
        transactionManagerRef = "primaryTransactionManager")
public class PrimaryDataSourceConfig {

    @Autowired
    private Environment env;

    @Bean(name = "primaryEntityManager")
    @Primary
    public LocalContainerEntityManagerFactoryBean primaryEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(primaryDatasource());
        em.setPersistenceUnitName("primaryEntityManager");
        em.setPackagesToScan(PrimaryBaseEntity.class.getPackageName());
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(this.additionalProperties());
        return em;
    }

    @Primary
    @Bean(name = "primaryDatasource")
    public DataSource primaryDatasource() {
        var dbDatasourceInfo = getPrimaryConfigInfo();
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        var url = String.format("jdbc:mysql://%s:%s/%s", dbDatasourceInfo.getHost(), dbDatasourceInfo.getPort(), dbDatasourceInfo.getDatabaseName());
        dataSource.setUrl(url);
        dataSource.setUsername(dbDatasourceInfo.getUsername());
        dataSource.setPassword(dbDatasourceInfo.getPassword());
        return dataSource;
    }

    private DBConfigData getPrimaryConfigInfo() {
        var config = new DBConfigData();
        config.setHost(env.getProperty("spring.datasource.primary.host", "localhost"));
        config.setPort(env.getProperty("spring.datasource.primary.port", Integer.class, 3306));
        config.setUsername(env.getProperty("spring.datasource.primary.username"));
        config.setPassword(env.getProperty("spring.datasource.primary.password"));
        config.setDatabaseName(env.getProperty("spring.datasource.primary.database-name"));
        return config;
    }

    @Primary
    @Bean(name = "primaryTransactionManager")
    public PlatformTransactionManager primaryTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(primaryEntityManager().getObject());
        return transactionManager;
    }

    private Properties additionalProperties() {
        return new Properties() {
            {  // Hibernate Specific:
                setProperty("hibernate.hbm2ddl.auto", env.getProperty("database.hibernate.schema_update", "update"));
                setProperty("hibernate.dialect", env.getProperty("database.hibernate.dialect", MySQL5Dialect.class.getName()));
                setProperty("hibernate.show_sql", env.getProperty("database.hibernate.show_sql", Boolean.TRUE.toString()));
                setProperty("hibernate.format_sql", env.getProperty("database.hibernate.format_sql", Boolean.TRUE.toString()));
            }
        };
    }
}
