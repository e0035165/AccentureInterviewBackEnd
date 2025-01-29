package org.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Entity
@Table(name="Users")
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    public String email;

    @Column(name = "Outstanding-Payments")
    public float outstandingPayments;

    public CustomUserDetails() {

    }


//    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    @JoinTable(
//            name="archived_news",
//            joinColumns = {@JoinColumn(name="user_id",referencedColumnName = "id"),@JoinColumn(name="username",referencedColumnName = "username")},
//            inverseJoinColumns = {@JoinColumn(name="news_id",referencedColumnName = "id"),@JoinColumn(name="news_title",referencedColumnName = "title")}
//    )
//    public List<AllNews>allNews;
//
//
//    public void setAllNews(List<AllNews> allNews) {
//        this.allNews = allNews;
//    }
//
//    public List<AllNews> getAllNews() {
//        if(allNews == null)
//            allNews = new ArrayList<>();
//
//        return allNews;
//    }
//
//    public void addNews(List<AllNews> allNews) {
//        if(this.allNews == null){
//            this.allNews = new ArrayList<>();
//        }
//        this.allNews.addAll(allNews);
//    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private SubscriptionPlans purchasedSubscriptionPlans;

    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name="user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private List<ROLE>roles;

    public List<ROLE> getRoles() {
        if (roles == null) {
            roles = new ArrayList<>();
        }
        return roles;
    }

    public void addRoles(ROLE role) {
        if(roles == null)
            roles = new ArrayList<>();

        this.roles.add(role);
    }

    public void removeRoles(ROLE role) {
        if(roles != null)
            this.roles.remove(role);
    }

    public void setRoles(List<ROLE> roles) {
        this.roles = roles;
    }

    public void addRole(ROLE role) {
        getRoles().add(role);
    }

    public void removeRole(ROLE role) {
        getRoles().remove(role);
    }

    public SubscriptionPlans getSubscriptionPlans() {
        return purchasedSubscriptionPlans;
    }

    private boolean isValid;


    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }





    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }




}
