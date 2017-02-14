package com.grudus.examshelper.emails;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:email.properties")
public class EmailProperties {

    final String hostName;
    final int port;
    final String username;
    final String password;
    final String message;
    final String messageSubject;
    final boolean startTtlsEnabled;
    final boolean withAuth;

    public EmailProperties(
            @Value("${mail.smtp.host}") String hostName,
            @Value("${mail.smtp.port}") int port,
            @Value("${mail.user}") String username,
            @Value("${mail.password}") String password,
            @Value("${mail.message.content}") String message,
            @Value("${mail.message.subject}") String messageSubject,
            @Value("${mail.smtp.auth}") boolean withAuth,
            @Value("${mail.smtp.starttls.enable}") boolean startTtlsEnabled) {

        this.hostName = hostName;
        this.port = port;
        this.username = username;
        this.password = password;
        this.message = message;
        this.messageSubject = messageSubject;
        this.startTtlsEnabled = startTtlsEnabled;
        this.withAuth = withAuth;
    }
}
