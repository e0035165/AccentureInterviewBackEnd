package org.controllerz;

import java.util.Objects;

public record SubscriptionBody(String name, Float rate) {
    public SubscriptionBody {
        Objects.requireNonNull(name);
        Objects.requireNonNull(rate);
    }
}
