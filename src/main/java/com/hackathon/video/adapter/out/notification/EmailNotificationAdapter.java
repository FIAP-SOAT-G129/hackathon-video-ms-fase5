package com.hackathon.video.adapter.out.notification;

import com.hackathon.video.domain.repository.NotificationPort;
import com.hackathon.video.exception.MessagingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Primary
@Component
@RequiredArgsConstructor
public class EmailNotificationAdapter implements NotificationPort {

    private static final Logger log = LoggerFactory.getLogger(EmailNotificationAdapter.class);
    private final JavaMailSender mailSender;

    @Override
    public void send(String email, String message) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(email);
            mailMessage.setSubject("Video Processing Update");
            mailMessage.setText(message);
            mailSender.send(mailMessage);
            
            log.info("Email sent to user {}: {}", email, message);
        } catch (Exception e) {
            log.error("Failed to send email {}", email, e);
            throw new MessagingException("Failed to send email notification");
        }
    }
}
