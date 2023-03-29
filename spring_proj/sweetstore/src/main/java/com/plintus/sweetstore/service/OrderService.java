package com.plintus.sweetstore.service;

import com.plintus.sweetstore.domain.OrderGoodStructs;
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

import java.util.ArrayList;
import java.util.Comparator;
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
                              String dadName, String phone,
                              Integer deliveryType, String address) {
        UserOrders orderToPlace = getCurrentOrderInCart();
        if (orderToPlace != null) {
            if (!goodsService.updateGoodsCount(orderToPlace)) {
                return false;
            }
            orderToPlace = deliveryService.addPrMethodToOrder(orderToPlace, address, deliveryType);
            userService.updateUser(firstName, lastName, dadName, phone);
            orderToPlace.setStatus(OSRep.findById(2).get());
            UORep.save(orderToPlace);
            return true;
        }
        return false;
    }

    public UserOrders getUserOrder(Integer orderId) {
        return UORep.findById(orderId).get();
    }

    public List<OrderGoodStructs> getOGSInOrder(Integer orderId) {
        return OGSRep.findAllByOrderId(orderId);
    }

    public OrderStatuses getOrderStatus(Integer orderId) {
        return UORep.findById(orderId).get().getStatus();
    }

    public Integer getLastOrderStatusId() {
        Iterable<OrderStatuses> statuses = OSRep.findAll();
        int counter = 0;
        for (OrderStatuses st : statuses) {
            ++counter;
        }
        return counter;
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

    public List<UserOrders> getUserOrders() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByUsername(auth.getName());
        return UORep.findAllByCustomerAndNotDefaultStatus(user.getId());
    }

    public Iterable<OrderStatuses> getAllStatuses() {
        return OSRep.findAll();
    }

    public Iterable<OrderStatuses> getNotInCartStatuses() {
        return OSRep.findAllByNotId(1);
    }

    public Iterable<UserOrders> getAllOrders() {
        return UORep.findAll();
    }

    public void updateStatuses(String ids, Integer newStatus) {
        if (!Objects.equals(ids, "")) {
            OrderStatuses status = OSRep.findById(newStatus).get();
            List<Integer> idsList = UtilService.getIntegerListFromStringList(
                    UtilService.getStringListFromStringData(ids)
            );
            Iterable<UserOrders> orders = UORep.findAllById(idsList);
            for (UserOrders order : orders) {
                order.setStatus(status);
            }
            UORep.saveAll(orders);
        }
    }

    public void increaseStatuses(String ids) {
        if (!Objects.equals(ids, "")) {
            List<Integer> idsList = UtilService.getIntegerListFromStringList(
                    UtilService.getStringListFromStringData(ids)
            );
            Iterable<UserOrders> orders = UORep.findAllById(idsList);
            Integer lastOrderStatusId = getLastOrderStatusId();
            for (UserOrders order : orders) {
                Integer newStatusId = order.getStatus().getId() + 1;
                if (newStatusId <= lastOrderStatusId) {
                    OrderStatuses newSt = OSRep.findById(newStatusId).get();
                    order.setStatus(newSt);
                }
            }
            UORep.saveAll(orders);
        }
    }

    public int getOrderFinalCost(List<OrderGoodStructs> ogs) {
        int finalCost = 0;
        for (OrderGoodStructs curOGS : ogs) {
            finalCost += curOGS.getCount() * curOGS.getGoodId().getCost();
        }
        return finalCost;
    }

    public List<UserOrders> getOrdersSortedByStatusAndDate(List<UserOrders> orders) {
        Integer lastOrderStatusId = getLastOrderStatusId();
        orders.sort(new Comparator<UserOrders>() {
            @Override
            public int compare(UserOrders o1, UserOrders o2) {
                boolean resBefore = o1.getOrderDate().before(o2.getOrderDate());
                boolean resAfter = o1.getOrderDate().after(o2.getOrderDate());
                if (resBefore) {
                    return 1;
                } else if (resAfter) {
                    return -1;
                }
                return 0;
            }
        });
        orders.sort(new Comparator<UserOrders>() {
            @Override
            public int compare(UserOrders o1, UserOrders o2) {
                int o1St = Objects.equals(o1.getStatus().getId(), lastOrderStatusId) ? 1 : 0;
                int o2St = Objects.equals(o2.getStatus().getId(), lastOrderStatusId) ? 1 : 0;
                if (o1St > o2St) {
                    return 1;
                } else if (o1St < o2St) {
                    return -1;
                }
                return 0;
            }
        });
        return orders;
    }
}
