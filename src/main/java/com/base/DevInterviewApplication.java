package com.base;


import com.entities.Contacts;
import com.entities.ROLE;
import com.entities.SubscriptionPlans;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.services.ContactsService;
import com.services.ROLEService;
import com.services.SubscriptionPlansService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@ComponentScan(basePackages= {"com.utilities","com.entities","com.repositories","com.services","com.filter",
        "com.controllerz","com.base"})
@EnableJpaRepositories(basePackages= {"com.repositories"})
@EntityScan(basePackages= {"com.entities"})
public class DevInterviewApplication implements CommandLineRunner {
    private static Logger LOG = LoggerFactory
            .getLogger(DevInterviewApplication.class);

    @Autowired
    private ROLEService roleService;

    @Autowired
    private ContactConfig contactConfig;


    @Autowired
    private SubscriptionPlansService subscriptionPlansService;

    @Autowired
    private ContactsService contactsService;

    @Value("${contacts}")
    public String filepath;

    private ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);

    public List<Contacts>getAllContacts() throws IOException {
        List<Contacts>getAllContacts=new ArrayList<>();
        File file = new File(filepath);
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuffer sb = new StringBuffer();
        String line = null;
        while((line=br.readLine())!=null)
            sb.append(line);

        ArrayNode Anodes  = (ArrayNode) objectMapper.readTree(sb.toString());
        for(int i=0;i<Anodes.size();i++){
            getAllContacts.add(
                new Contacts(Anodes.get(i).get("name").asText(),
                    Anodes.get(i).get("email").asText(),
                    Anodes.get(i).get("no").asText())
            );
        }
        return getAllContacts;
    }


    public static void main(String[] args) {
        SpringApplication.run(DevInterviewApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOG.info("Application started");
        List.of("ROLE_USER","ROLE_ADMIN","ROLE_MANAGER").stream()
                .map(role->new ROLE(role)).forEach(role->roleService.add(role));

        Map.of("ONE_MONTH",12.99f,"SIX_MONTH",10.99f,"ONE_YEAR",5.99f)
                .entrySet().stream().map(entry->new SubscriptionPlans(entry.getKey(), entry.getValue()))
                .forEach(subscriptionPlans -> subscriptionPlansService.addSubscriptionPlan(subscriptionPlans));

        List<Contacts>allContacts=getAllContacts();
        contactsService.addAllContacts(allContacts);


    }

}
