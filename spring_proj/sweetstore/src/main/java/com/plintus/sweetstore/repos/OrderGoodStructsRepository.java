package com.plintus.sweetstore.repos;

import com.plintus.sweetstore.domain.Goods;
import com.plintus.sweetstore.domain.OrderGoodStructs;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderGoodStructsRepository extends CrudRepository<OrderGoodStructs, Integer> {
    @Query(
            value = "SELECT * FROM order_good_structs WHERE order_id = :orderId",
            nativeQuery = true
    )
    List<OrderGoodStructs> findAllByOrderId(Integer orderId);

    @Query(
            value = "SELECT * FROM order_good_structs WHERE order_id = :orderId and good_id = :goodId",
            nativeQuery = true
    )
    List<OrderGoodStructs> findByOrderIdAndGoodId(Integer orderId, Integer goodId);
}
