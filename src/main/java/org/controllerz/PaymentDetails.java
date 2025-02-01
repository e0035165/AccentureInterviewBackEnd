package com.controllerz;

public record PaymentDetails(Double total, String currency, String method, String intent, String description) {

}
