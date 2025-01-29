package org.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Subscriptions")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SubscriptionPlans {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Name_of_plan")
    private String name;

    @Column(name="rate_per_month")
    private float ratePerMonth;

    public SubscriptionPlans(String name, float ratePerMonth) {
        this.name = name;
        this.ratePerMonth = ratePerMonth;
    }


    @OneToMany(mappedBy = "purchasedSubscriptionPlans", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<CustomUserDetails> users;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setUsers(List<CustomUserDetails> users) {
        this.users = users;
    }

    public List<CustomUserDetails> getUsers() {
        return users;
    }

    public void addUser(CustomUserDetails user) {
        if(this.users == null)
            this.users = new ArrayList<>();

        this.users.add(user);
    }

    public void removeUser(CustomUserDetails user) {
        if(this.users == null || !this.users.contains(user))
            return;

        this.users.remove(user);
    }

}
