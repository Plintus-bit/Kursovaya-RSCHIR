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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    private Authentication auth;
    @GetMapping
    public String cart(Model model) {
        List<OrderGoodStructs> ogs = cartService.getGoodsInOrder();
        model.addAttribute("ogs", ogs);
        if (ogs == null || ogs.size() == 0) {
            model.addAttribute("orderId", -1);
        } else {
            model.addAttribute("orderId", ogs.get(0).getOrderId().getId());
            model.addAttribute("finalCost", cartService.getOrderCost(ogs));
        }
        return "cart";
    }

    @PostMapping("/edit_cart")
    public String editCart(@RequestParam Integer goodId,
                           @RequestParam String act,
                           @RequestParam(required = false) Integer value,
                           @RequestParam Integer cId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (Objects.equals(act, "ins")) {
            cartService.insertGoodInCart(username, goodId, value);
        } else if (Objects.equals(act, "del")) {
            cartService.deleteGoodFromCart(username, goodId);
        }
        return "redirect:/menu/section?cId=" + cId;
    }
}
