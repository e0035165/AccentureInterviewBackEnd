package org.services;

import org.entities.CustomUserDetails;
import org.repositories.CustomUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private CustomUserRepository customUserRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<CustomUserDetails> optional = customUserRepository.findAll().stream()
                .filter(u -> u.getUsername().equals(username)).findFirst();
        if (optional.isPresent()) {
            return optional.get();
        }

        return null;
    }


    public void addUser(CustomUserDetails customUserDetails) {
        customUserRepository.save(customUserDetails);
    }

    public void editUser(CustomUserDetails customUserDetails) {
        customUserRepository.save(customUserDetails);
    }
}
