package org.repositories;

import org.entities.SubscriptionPlans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SubscriptionPlansRepository extends JpaRepository<SubscriptionPlans, Long> {
}
