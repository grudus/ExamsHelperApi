package com.grudus.examshelper.configuration;


import org.apache.commons.validator.routines.EmailValidator;
import org.jooq.DSLContext;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public EmailValidator emailValidator() {
        return EmailValidator.getInstance();
    }

}
