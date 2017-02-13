package com.grudus.examshelper;

import com.grudus.examshelper.configuration.ExamsHelperContext;
import org.jooq.DSLContext;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

import static org.jooq.SQLDialect.MYSQL;

@Configuration
@Import(ExamsHelperContext.class)
@PropertySource("classpath:/test.properties")
@ComponentScan("com.grudus.examshelper")
public class TestContext {

    @Bean
    @ConfigurationProperties
    public DataSource primaryDataSource(@Value("${spring.datasource.driver-class-name}") String driver,
                                        @Value("${spring.datasource.url}") String url,
                                        @Value("${spring.datasource.username}") String username,
                                        @Value("${spring.datasource.password}") String password) {
        return DataSourceBuilder.create()
                .username(username)
                .password(password)
                .url(url)
                .driverClassName(driver)
                .build();
    }

    @Bean
    @Primary
    public DSLContext dslContext(javax.sql.DataSource primaryDataSource) {
        return new DefaultDSLContext(primaryDataSource, MYSQL);
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager(DataSource dataSource) {
        DataSourceTransactionManager manager = new DataSourceTransactionManager(dataSource);
        return manager;
    }

}
