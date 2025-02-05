package org.services;

import com.paypal.api.payments.*;
import com.paypal.api.payments.Currency;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.annotation.PostConstruct;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
@RequiredArgsConstructor
public class PaypalService {

    @Autowired
    private APIContext apiContext;

    private Invoicer invoicer;





    @Data
    @RequiredArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    private class Invoicer{
        public String given_name;
        public String sur_name;
        public Map<String,String> address;
        public String email;
        public InvoiceAddress invoiceAddress;
    }







    @PostConstruct
    public void init(@Value("${paypal.given_name}") String given_name,
                     @Value("${paypal.sur_name}")String surname,
                     @Value("${paypal.email}") String email)  {
        invoicer = new Invoicer();
        invoicer.given_name = given_name;
        invoicer.sur_name = surname;
        invoicer.email = email;
        invoicer.invoiceAddress = new InvoiceAddress();
        invoicer.invoiceAddress.setPostalCode("760851");
        invoicer.invoiceAddress.setCity("Singapore");
        invoicer.invoiceAddress.setPhone(new Phone().setCountryCode("+65")
                .setNationalNumber("96196995"));
    }

    public Phone getPhone(String phoneNumber, String countryCode) throws PayPalRESTException {
        Phone phone = new Phone();
        phone.setCountryCode(countryCode);
        phone.setNationalNumber(phoneNumber);
        return phone;
    }

    public InvoiceAddress getInvoiceAddress(String postalCode, String city,
                                            String state, String countryCode,
                                            String line1, String line2,
                                            String status) throws PayPalRESTException {
        InvoiceAddress invAddress = new InvoiceAddress();
        invAddress.setPostalCode(postalCode);
        invAddress.setState(state);
        invAddress.setCountryCode(countryCode);
        invAddress.setLine1(line1);
        invAddress.setLine2(line2);
        invAddress.setStatus(status);
        invAddress.setCity(city);
        return invAddress;
    }

    public InvoiceItem getInvoiceItem(String name, Integer qty, Currency currency) {
        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setUnitPrice(currency);
        invoiceItem.setQuantity(qty);
        invoiceItem.setName(name);
        return invoiceItem;
    }

    public Invoice createInvoice(@NonNull Date invoiceDate,
                                 @NonNull String currency_code,
                                 Phone phone,
                                 @NonNull Integer dueTimeInDays,
                                 List<InvoiceItem>items,
                                 String username,
                                 String surname,
                                 InvoiceAddress customer_address
    ) throws PayPalRESTException {
        Billing billing = new Billing();
        Currency currency = new Currency();
        currency.setCurrency(currency_code);
        InstallmentOption installmentOption = new InstallmentOption();
        installmentOption.setTerm(dueTimeInDays);
        installmentOption.setMonthlyPayment(currency);
        installmentOption.setDiscountAmount(currency);
        installmentOption.setDiscountPercentage(new Percentage());
        billing.setSelectedInstallmentOption(installmentOption);
        Invoice invoice = new Invoice();
        String inv_id = username+"_"+(new Date(System.currentTimeMillis())).toString();
        invoice.setId(inv_id);
        invoice.setInvoiceDate(invoiceDate.toString());
        invoice.setItems(items);
        BillingInfo billingInfo = new BillingInfo();
        billingInfo.setEmail(invoicer.email);
        billingInfo.setAdditionalInfo("Invoice sending");
        billingInfo.setFirstName(invoicer.given_name);
        billingInfo.setLastName(invoicer.sur_name);
        billingInfo.setAddress(this.invoicer.invoiceAddress);
        billingInfo.setBusinessName("The Melaka Times");
        billingInfo.setLanguage("English");
        ShippingInfo shippingInfo = new ShippingInfo();
        shippingInfo.setFirstName(username);
        shippingInfo.setLastName(surname);
        shippingInfo.setAddress(customer_address);
        shippingInfo.setPhone(phone);
        shippingInfo.setBusinessName("Personal");
        invoice.setShippingInfo(shippingInfo);
        ShippingCost shippingCost = new ShippingCost();
        shippingCost.setAmount(currency);
        shippingCost.setTax(new Tax("Govt Tax", 5.75f));
        invoice.setShippingCost(shippingCost);

        return invoice.create(apiContext);
    }



    public Payment createPayment(
            @NonNull Double totalAmount,
            @NonNull String currency,
            String method,
            String intent,
            String description,
            String cancelUrl,
            String successURL
    ) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format(Locale.forLanguageTag(currency),"%.2f", totalAmount));
        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);
        List<Transaction> allTransactions = List.of(transaction);
        Payer payer = new Payer();
        payer.setPaymentMethod(method);
        Payment payment = new Payment();
        payment.setIntent(intent);
        payment.setPayer(payer);
        payment.setTransactions(allTransactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successURL);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    public Payment executePayment(
            @NonNull String paymentId, @NonNull String payerId
            ) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution execution = new PaymentExecution();
        execution.setPayerId(payerId);

        return payment.execute(apiContext,execution);
    }
}
