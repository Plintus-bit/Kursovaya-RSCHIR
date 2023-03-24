package com.plintus.sweetstore.service;

import com.plintus.sweetstore.domain.*;
import com.plintus.sweetstore.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public UserOrders getOrderIdOrCreateNewByUsername(String username) {
        User user = userRep.findByUsername(username);
        OrderStatuses status = OSRep.findById(1).get();
        List<UserOrders> orders = UORep.findByCustomerAndStatus(user.getId(), status.getId());
        if (orders.size() < 1) {
            // создать новый заказ
            UserOrders newOrder = new UserOrders();
            newOrder.setCustomer(user);
            newOrder.setStatus(status);
            newOrder.setOrderDate(new Date());
            newOrder = UORep.save(newOrder);
            return newOrder;
        } else if (orders.size() == 1) {
            return orders.get(0);
        }
        return null;
    }

    public Integer getOrderInCartId (String username) {
        User user = userRep.findByUsername(username);
        OrderStatuses status = OSRep.findById(1).get();
        List<UserOrders> orders = UORep.findByCustomerAndStatus(user.getId(), status.getId());
        if (orders.size() == 1) {
            return orders.get(0).getId();
        }
        return -1;
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

    public void deleteGoodFromCart(String username,
                                   Integer goodId) {
        UserOrders order = getOrderIdOrCreateNewByUsername(username);
        List<OrderGoodStructs> ogsWithGood = OGSRep.findByOrderIdAndGoodId(order.getId(), goodId);
        if (ogsWithGood != null) {
            OGSRep.deleteAll(ogsWithGood);
        }
    }

    public void insertGoodInCart(String username,
                                 Integer goodId, Integer count) {
        if (!(count == null || count == 0)) {
            UserOrders order = getOrderIdOrCreateNewByUsername(username);
            Goods good = goodRep.findById(goodId).get();
            List<OrderGoodStructs> ogsWithGood = OGSRep.findByOrderIdAndGoodId(order.getId(), goodId);
            OrderGoodStructs currentOGS = null;
            if (ogsWithGood.size() == 1) {
                currentOGS = ogsWithGood.get(0);
                if (count + currentOGS.getCount() > good.getCount()) {
                    currentOGS.setCount(good.getCount());
                } else {
                    currentOGS.setCount(count + ogsWithGood.get(0).getCount());
                }
            } else if (ogsWithGood.size() == 0) {
                currentOGS = new OrderGoodStructs();
                currentOGS.setOrderId(order);
                currentOGS.setGoodId(good);
                currentOGS.setCount(count);
            }
            if (currentOGS != null) {
                OGSRep.save(currentOGS);
            }
        }
    }

    public void updateGoodInCart(String username,
                                 Integer goodId,
                                 String type) {
        UserOrders order = getOrderIdOrCreateNewByUsername(username);
        List<OrderGoodStructs> ogsWithGood = OGSRep.findByOrderIdAndGoodId(order.getId(), goodId);
        if (ogsWithGood.size() == 1) {
            OrderGoodStructs currentOGS = ogsWithGood.get(0);
            Integer goodCountInCart = currentOGS.getCount();
            Goods good = currentOGS.getGoodId();
            if (Objects.equals(type, "plus")) {
                if (goodCountInCart + 1 > good.getCount()) {
                    currentOGS.setCount(good.getCount());
                } else {
                    currentOGS.setCount(goodCountInCart + 1);
                }
            } else if (Objects.equals(type, "minus")) {
                if (goodCountInCart - 1 > 0) {
                    currentOGS.setCount(goodCountInCart - 1);
                } else {
                    OGSRep.delete(currentOGS);
                    return;
                }
            }
            OGSRep.save(currentOGS);
        }
    }

    public int getOrderCost(List<OrderGoodStructs> ogs) {
        int finalCost = 0;
        for (OrderGoodStructs curOGS : ogs) {
            finalCost += curOGS.getCount() * curOGS.getGoodId().getCost();
        }
        return finalCost;
    }
}
