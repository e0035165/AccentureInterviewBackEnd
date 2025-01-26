package com.services;


import com.entities.ROLE;
import com.repositories.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ROLEService {

    @Autowired
    private RolesRepository rolesRepository;

    public void add(ROLE role) {
        Optional<ROLE> test=rolesRepository.findById(role.getId());
        if(test.isPresent()) {
            rolesRepository.delete(test.get());
        }
        rolesRepository.save(role);
    }

    public ROLE getByRole(String role) {
        Optional<ROLE>getRole = rolesRepository.findAll().stream()
                .filter(role1 -> role1.getName().equals(role)).findFirst();
        return getRole.orElse(null);
    }

    public List<ROLE> getRelevantRoles(Set<String> roles) {
        List<ROLE>getAllROLES = rolesRepository.findAll().stream()
                .filter(role -> roles.contains(role.getName())).toList();
        return getAllROLES;
    }
}
