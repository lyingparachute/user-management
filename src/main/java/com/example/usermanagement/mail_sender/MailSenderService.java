package com.example.usermanagement.mail_sender;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailSenderService {

    private static final String APP_NAME = "UserManagement";
    private final JavaMailSender javaMailSender;
    private final ITemplateEngine thymeleafTemplateEngine;

    @Value("${spring.mail.username}")
    private String sender;

    @Async
    public void sendEmail(final String userEmail,
                          final String subject,
                          final String templateLocation,
                          final Map<String, Object> templateModel) {
        final String htmlBody = createEmailBody(templateLocation, templateModel);
        try {
            sendHtmlMessage(userEmail, subject, htmlBody);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to send email.", e);
        }
    }

    private String createEmailBody(final String templateLocation,
                                   final Map<String, Object> templateModel) {
        final Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);
        return thymeleafTemplateEngine.process(templateLocation, thymeleafContext);
    }

    private void sendHtmlMessage(final String to,
                                 final String subject,
                                 final String htmlBody)
        throws MessagingException, UnsupportedEncodingException {
        final MimeMessage message = javaMailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(sender, APP_NAME);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        javaMailSender.send(message);
    }

    public static String getAppUrl(final HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
