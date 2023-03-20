package com.plintus.sweetstore.controller;

import com.plintus.sweetstore.domain.GoodSubtypes;
import com.plintus.sweetstore.domain.GoodTypes;
import com.plintus.sweetstore.domain.Ingredients;
import com.plintus.sweetstore.repos.GoodRepository;
import com.plintus.sweetstore.service.GoodsService;
import com.plintus.sweetstore.service.IngsService;
import com.plintus.sweetstore.service.TypesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/profile/admins")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminsController {
    @Autowired
    private IngsService ingsService;
    @Autowired
    private TypesService typesService;
    @Autowired
    private GoodsService goodsService;
    @GetMapping
    public String adminsPage(@RequestParam String type) {
        return "redirect:/profile/admins/edit_db?type=" + type;
    }

    @GetMapping("/edit_db")
    public String editPage(@RequestParam String type, Model model) {
        model.addAttribute("type", type);
        return "edit_db";
    }

    @GetMapping("{type}")
    public String goods(@PathVariable String type,
                        @RequestParam String act,
                        Model model) {
        model.addAttribute("act", act);
        if (Objects.equals(act, "upd") || Objects.equals(act, "del")) {
            model.addAttribute("oldIng", new Ingredients());
        }
        if (Objects.equals(type, "ings")) {
            model.addAttribute("all", ingsService.getAllIngredients());
        }
        else if (Objects.equals(type, "gtypes")) {
            model.addAttribute("all", typesService.getAllTypes());
        }
        else if (Objects.equals(type, "gsubts")) {
            model.addAttribute("all", typesService.getAllSubtypes());
        }
        return type;
    }

    @PostMapping("ings/ins")
    public String ingsIns(@RequestParam String name) {
        ingsService.insertIngredients(name);
        return "redirect:/profile";
    }

    @PostMapping("ings/upd")
    public String ingsUpd(@RequestParam String oldNames,
                          @RequestParam String newNames) {
        if (oldNames == null) {
            oldNames = "";
        }
        ingsService.updateIngredients(oldNames, newNames);
        return "redirect:/profile";
    }

    @PostMapping("ings/del")
    public String ingsDel(@RequestParam String names) {
        ingsService.deleteIngredients(names);
        return "redirect:/profile";
    }

    @PostMapping("gtypes/ins")
    public String gtypesIns(@RequestParam String name,
                            @RequestParam String url) {
        typesService.insertTypes(name, url);
        return "redirect:/profile";
    }

    @PostMapping("gtypes/upd")
    public String gtypesUpd(@RequestParam String oldNames,
                            @RequestParam String newNames,
                            @RequestParam String urls) {
        typesService.updateTypes(oldNames, newNames, urls);
        return "redirect:/profile";
    }

    @PostMapping("gtypes/del")
    public String gtypesDel(@RequestParam String names) {
        typesService.deleteTypes(names);
        return "redirect:/profile";
    }
    @PostMapping("gsubts/ins")
    public String gsubtsIns(@RequestParam String name,
                            @RequestParam String parentIds) {
        typesService.insertTypes(name, parentIds);
        return "redirect:/profile";
    }

    @PostMapping("gsubts/upd")
    public String gsubtsUpd(@RequestParam String oldNames,
                            @RequestParam String newNames,
                            @RequestParam String newParentNames) {
        typesService.updateSubtypes(oldNames, newNames, newParentNames);
        return "redirect:/profile";
    }

    @PostMapping("gsubts/del")
    public String gsubtsDel(@RequestParam String names) {
        typesService.deleteSubtypes(names);
        return "redirect:/profile";
    }

}
