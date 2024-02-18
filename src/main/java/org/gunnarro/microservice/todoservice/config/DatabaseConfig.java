package org.gunnarro.microservice.todoservice.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.envers.repository.config.EnableEnversRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Slf4j
@EnableEnversRepositories(basePackages = "org.gunnarro.microservice.todoservice")
//@EnableJpaRepositories(repositoryFactoryBeanClass = EnableEnversRepositories.class, basePackages = "org.gunnarro.microservice.todoservice.repository")
//@EnableJpaRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
@EnableTransactionManagement
@Configuration
public class DatabaseConfig {
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.microservice")
    public DataSourceProperties microserviceDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "microserviceDataSource")
    @Primary
    @ConfigurationProperties("spring.datasource.microservice.configuration")
    public DataSource microserviceDataSource() {
        return microserviceDataSourceProperties()
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "microserviceJdbcTemplate")
    public JdbcTemplate microserviceJdbcTemplate(@Qualifier("microserviceDataSource") DataSource microserviceDataSource) {
        log.debug("microserviceJdbcTemplate dataSource:{}", microserviceDataSource);
        return new JdbcTemplate(microserviceDataSource);
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory);
        return txManager;
    }
}
