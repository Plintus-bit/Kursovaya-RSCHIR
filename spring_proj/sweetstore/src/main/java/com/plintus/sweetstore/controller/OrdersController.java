package com.plintus.sweetstore.controller;

import com.plintus.sweetstore.domain.UserOrders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orders")
public class OrdersController {
    @GetMapping
    public String orders() {
        return "orders";
    }

    @GetMapping("{order}")
    public String getUserEditForm(@PathVariable UserOrders order, Model model) {
        model.addAttribute("order", order);
        return "order_view";
    }
}
