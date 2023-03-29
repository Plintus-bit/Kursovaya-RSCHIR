package com.plintus.sweetstore.controller;

import com.plintus.sweetstore.domain.*;
import com.plintus.sweetstore.repos.GoodRepository;
import com.plintus.sweetstore.service.*;
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
    private DeliveryService deliveryService;
    @Autowired
    private IngsService ingsService;
    @Autowired
    private TypesService typesService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String getUserList(Model model) {
        model.addAttribute("users", userService.findAll());
        return "userlist";
    }

    @GetMapping("/users/{user}")
    public String getUserEditForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "useredit";
    }

    @PostMapping("/users")
    public String saveUser(@RequestParam String username,
                           @RequestParam Map<String, String> form,
                           @RequestParam("user_id") User user){
        userService.saveUser(user, username, form);
        return "redirect:/profile/admins/users";
    }

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
        switch (type) {
            case "ings":
                model.addAttribute("all", ingsService.getAllIngredients());
                break;
            case "gtypes":
                model.addAttribute("all", typesService.getAllTypes());
                break;
            case "gsubts":
                model.addAttribute("all", typesService.getAllSubtypes());
                model.addAttribute("allTypes", typesService.getAllTypes());
                break;
            case "goods":
                model.addAttribute("all", goodsService.getAllGoods());
                model.addAttribute("allSubtypes", typesService.getAllSubtypes());
                break;
            case "ings_in_goods":
                model.addAttribute("all", ingsService.getAllIngsInGoods());
                model.addAttribute("ings", ingsService.getAllIngredients());
                model.addAttribute("goods", goodsService.getAllGoods());
                break;
            case "kbjs":
                model.addAttribute("all", goodsService.getAllKBJS());
                model.addAttribute("goods", goodsService.getAllGoods());
                break;
        }
        return type;
    }

    @GetMapping("/orders")
    public String orders(@RequestParam String type, Model model) {
        model.addAttribute("type", type);
        return "edit_db_2";
    }

    @GetMapping("/orders/{type}")
    public String ordersType(@PathVariable String type,
                             @RequestParam String act,
                             Model model) {
        model.addAttribute("act", act);
        switch (act) {
            case "upd":
                model.addAttribute("statuses", orderService.getNotInCartStatuses());
                break;
        }
        model.addAttribute("allOrders", orderService.getAllOrders());
        return type;
    }

    @PostMapping("/orders/statuses/upd")
    public String statUpd(@RequestParam String ids,
                          @RequestParam Integer newStatus) {
        orderService.updateStatuses(ids, newStatus);
        return "redirect:/profile/admins/orders/statuses?act=upd";
    }

    @PostMapping("/orders/statuses/increase")
    public String statIncr(@RequestParam String ids) {
        orderService.increaseStatuses(ids);
        return "redirect:/profile/admins/orders/statuses?act=increase";
    }

    @PostMapping("ings/ins")
    public String ingsIns(@RequestParam String name) {
        ingsService.insertIngredients(name);
        return "redirect:/profile/admins/ings?act=ins";
    }

    @PostMapping("ings/upd")
    public String ingsUpd(@RequestParam String oldNames,
                          @RequestParam String newNames) {
        if (oldNames == null) {
            oldNames = "";
        }
        ingsService.updateIngredients(oldNames, newNames);
        return "redirect:/profile/admins/ings?act=upd";
    }

    @PostMapping("ings/del")
    public String ingsDel(@RequestParam String names) {
        ingsService.deleteIngredients(names);
        return "redirect:/profile/admins/ings?act=del";
    }

    @PostMapping("gtypes/ins")
    public String gtypesIns(@RequestParam String name,
                            @RequestParam String url) {
        typesService.insertTypes(name, url);
        return "redirect:/profile/admins/gtypes?act=ins";
    }

    @PostMapping("gtypes/upd")
    public String gtypesUpd(@RequestParam String oldNames,
                            @RequestParam String newNames,
                            @RequestParam String urls) {
        typesService.updateTypes(oldNames, newNames, urls);
        return "redirect:/profile/admins/gtypes?act=upd";
    }

    @PostMapping("gtypes/del")
    public String gtypesDel(@RequestParam String names) {
        typesService.deleteTypes(names);
        return "redirect:/profile/admins/gtypes?act=del";
    }
    @PostMapping("gsubts/ins")
    public String gsubtsIns(@RequestParam String name,
                            @RequestParam String parentId) {
        typesService.insertSubtypes(name, parentId);
        return "redirect:/profile/admins/gsubts?act=ins";
    }

    @PostMapping("gsubts/upd")
    public String gsubtsUpd(@RequestParam String oldNames,
                            @RequestParam String newNames,
                            @RequestParam String newParentNames) {
        typesService.updateSubtypes(oldNames, newNames, newParentNames);
        return "redirect:/profile/admins/gsubts?act=upd";
    }

    @PostMapping("gsubts/del")
    public String gsubtsDel(@RequestParam String names) {
        typesService.deleteSubtypes(names);
        return "redirect:/profile/admins/gsubts?act=del";
    }

    @PostMapping("goods/ins")
    public String goodsIns(@RequestParam String article,
                           @RequestParam String name,
                           @RequestParam String descript,
                           @RequestParam String subtype,
                           @RequestParam String cost,
                           @RequestParam String count,
                           @RequestParam String url) {
        goodsService.insertGoods(article, name, descript, subtype, cost, count, url);
        return "redirect:/profile/admins/goods?act=ins";
    }

    @PostMapping("goods/upd")
    public String goodsUpd(@RequestParam String oldArticle,
                           @RequestParam String article,
                           @RequestParam String name,
                           @RequestParam String descript,
                           @RequestParam String subtype,
                           @RequestParam String cost,
                           @RequestParam String count,
                           @RequestParam String url) {
        goodsService.updateGoods(oldArticle, article, name, descript, subtype, cost, count, url);
        return "redirect:/profile/admins/goods?act=upd";
    }

    @PostMapping("goods/del")
    public String goodsDel(@RequestParam String article) {
        goodsService.deleteGoods(article);
        return "redirect:/profile/admins/goods?act=del";
    }

    @PostMapping("kbjs/ins")
    public String kbjsIns(@RequestParam String article,
                          @RequestParam String caloric,
                          @RequestParam String proteins,
                          @RequestParam String fats,
                          @RequestParam String carbhyd,
                          @RequestParam String diet_fiber,
                          @RequestParam String water) {
        goodsService.insertNutrition(article, caloric,
                fats, proteins, diet_fiber, carbhyd, water);
        return "redirect:/profile/admins/kbjs?act=ins";
    }

    @PostMapping("kbjs/upd")
    public String kbjsUpd(@RequestParam String article,
                          @RequestParam String caloric,
                          @RequestParam String proteins,
                          @RequestParam String fats,
                          @RequestParam String carbhyd,
                          @RequestParam String diet_fiber,
                          @RequestParam String water) {
        goodsService.updateNutrition(article, caloric,
                fats, proteins, diet_fiber, carbhyd, water);
        return "redirect:/profile/admins/kbjs?act=upd";
    }

    @PostMapping("kbjs/del")
    public String kbjsDel(@RequestParam String article) {
        goodsService.deleteNutrition(article);
        return "redirect:/profile/admins/kbjs?act=del";
    }

    @PostMapping("ings_in_goods/ins")
    public String ingsInGoodsIns(@RequestParam String article,
                                 @RequestParam String ings) {
        ingsService.insertIngsStructs(article, ings);
        return "redirect:/profile/admins/ings_in_goods?act=ins";
    }
    @PostMapping("ings_in_goods/del")
    public String ingsInGoodsDel(@RequestParam String article,
                                 @RequestParam String ings) {
        ingsService.deleteIngsStructs(article, ings);
        return "redirect:/profile/admins/ings_in_goods?act=del";
    }

}
