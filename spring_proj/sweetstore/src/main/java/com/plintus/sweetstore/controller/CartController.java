package com.plintus.sweetstore.controller;

import com.plintus.sweetstore.domain.OrderGoodStructs;
import com.plintus.sweetstore.domain.User;
import com.plintus.sweetstore.service.CartService;
import com.plintus.sweetstore.service.DeliveryService;
import com.plintus.sweetstore.service.OrderService;
import com.plintus.sweetstore.service.UserService;
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
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private DeliveryService deliveryService;
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
                           @RequestParam(required = false) Integer cId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (cId == null) {
            if (Objects.equals(act, "del")) {
                cartService.deleteGoodFromCart(username, goodId);
            }
            return "redirect:/cart";
        }
        if (Objects.equals(act, "ins")) {
            cartService.insertGoodInCart(username, goodId, value);
        } else if (Objects.equals(act, "del")) {
            cartService.deleteGoodFromCart(username, goodId);
        }
        return "redirect:/menu/section?cId=" + cId;
    }

    @PostMapping("/edit_good_in_cart")
    public String editGoodInCart(@RequestParam String type,
                                 @RequestParam Integer goodId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        cartService.updateGoodInCart(username, goodId, type);
        return "redirect:/cart";
    }

    @GetMapping("/order_making")
    public String orderMaking(Model model) {
        String[] userFullname = userService.getUserFullname();
        model.addAttribute("firstName", userFullname[1]);
        model.addAttribute("lastName", userFullname[0]);
        model.addAttribute("dadName", userFullname[2]);
        model.addAttribute("deliveryTypes", deliveryService.getDeliveryTypes());
        return "order_making";
    }

    @PostMapping("/order_making/place_order")
    public String placeOrder(Model model,
                             @RequestParam String firstName,
                             @RequestParam String lastName,
                             @RequestParam String dadName,
                             @RequestParam Integer delivery_type,
                             @RequestParam String address) {
        boolean status = orderService.placeOrder(firstName, lastName, dadName, delivery_type, address);
        model.addAttribute("status", status);
        if (status) {
            model.addAttribute("message", "Заказ успешно оформлен!");
        } else {
            model.addAttribute("message", "Произошла ошибка. Приносим извинения :(");
        }
        return "order_is_placed";
    }
}
