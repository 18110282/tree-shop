package com.treeshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.Properties;

@Service
public class MailService {
    private static final String CONTENT_TYPE_TEXT_HTML = "text/html;charset=\"utf-8\"";

    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private String port;
    @Value("${spring.mail.username}")
    private String email;
    @Value("${spring.mail.password}")
    private String password;

    @Autowired
    ThymeleafService thymeleafService;
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMail(HttpServletRequest request) {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email, password);
                    }
                });
//        MimeMessage message = javaMailSender.createMimeMessage();
        Message message = new MimeMessage(session);
        try {
            message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress("hieuho10022000@gmail.com")});

            message.setFrom(new InternetAddress("treeshop49@gmail.com"));
            message.setSubject("Spring-email-with-thymeleaf subject");
            message.setContent(thymeleafService.getContent(request), CONTENT_TYPE_TEXT_HTML);
            Transport.send(message);
//            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
