package org.base;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.paypal.base.rest.APIContext;

@Configuration
public class PaypalConfig {
    @Value("${paypal.client-id}")
    private String PAYPAL_CLIENT_ID;

    @Value("${paypal.client-secret}")
    private String PAYPAL_CLIENT_SECRET;

    @Value("${paypal.mode}")
    private String PAYPAL_MODE;

    @Bean
    public APIContext apiContext() {
        return new APIContext(PAYPAL_CLIENT_ID, PAYPAL_CLIENT_SECRET, PAYPAL_MODE);
    }


}
