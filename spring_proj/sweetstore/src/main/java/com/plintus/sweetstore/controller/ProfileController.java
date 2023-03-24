package com.plintus.sweetstore.controller;

import com.plintus.sweetstore.domain.Role;
import com.plintus.sweetstore.domain.User;
import com.plintus.sweetstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private UserService userService;

    @GetMapping
    public String profile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("isAdmin", auth.getAuthorities().contains(Role.ADMIN));
        return "user_profile";
    }

    @GetMapping("/account")
    public String account(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());
        String[] userFullname = userService.getUserFullname();
        model.addAttribute("firstName", userFullname[1]);
        model.addAttribute("lastName", userFullname[0]);
        model.addAttribute("dadName", userFullname[2]);
        return "account";
    }

    @PostMapping("/account")
    public String updateAccount(Model model,
                                @RequestParam String username,
                                @RequestParam String firstName,
                                @RequestParam String lastName,
                                @RequestParam String dadName) {
        model.addAttribute("username", username);
        model.addAttribute("firstName", firstName);
        model.addAttribute("lastName", lastName);
        model.addAttribute("dadName", dadName);
        boolean hasErrors = false;
        if (firstName.length() < 1) {
            hasErrors = true;
            model.addAttribute("messageFirstName", "Укажите имя");
        }
        if (lastName.length() < 1) {
            hasErrors = true;
            model.addAttribute("messageLastName", "Укажите фамилию");
        }
        if (dadName.length() < 1) {
            hasErrors = true;
            model.addAttribute("messageDadName", "Укажите отчество");
        }
        if (hasErrors) {
            return "account";
        }
        boolean status = userService.updateUser(firstName, lastName, dadName);
        System.out.println(status);
        if (!status) {
            model.addAttribute("message", "Пользователь с таким никнеймом уже существует");
            model.addAttribute("status", false);
        } else {
            model.addAttribute("message", "Данные успешно изменены");
            model.addAttribute("status", true);
        }
        return "account";
    }

}
