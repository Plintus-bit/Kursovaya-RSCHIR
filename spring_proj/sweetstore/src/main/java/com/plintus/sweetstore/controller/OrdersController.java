package com.plintus.sweetstore.controller;

import com.plintus.sweetstore.domain.OrderGoodStructs;
import com.plintus.sweetstore.domain.OrderStatuses;
import com.plintus.sweetstore.domain.UserOrders;
import com.plintus.sweetstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrdersController {
    @Autowired
    private OrderService orderService;
    @GetMapping
    public String orders(Model model) {
        model.addAttribute("orders", orderService.getOrdersSortedByStatusAndDate(
                orderService.getUserOrders()
        ));
        model.addAttribute("lastStatusId", orderService.getLastOrderStatusId());
        return "orders";
    }

    @GetMapping("/order_view")
    public String getUserEditForm(@RequestParam Integer id,
                                  Model model) {
        OrderStatuses orderStatus = orderService.getOrderStatus(id);
        List<OrderGoodStructs> ogs = orderService.getOGSInOrder(id);
        model.addAttribute("order", orderService.getUserOrder(id));
        model.addAttribute("lastStatusId", orderService.getLastOrderStatusId());
        model.addAttribute("statusId", orderStatus.getId());
        model.addAttribute("status", orderStatus.getName());
        model.addAttribute("ogs", ogs);
        model.addAttribute("finalCost", orderService.getOrderFinalCost(ogs));
        return "order_view";
    }
}
