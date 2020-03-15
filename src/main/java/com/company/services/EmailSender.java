package com.company.services;

import com.company.models.User;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class EmailSender {
    final String username = "sh.il.almaz@gmail.com";
    final String password = "jdwahzedxdyhtofx";

    public EmailSender() {
    }

    public void sendVerificationEmail(User user, String link) {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
            cfg.setDirectoryForTemplateLoading(new File("../webapps/springservice/WEB-INF/templates"));
            cfg.setDefaultEncoding("UTF-8");
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("sh.il.almaz@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(user.getEmail())
            );
            Map<String, String> root = new HashMap<>();
            root.put("login", user.getLogin());
            root.put("link", link);
            Template template = cfg.getTemplate("verification_email_message.ftl");
            message.setSubject("Email verification.");
            message.setContent(FreeMarkerTemplateUtils.processTemplateIntoString(template, root), "text/html");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException | IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

}
