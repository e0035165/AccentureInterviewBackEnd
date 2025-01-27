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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

    @Value("${login-message}")
    private String loginMessageFilePath;

    @Autowired
    @Qualifier("getReceivedProperties")
    private Properties mailProperties;

    @Value("${front-end-link}")
    private String frontEndLink;

    private String messageBlock = "Hello Mr.name,\n" +
            "\n" +
            "Your account has been successfully been created. Attached is the token to be used to activate your account.\n" +
            "Please go to this link for activation: "+frontEndLink+
            "\n" +
            "Token: jwt-token\n" +
            "\n" +
            "Thanks,\n" +
            "Mr.name \n";

    public void sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    public void sendLoginMessage(CustomUserDetails customUserDetails, String token) throws IOException {
        String to = customUserDetails.getEmail();
        String subject = "Account Activation";
        String mess = messageBlock.replaceAll(".name", "."+customUserDetails.getUsername())
                        .replaceAll("jwt-token",token);
        System.out.println(mess);
        sendSimpleMail(to, subject, mess);
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
