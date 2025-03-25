package com.application;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailService {

    // Load Environment Variables
    private static final Dotenv dotenv = Dotenv.load();

    private final String senderEmailId = dotenv.get("EMAIL_USERNAME");
    private final String senderPassword = dotenv.get("EMAIL_PASSWORD");
    private final Properties properties;

    public EmailService() {
        properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", dotenv.get("EMAIL_HOST"));
        properties.put("mail.smtp.port", dotenv.get("EMAIL_PORT"));
    }

    public void sendEmail(String to, String subject, String content) {
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmailId, senderPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmailId));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);
            System.out.println("Email sent successfully to " + to);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


}
