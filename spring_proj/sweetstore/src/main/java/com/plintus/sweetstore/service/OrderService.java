package com.plintus.sweetstore.service;

import com.plintus.sweetstore.domain.OrderStatuses;
import com.plintus.sweetstore.domain.User;
import com.plintus.sweetstore.domain.UserOrders;
import com.plintus.sweetstore.repos.OrderGoodStructsRepository;
import com.plintus.sweetstore.repos.OrderStatusesRepository;
import com.plintus.sweetstore.repos.UserOrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class OrderService {
    @Autowired
    private UserOrdersRepository UORep;
    @Autowired
    private OrderGoodStructsRepository OGSRep;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderStatusesRepository OSRep;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private DeliveryService deliveryService;

    public boolean placeOrder(String firstName, String lastName,
                              String dadName, Integer deliveryType,
                              String address) {
        UserOrders orderToPlace = getCurrentOrderInCart();
        if (orderToPlace != null) {
            if (!goodsService.updateGoodsCount(orderToPlace)) {
                return false;
            }
            orderToPlace = deliveryService.addPrMethodToOrder(orderToPlace, address, deliveryType);
            userService.updateUser(firstName, lastName, dadName);
            orderToPlace.setStatus(OSRep.findById(2).get());
            UORep.save(orderToPlace);
            return true;
        }
        return false;
    }

    public UserOrders getCurrentOrderInCart() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByUsername(auth.getName());
        OrderStatuses status = OSRep.findById(1).get();
        List<UserOrders> orders = UORep.findByCustomerAndStatus(user.getId(), status.getId());
        if (orders.size() == 1) {
            return orders.get(0);
        }
        return null;
    }
}
