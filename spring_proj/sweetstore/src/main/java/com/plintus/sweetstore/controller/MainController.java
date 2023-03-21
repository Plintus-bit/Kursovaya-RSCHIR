package com.plintus.sweetstore.controller;

import com.plintus.sweetstore.domain.GoodTypes;
import com.plintus.sweetstore.repos.GoodRepository;
import com.plintus.sweetstore.repos.GoodSubtypesRepository;
import com.plintus.sweetstore.repos.GoodTypesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class MainController {
    @Autowired
    private GoodRepository goodRep;
    @Autowired
    private GoodSubtypesRepository good_subtypesRep;
    @Autowired
    private GoodTypesRepository goodTypesRep;
    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/main")
    public String main() {
        return "main";
    }
    @GetMapping("/section")
    public String loadTypes(Map<String, Object> model) {
        Iterable<GoodTypes> types = goodTypesRep.findAll();
        model.put("types", types);
        return "section";
    }
}
