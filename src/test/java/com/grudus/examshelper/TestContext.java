package com.grudus.examshelper;

import com.grudus.examshelper.configuration.ExamsHelperContext;
import com.grudus.examshelper.emails.EmailProperties;
import com.grudus.examshelper.emails.EmailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;

import javax.mail.MessagingException;
import javax.sql.DataSource;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

@Configuration
@Import(ExamsHelperContext.class)
@PropertySource("classpath:/test.properties")
@ComponentScan("com.grudus.examshelper")
public class TestContext {

    @Bean
    @Primary
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
    public EmailSender emailSender() throws MessagingException {
        EmailSender sender = mock(EmailSender.class);
        doNothing().when(sender).send(anyString(), anyString());
        return sender;
    }

    @Bean
    @Primary
    public EmailProperties emailProperties() {
        return new EmailProperties(null, 0, null, null, null, null, false, false);
    }
}
