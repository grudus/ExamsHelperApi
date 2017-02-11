package com.grudus.examshelper.configuration;


import org.jooq.DSLContext;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

import static org.jooq.SQLDialect.MYSQL;

@Configuration
public class ExamsHelperContext {


    @Bean
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public DSLContext dslContext(DataSource primaryDataSource) {
        return new DefaultDSLContext(primaryDataSource, MYSQL);
    }

}
