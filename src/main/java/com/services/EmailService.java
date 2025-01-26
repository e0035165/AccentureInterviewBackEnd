package com.services;


import com.base.SpringSecurityConfigUtil;
import com.entities.CustomUserDetails;
import com.utilities.Message;
import jakarta.mail.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${spring.mail.password}")
    private String password;

    @Autowired
    @Qualifier("getReceivedProperties")
    private Properties mailProperties;

    public void sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    public void sendLoginMessage(CustomUserDetails customUserDetails) {

    }

    public List<jakarta.mail.Message> getInboxMessages() {
       Session session = Session.getDefaultInstance(mailProperties, null);
        try{
            Store store = session.getStore();
            store.connect(from,password);
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            jakarta.mail.Message[] messages = inbox.getMessages();
            return List.of(messages);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


}
