package com.plintus.sweetstore.controller;

import com.plintus.sweetstore.domain.User;
import com.plintus.sweetstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Controller
public class RegistrationController {
    @Autowired
    UserService user_service;

    @GetMapping("/registration")
    public String registr() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model) {
        if (user.getUsername().length() < 1) {
            model.put("message", "Пользователь не может быть без имени");
            return "registration";
        }
        if (user.getPassword().length() < 1) {
            model.put("message", "Слишком маленький пароль");
            return "registration";
        }
        if (!user_service.saveUser(user)) {
            model.put("message", "Пользователь уже существует.");
            return "registration";
        }
        return "redirect:/login";
    }
}
