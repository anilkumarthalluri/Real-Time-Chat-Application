package com.chat.Real_Time.Chat.Application.service.impl;

import com.chat.Real_Time.Chat.Application.service.EmailService;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Value("${SENDGRID_API_KEY}")
    private String sendGridApiKey;

    @Value("${SENDGRID_FROM_EMAIL}")
    private String fromEmail;

    @Value("${FRONTEND_URL}")
    private String frontendUrl;

    @Override
    public void sendPasswordResetEmail(String to, String token) {
        Email from = new Email(fromEmail, "Real-Time Chat App");
        String subject = "Password Reset Request";
        Email toEmail = new Email(to);

        try {
            ClassPathResource resource = new ClassPathResource("password-reset-template.html");
            String htmlTemplate = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            String resetLink = frontendUrl + "/reset-password.html?token=" + token;
            String finalHtml = htmlTemplate.replace("{{RESET_LINK}}", resetLink);

            Content content = new Content("text/html", finalHtml);
            Mail mail = new Mail(from, subject, toEmail, content);
            SendGrid sg = new SendGrid(sendGridApiKey);
            Request request = new Request();

            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            if (response.getStatusCode() < 200 || response.getStatusCode() >= 300) {
                logger.error("SendGrid returned a non-successful status code: {}", response.getStatusCode());
                logger.error("Response Body: {}", response.getBody());
                throw new IOException("SendGrid request failed with status code: " + response.getStatusCode());
            }

            logger.info("Password reset email sent successfully to {}", to);

        } catch (IOException ex) {
            logger.error("Error sending password reset email: {}", ex.getMessage());
            throw new RuntimeException("Failed to send password reset email.", ex);
        }
    }
}
