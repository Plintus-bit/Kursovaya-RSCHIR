package com.plintus.sweetstore.controller;

import com.plintus.sweetstore.domain.OrderGoodStructs;
import com.plintus.sweetstore.domain.User;
import com.plintus.sweetstore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    private Authentication auth;
    @GetMapping
    public String cart(Model model) {
        auth = SecurityContextHolder.getContext().getAuthentication();
        List<OrderGoodStructs> ogs = cartService.getGoodsInOrder();
        model.addAttribute("ogs", ogs);
        if (ogs == null || ogs.size() == 0) {
            model.addAttribute("orderId", -1);
        } else {
            model.addAttribute("orderId", ogs.get(0).getOrderId().getId());
        }
        return "cart";
    }
}
