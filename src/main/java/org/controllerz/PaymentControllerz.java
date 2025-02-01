package org.controllerz;


import com.fasterxml.jackson.databind.JsonNode;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.services.PaypalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/v1/payment")
@CrossOrigin(origins = "http://localhost:5100", allowedHeaders = {"Authorization", "Content-Type"})
public class PaymentControllerz {
    @Autowired
    private PaypalService paypalService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${server.address}")
    private String serverAddress;

    @Value("${server.port}")
    private String serverPort;

    @GetMapping(path = "/")
    public String home() {
        return "index";
    }

    @PostMapping(path="/pay")
    public ResponseEntity<String> createPayment(@RequestBody(required = true) com.controllerz.PaymentDetails paymentDetails) {
        String cancelURL = "http://localhost:5100/v1/payment/cancel";
        String successURL = "http://localhost:5100/v1/payment/success";

        System.out.println(cancelURL);
        System.out.println(successURL);
        try {
            Payment payment = paypalService.createPayment(
                    paymentDetails.total(), paymentDetails.currency(), paymentDetails.method(),
                    paymentDetails.intent(), paymentDetails.description(), cancelURL,
                    successURL
            );
            Optional<Links> getLink = payment.getLinks().stream().filter(link->link.getRel().equals("approval_url"))
                    .findFirst();
            if(getLink.isPresent()) {
                return ResponseEntity.ok(getLink.get().getHref());
            } else {
                return ResponseEntity.badRequest().body("Link not found");
            }
        } catch (PayPalRESTException e) {
            //throw new RuntimeException(e);
            return ResponseEntity.status(500).body(e.getMessage());
        }

    }

    @GetMapping(path="/success")
    public ResponseEntity<String> getPaymentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId
    ) {
        try{
            Payment payment = paypalService.executePayment(paymentId,payerId);
            System.out.println(payment.toJSON());
            if(payment.getState().equals("approved")) {
                return ResponseEntity.ok().body("Payment successful");
            } else {
                return ResponseEntity.badRequest().body("Payment failed");
            }
        } catch (PayPalRESTException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }

    }

    @GetMapping(path="/cancel")
    public ResponseEntity<String> getPaymentCancellation(
    ) {

        return ResponseEntity.badRequest().body("Payment cancelled");
    }





}
