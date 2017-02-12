package com.grudus.examshelper.emails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

import static java.lang.String.format;

@Component
public class EmailSender {

    private final EmailProperties emailProperties;

    private Properties properties;
    private Session session;
    private MimeMessage mimeMessage;

    @Autowired
    public EmailSender(EmailProperties emailProperties) {
        this.emailProperties = emailProperties;
        properties = new Properties();
        properties.put("mail.smtp.host", emailProperties.getHostName());
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", emailProperties.getPort());
        session = Session.getDefaultInstance(properties, new SMTPAuthenticator());

    }

    private void send(String message, String emailRecipient) throws MessagingException {
        mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(new InternetAddress(emailProperties.getUsername()));
        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(emailRecipient));
        mimeMessage.setSubject(emailProperties.getMessageSubject());
        mimeMessage.setText(message);

        Transport.send(mimeMessage);
        System.out.println("Sent message successfully...");
    }

    public void sendConfirmationRegister(String username, String recipient, String token, String url) throws MessagingException {
        send(format(emailProperties.getMessage(), username, url, token), recipient);
    }


    private class SMTPAuthenticator extends Authenticator {

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            String username = emailProperties.getUsername();
            String password = emailProperties.getPassword();
            return new PasswordAuthentication(username, password);
        }
    }
}
