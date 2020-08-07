package com.devolek.blogengine.main.service.impl;

import com.devolek.blogengine.main.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;
    private final SimpleMailMessage template;
    private final String projectName;

    @Autowired
    public EmailServiceImpl(
            @Qualifier("getJavaMailSender") JavaMailSender emailSender,
            SimpleMailMessage template,
            @Value("${project.name}") String projectName
    ) {
        this.emailSender = emailSender;
        this.template = template;
        this.projectName = projectName;
    }

    @Override
    public void sendSimpleMessage(String to, String text, String subject) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setText(text);
            message.setSubject(subject);

            emailSender.send(message);
        } catch (MailException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void sendSimpleMessageUsingTemplate(String to, String... templateModel) {
        String text = String.format(template.getText(), templateModel);
        sendSimpleMessage(to, text, projectName);
    }

    @Override
    public void sendPasswordRecovery(String to, String name, String link) {
        String text = String.format(template.getText(), name, link);
        sendSimpleMessage(to, text, "Запрос восстановления пароля " + projectName);
    }

    @Override
    public void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
            helper.addAttachment("Invoice", file);

            message.setSubject(projectName);
            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
