package com.plintus.sweetstore.repos;

import com.plintus.sweetstore.domain.OrderStatuses;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OrderStatusesRepository extends CrudRepository<OrderStatuses, Integer> {
    Optional<OrderStatuses> findByName(String name);
}
