package org.base;


import org.entities.CustomUserDetails;
import org.entities.ROLE;
import com.nimbusds.jose.JOSEException;
import org.services.CustomUserDetailsService;
import org.services.ROLEService;
import org.utilities.jwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.time.Duration;
import java.util.*;

@Component
public class SpringSecurityConfigUtil {
    @Autowired
    private CustomUserDetailsService service;

    @Autowired
    private jwtService aes_service;

    @Autowired
    private ROLEService roleService;



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Bean
    public AuthenticationProvider getAuthenticationProvider(PasswordEncoder encoder) throws JOSEException, ParseException {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        CustomUserDetails adminDetail = new CustomUserDetails();
        adminDetail.setUsername("Admin");
        adminDetail.setPassword(encoder.encode("Admin@123"));
        adminDetail.setEmail("websitemaster591@gmail.com");
        adminDetail.setValid(true);
        List<ROLE> allAllRoles = roleService.getRelevantRoles(Set.of("ROLE_ADMIN","ROLE_MANAGER"));
        adminDetail.setRoles(allAllRoles);
        service.addUser(adminDetail);
        provider.setUserDetailsService(service);
        provider.setPasswordEncoder(encoder);
        Map<String,Object> allDetails = new HashMap<>();
        allDetails.put("username", adminDetail.getUsername());
        allDetails.put("password", "Admin@123");

        System.out.println("Bearer "+aes_service.encrypt(allDetails));
        //System.out.println("DaoAuthentication "+provider.hashCode());
        return provider;
    }

//    mail:
//    host: smtp.gmail.com
//    port: 587
//    username: websitemaster591@gmail.com
//    password: ptzkgnkojglqzmgj
//    properties:
//    smtp:
//    socketFactory:
//    port: 465
//    class: javax.net.ssl.SSLSocketFactory
//    auth: true
//    starttls:
//    enable: true

    @Value("${spring.mail.username}")
    private String adminEmail;

    @Value("${spring.mail.port}")
    private Integer adminPort;

    @Value("${spring.mail.password}")
    private String adminPassword;

    @Value("${mail.store.protocol}")
    private String mailStoreProtocol;

    @Value("${mail.imap.host}")
    private String mailImapsHost;

    @Value("${mail.imap.port}")
    private Integer mailImapsPort;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(adminPort);
        mailSender.setUsername(adminEmail);
        mailSender.setPassword(adminPassword);
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    @Bean
    @Order(2)
    public Properties getReceivedProperties() {
        Properties props = new Properties();
        props.put("mail.store.protocol", "");
        props.put("mail.imaps.host", mailImapsHost);
        props.put("mail.imaps.port", mailImapsPort);
        return props;
    }


    @Bean
    public RestTemplate restTemplate() {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        return restTemplateBuilder.readTimeout(Duration.ofSeconds(30))
                .connectTimeout(Duration.ofSeconds(30))
                .build();
    }


}
