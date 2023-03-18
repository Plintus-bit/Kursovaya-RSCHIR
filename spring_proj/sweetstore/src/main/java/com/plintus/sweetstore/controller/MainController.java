package com.plintus.sweetstore.controller;

import com.plintus.sweetstore.domain.GoodSubtypes;
import com.plintus.sweetstore.domain.GoodTypes;
import com.plintus.sweetstore.domain.Goods;
import com.plintus.sweetstore.repos.GoodRepository;
import com.plintus.sweetstore.repos.GoodSubtypesRepository;
import com.plintus.sweetstore.repos.GoodTypesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class MainController {
    @Autowired
    private GoodRepository good_rep;
    @Autowired
    private GoodSubtypesRepository good_subtypes_rep;
    @Autowired
    private GoodTypesRepository good_types_rep;
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
        Iterable<GoodTypes> types = good_types_rep.findAll();
        model.put("types", types);
        return "section";
    }

    // изменить mapping, добавить get
    @PostMapping("/section")
    public String addType(@RequestParam String name,
                          Map<String, Object> model) {
        GoodTypes type = new GoodTypes(name);
        good_types_rep.save(type);
        Iterable<GoodTypes> types = good_types_rep.findAll();
        model.put("types", types);
        return "section";
    }
}
