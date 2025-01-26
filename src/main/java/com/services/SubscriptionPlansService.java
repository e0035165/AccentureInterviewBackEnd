package com.services;


import com.entities.CustomUserDetails;
import com.entities.SubscriptionPlans;
import com.repositories.SubscriptionPlansRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionPlansService {
    @Autowired
    private SubscriptionPlansRepository subscriptionPlansRepository;

    public List<SubscriptionPlans> getAllSubscriptionPlans() {
        return subscriptionPlansRepository.findAll();
    }

    public SubscriptionPlans getSubscriptionPlanById(long id) {
        Optional<SubscriptionPlans> getPlan = subscriptionPlansRepository.findById(id);
        return getPlan.orElse(null);
    }

    public SubscriptionPlans getSubscriptionPlanByName(String name) {
        Optional<SubscriptionPlans> getPlan = subscriptionPlansRepository.findAll()
                .stream().filter(plan -> plan.getName().equals(name)).findFirst();
        return getPlan.orElse(null);
    }

//    public List<CustomUserDetails> getCustomUserDetails(long id) {
//        SubscriptionPlans plan = getSubscriptionPlanById(id);
//        return plan.getUsers();
//    }

    public void addSubscriptionPlan(SubscriptionPlans subscriptionPlans) {
        subscriptionPlansRepository.save(subscriptionPlans);
    }
}
