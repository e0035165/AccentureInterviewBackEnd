package org.controllerz;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.entities.Contacts;
import org.entities.CustomUserDetails;
import org.entities.ROLE;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.services.ContactsService;
import org.services.CustomUserDetailsService;
import org.services.ROLEService;
import org.utilities.jwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private jwtService jwtService;

    @Autowired
    private EmailService emailService;


    @GetMapping(path = "/contactUS")
    public ResponseEntity<List<Contacts>> getContacts() {
        List<Contacts> getContacts = contactsService.getAllContacts();
        return new ResponseEntity<>(getContacts, HttpStatus.OK);
    }

    @PostMapping(path="/login")
    public ResponseEntity<JsonNode> login(@RequestBody AuthRequest authRequest) {
        ObjectNode node = (new ObjectMapper()).createObjectNode();
        CustomUserDetails details = (CustomUserDetails) customUserDetailsService.loadUserByUsername(authRequest.username());
        if(details==null) {
            node.put("message", "Invalid username or password");
            return ResponseEntity.badRequest().body(node);
        }
        if(details.isEnabled()) {
            String bearer = "Bearer "+jwtService.encrypt(authRequest);
            node.put("bearer", bearer);
            return ResponseEntity.ok(node);
        } else {
            node.put("message", "Disabled");
            return ResponseEntity.badRequest().body(node);
        }
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
        String tolken = "Bearer "+e_service.encrypt(authRequest);
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("token", tolken);
        emailService.sendLoginMessage(userDetails, tolken);
        return ResponseEntity.status(201).body(node);
    }




}
