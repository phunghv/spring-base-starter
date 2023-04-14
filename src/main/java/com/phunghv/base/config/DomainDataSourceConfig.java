package com.phunghv.base.config;


import com.phunghv.base.persistent.domain.BaseEntity;
import java.util.Properties;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
@Slf4j
@RequiredArgsConstructor
@EnableJpaRepositories(basePackages = {"com.phunghv.base.persistent.domain"},
        entityManagerFactoryRef = "domainEntityManagerFactory",
        transactionManagerRef = "domainTransactionManager")
public class DomainDataSourceConfig {

    @Autowired
    private Environment env;
    private final DBConfigDataFactory dbConfigDataFactory;

    @Bean(name = "domainDataSource")
    public DataSource domainDataSource() {
        RoutingDataSource dataSource = new RoutingDataSource(dbConfigDataFactory);
        dataSource.initDataSources();
        return dataSource;
    }

    @Bean(name = "domainTransactionManager")
    public DataSourceTransactionManager domainTransactionManager(@Qualifier("domainDataSource") DataSource dataSource) {
        DataSourceTransactionManager txManager = new DataSourceTransactionManager();
        txManager.setDataSource(dataSource);
        return txManager;
    }

    @Autowired
    @Bean(name = "domainEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean domainEntityManagerFactoryBean(@Qualifier("domainDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        entityManagerFactoryBean.setPackagesToScan(BaseEntity.class.getPackageName());
        entityManagerFactoryBean.setJpaProperties(this.additionalProperties());
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
        return entityManagerFactoryBean;
    }

    private Properties additionalProperties() {
        return new Properties() {
            {
                setProperty("hibernate.hbm2ddl.auto", env.getProperty("database.hibernate.schema_update", "update"));
                setProperty("hibernate.dialect", env.getProperty("database.hibernate.dialect", MySQL5Dialect.class.getName()));
                setProperty("hibernate.show_sql", env.getProperty("database.hibernate.show_sql", Boolean.TRUE.toString()));
                setProperty("hibernate.format_sql", env.getProperty("database.hibernate.format_sql", Boolean.TRUE.toString()));
            }
        };
    }
}
