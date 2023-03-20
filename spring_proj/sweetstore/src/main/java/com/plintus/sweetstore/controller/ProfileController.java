package com.plintus.sweetstore.controller;

import com.plintus.sweetstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private UserService userService;

    private Authentication auth;

    @GetMapping
    public String profile(Model model) {
        model.addAttribute("isAdmin", true);
        auth = SecurityContextHolder.getContext().getAuthentication();
        return "user_profile";
    }

}
