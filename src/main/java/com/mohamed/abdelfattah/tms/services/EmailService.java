package com.mohamed.abdelfattah.tms.services;

import com.mohamed.abdelfattah.tms.dto.EmailData;
import com.mohamed.abdelfattah.tms.entities.Task;
import com.mohamed.abdelfattah.tms.entities.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor

public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Async
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setFrom(fromEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    @Async
    public void sendEmail(EmailData emailData) throws MessagingException {

        Context context = new Context();
        context.setVariable("userName", emailData.userName());
        context.setVariable("taskTitle", emailData.taskTitle());
        context.setVariable("taskPriority", emailData.taskPriority());
        context.setVariable("taskDescription", emailData.taskDescription());

        String htmlContent = templateEngine.process("task-assignment-email", context);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper =
                new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");

        helper.setTo(emailData.to());
        helper.setText(htmlContent, true);
        helper.setSubject("New Task Assigned: " + emailData.taskTitle());
        helper.setFrom(fromEmail);

        mailSender.send(message);
    }

    public EmailData createData(User user, Task task) {
        return new EmailData(
                user.getEmail(),
                user.getFullName(),
                task.getTitle(),
                task.getPriority(),
                task.getDescription()
        );
    }
}

