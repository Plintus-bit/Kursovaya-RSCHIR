package com.plintus.sweetstore;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Objects;

@Controller
public class StoreController {

    @GetMapping("/")
    public String IndexPage(Map<String, Object> model) {
        model.put("name", "Plintus");
        model.put("who", "арт художник");
        return "index";
    }
}
