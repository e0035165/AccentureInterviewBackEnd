package com.repositories;

import com.entities.ROLE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<ROLE,Integer> {
}
