package com.plintus.sweetstore.controller;

import com.plintus.sweetstore.domain.GoodTypes;
import com.plintus.sweetstore.domain.Goods;
import com.plintus.sweetstore.service.CartService;
import com.plintus.sweetstore.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private GoodsService goodServ;
    @Autowired
    private CartService cartService;
    @GetMapping
    public String menu(Map<String, Object> model) {
        Iterable<GoodTypes> types = goodServ.getAllTypes();
        model.put("c_datas", types);
        return "menu";
    }
    @GetMapping("/section")
    public String section(@RequestParam Integer cId, Model model) {
        model.addAttribute("good_type", goodServ.getTypeById(cId));
        model.addAttribute("goods", goodServ.getGoodsByTypeId(cId));
        return "section";
    }

    @GetMapping("/section/good")
    public String good(@RequestParam Integer id, Model model) {
        model.addAttribute("good", goodServ.getGoodById(id));
        model.addAttribute("ings", goodServ.getGoodIngsTextByGoodId(id));
        model.addAttribute("nutrition", goodServ.getGoodNutrByGoodId(id));
        return "good";
    }
}
