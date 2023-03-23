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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registr() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user,
                          @RequestParam String passwordConfirm,
                          Map<String, Object> model) {
        System.out.println(user.toString());
        boolean hasErrors = false;
        if (user.getUsername().length() < 1) {
            hasErrors = true;
            model.put("messageUsername", "Пользователь не может быть без имени");
        }
        if (!user.getPassword().equals(passwordConfirm)) {
            hasErrors = true;
            model.put("messagePasswdConf", "Введены разные пароли");
        }
        if (user.getPassword().length() < 1) {
            hasErrors = true;
            model.put("messagePasswd", "Слишком маленький пароль");
        }
        if (user.getEmail() == null || Objects.equals(user.getEmail(), "")) {
            hasErrors = true;
            model.put("messageEmail", "Почта не может быть пустой");
        }
        if (user.getPhone() == null || Objects.equals(user.getPhone(), "")) {
            hasErrors = true;
            model.put("messagePhone", "Телефон не может быть пустой");
        }
        if (hasErrors) {
            return "registration";
        }
        if (!userService.saveUser(user)) {
            model.put("messageUserExist", "Пользователь уже существует.");
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
