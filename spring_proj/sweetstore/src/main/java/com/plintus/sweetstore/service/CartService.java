package com.plintus.sweetstore.service;

import com.plintus.sweetstore.domain.*;
import com.plintus.sweetstore.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {
    @Autowired
    private OrderGoodStructsRepository OGSRep;
    @Autowired
    private GoodRepository goodRep;
    @Autowired
    private UserOrdersRepository UORep;
    @Autowired
    private UserRepository userRep;
    @Autowired
    private OrderStatusesRepository OSRep;

    public Integer getOrderIdOrCreateNewByUser(User user) {
        OrderStatuses status = OSRep.findByName("не оформлен").get();
        List<UserOrders> orders = UORep.findByCustomerAndStatus(user.getId(), status.getId());
        if (orders.size() != 1) {
            // создать новый заказ
        }
        // получить номер текущего
        return null;
    }

    public List<OrderGoodStructs> getGoodsInOrder() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRep.findByUsername(auth.getName());
        List<UserOrders> orders = UORep
                .findByCustomerAndStatus(user.getId(),OSRep.findByName("не оформлен").get().getId());
        if (orders.size() != 0) {
            Integer orderId = orders.get(0).getId();
            return OGSRep.findAllByOrderId(orderId);
        }
        return new ArrayList<OrderGoodStructs>();

    }

    public List<Goods> getGoodsByOrderId() {
        return null;
    }

}
