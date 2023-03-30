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
        List<UserOrders> orders = orderService.getOrdersSortedByStatusAndDate(
                orderService.getUserOrders());
        boolean isEmpty = orders.size() == 0;
        model.addAttribute("isEmpty", isEmpty);
        model.addAttribute("orders", orders);
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

    @GetMapping("/drop_order/{id}")
    public String dropOrder(@PathVariable Integer id,
                            Model model) {
        boolean isCanceled = orderService.cancelOrder(id);
        if (isCanceled) {
            model.addAttribute("messageTitle", "Заказ успешно отменён!");
            model.addAttribute("message", "Очень жаль, что вы передумали :(");
        } else {
            model.addAttribute("messageTitle", "Что-то пошло не так");
            model.addAttribute("message", "К сожалению, " +
                    "при отмене заказа произошёл сбой. Попробуйте ещё раз позже");
        }
        return "order_is_placed";
    }
}
