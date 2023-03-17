package com.plintus.sweetstore.controller;

import com.plintus.sweetstore.domain.Good;
import com.plintus.sweetstore.repos.GoodRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.Console;
import java.util.Map;
import java.util.logging.Logger;

@Controller
public class MainController {

    @Autowired
    private GoodRepository good_rep;


    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/main")
    public String main() {
        return "main";
    }

    @GetMapping("/section")
    public String loadGoods(Map<String, Object> model) {
        Iterable<Good> goods = good_rep.findAll();
        model.put("goods", goods);
        return "section";
    }

    // изменить mapping, добавить get
    @PostMapping("/section")
    public String addGood(@RequestParam Integer article,
                          @RequestParam String name,
                          @RequestParam String descript,
                          @RequestParam Integer cost,
                          @RequestParam String url,
                          Map<String, Object> model) {
        Good new_good = new Good(article, name, descript, cost, url);
        good_rep.save(new_good);
        // нужно рефакторить
        Iterable<Good> goods = good_rep.findAll();
        model.put("goods", goods);
        return "section";
    }
}
