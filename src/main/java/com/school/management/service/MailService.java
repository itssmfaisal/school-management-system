package com.school.management.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MailService {
    private final JavaMailSender sender;

    public MailService(JavaMailSender sender) {
        this.sender = sender;
    }

    public void sendToMany(List<String> recipients, String subject, String body) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(recipients.toArray(new String[0]));
        msg.setSubject(subject);
        msg.setText(body);
        sender.send(msg);
    }
}
