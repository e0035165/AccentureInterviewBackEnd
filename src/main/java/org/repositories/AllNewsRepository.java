package org.repositories;


import org.entities.AllNews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllNewsRepository extends JpaRepository<AllNews, Long> {
}
