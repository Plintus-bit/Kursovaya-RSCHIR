package com.plintus.sweetstore.repos;

import com.plintus.sweetstore.domain.UserOrders;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserOrdersRepository extends CrudRepository<UserOrders, Integer> {
    @Query(
            value = "SELECT * FROM user_orders WHERE customer_id = :customer and status_id = :status",
            nativeQuery = true
    )
    List<UserOrders> findByCustomerAndStatus(Long customer, Integer status);
}
