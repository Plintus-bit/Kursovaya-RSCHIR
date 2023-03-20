package com.plintus.sweetstore.controller;

import com.plintus.sweetstore.domain.User;
import com.plintus.sweetstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

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
        if (!userService.saveUser(user)) {
            model.put("message", "Пользователь уже существует.");
            return "registration";
        }
        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean is_activated = userService.activateUser(code);
        if (is_activated) {
            model.addAttribute("message", "Активация прошла успешно!");
        } else {
            model.addAttribute("message", "Код активации не найден");
        }
        return "login";
    }
}
