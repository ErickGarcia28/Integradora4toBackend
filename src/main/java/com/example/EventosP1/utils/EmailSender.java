package com.example.EventosP1.utils;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailSender {
    private static final Logger logger = LoggerFactory.getLogger(EmailSender.class);

    private final JavaMailSender mailSender;

    @Autowired
    public EmailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(text, true);
            helper.setTo(to);
            helper.setSubject(subject);
            mailSender.send(mimeMessage);
            logger.info("Correo enviado a {}", to);
        } catch (Exception e) {
            logger.error("Error al enviar el correo: ", e);
        }
    }
}
