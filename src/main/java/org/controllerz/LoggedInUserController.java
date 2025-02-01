package org.controllerz;


import org.entities.CustomUserDetails;
import org.entities.SubscriptionPlans;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.services.CustomUserDetailsService;
import org.services.EmailService;
import org.services.SubscriptionPlansService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path="/v1/personalAccount")
@CrossOrigin(origins = "http://localhost:5100", allowedHeaders = {"Authorization", "Content-Type"})
public class LoggedInUserController {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SubscriptionPlansService subscriptionPlansService;

    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private EmailService emailService;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @GetMapping(path = "/getSubscriptions")
    public ResponseEntity<List<SubscriptionPlans>> getAllSubscriptionPlans() {
        List<SubscriptionPlans>getAllPlans = subscriptionPlansService.getAllSubscriptionPlans();
        return new ResponseEntity<>(getAllPlans, HttpStatus.OK);
    }



    @PostMapping(path = "/activation")
    public ResponseEntity<String> activation(@RequestBody Map<String,Object> responseBody) {
        CustomUserDetails details = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        details.setValid(true);
        return ResponseEntity.ok("Successfully activated. You may use your account credentials to login");
    }




    @PutMapping(path = "/{username}/addToCart")
    public ResponseEntity<String> subscriptionAddition(@PathVariable(required = true) String username,
                                                       @RequestBody(required = true) Map<String,Object> body) {
        CustomUserDetails details = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        JsonNode node = objectMapper.valueToTree(body);
        ArrayNode arrayNode = (ArrayNode) node;
        for(int i = 0; i < arrayNode.size(); i++) {
            JsonNode subNode = arrayNode.get(i);
            if(!node.isTextual()) {
                return ResponseEntity.badRequest().body("Invalid subscription Item");
            } else {
                SubscriptionPlans subscription = subscriptionPlansService.getSubscriptionPlanByName(node.asText());
                if(subscription == null) {
                    return ResponseEntity.badRequest().body("Invalid subscription plan");
                } else {
                    subscription.addUser(details);
                }
            }
        }
        return ResponseEntity.status(204).body("Subscription successfully added");
    }






}
