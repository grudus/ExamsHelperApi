package com.grudus.examshelper.emails;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

import static java.lang.String.format;

@Component
public class EmailSender {
    private static final Logger logger = LoggerFactory.getLogger(EmailSender.class);

    public static final String HOST_KEY = "mail.smtp.host";
    public static final String STARTTLS_ENABLE_KEY = "mail.smtp.starttls.enable";
    public static final String AUTH_KEY = "mail.smtp.auth";
    public static final String PORT_KEY = "mail.smtp.port";

    private final EmailProperties emailProperties;

    private final Properties properties;
    private final Session session;
    private MimeMessage mimeMessage;

    @Autowired
    public EmailSender(EmailProperties emailProperties) {
        this.emailProperties = emailProperties;
        properties = new Properties();
        properties.put(HOST_KEY, emailProperties.hostName);
        properties.put(STARTTLS_ENABLE_KEY, emailProperties.startTtlsEnabled);
        properties.put(AUTH_KEY, emailProperties.withAuth);
        properties.put(PORT_KEY, emailProperties.port);
        session = Session.getDefaultInstance(properties, new SMTPAuthenticator());
        logger.debug("Created session for {}", properties);
    }

    private void send(String message, String emailRecipient) throws MessagingException {
        mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(new InternetAddress(emailProperties.username));
        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(emailRecipient));
        mimeMessage.setSubject(emailProperties.messageSubject);
        mimeMessage.setText(message);

        Transport.send(mimeMessage);
        logger.info("Successfully sent message {} to the {}", message, emailRecipient);
    }

    public void sendConfirmationRegister(String username, String recipient, String token, String url) throws MessagingException {
        send(format(emailProperties.message, username, url, token), recipient);
    }


    private class SMTPAuthenticator extends Authenticator {

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            String username = emailProperties.username;
            String password = emailProperties.password;
            return new PasswordAuthentication(username, password);
        }
    }
}
