package com.plintus.sweetstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSender {
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String mailUsername;
    public void send(String emailTo,
                     String emailSubject,
                     String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(mailUsername);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(emailSubject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }
}
