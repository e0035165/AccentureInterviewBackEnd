package com.controllerz;

import com.entities.Contacts;
import com.entities.CustomUserDetails;
import com.entities.ROLE;
import com.entities.SubscriptionPlans;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.services.ContactsService;
import com.services.CustomUserDetailsService;
import com.services.ROLEService;
import com.services.SubscriptionPlansService;
import com.utilities.jwtService;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/v1/frontPage")
@CrossOrigin(origins = "http://localhost:5100")
public class FrontPageController {
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ROLEService roleService;

    @Autowired
    private jwtService e_service;

    @Autowired
    private ContactsService contactsService;



    @GetMapping(path = "/contactUS")
    public ResponseEntity<List<Contacts>> getContacts() {
        List<Contacts> getContacts = contactsService.getAllContacts();
        return new ResponseEntity<>(getContacts, HttpStatus.OK);
    }

    @PostMapping(path = "/signup")
    public ResponseEntity<JsonNode> Signup(@RequestBody AuthRequest authRequest) {
        CustomUserDetails userDetails = new CustomUserDetails();
        userDetails.setUsername(authRequest.username());
        userDetails.setPassword(passwordEncoder.encode(authRequest.password()));
        userDetails.setEmail(authRequest.email());
        userDetails.setValid(false);
        ROLE getUserRole = roleService.getByRole("ROLE_USER");
        userDetails.addRole(getUserRole);
        userDetailsService.addUser(userDetails);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(token);
        String tolken = e_service.encrypt(authRequest);
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("token", tolken);
        return ResponseEntity.status(201).body(node);
    }




}
