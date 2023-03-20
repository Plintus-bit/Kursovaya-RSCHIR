package com.plintus.sweetstore.repos;

import com.plintus.sweetstore.domain.OrderGoodStructs;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderGoodStructsRepository extends CrudRepository<OrderGoodStructs, Integer> {
    List<OrderGoodStructs> findAllByOrderId(Integer orderId);
}
