package com.plintus.sweetstore.service;

import com.plintus.sweetstore.domain.*;
import com.plintus.sweetstore.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GoodsService {
    @Autowired
    private GoodRepository goodRep;
    @Autowired
    private OrderGoodStructsRepository OGSRep;
    @Autowired
    private GoodTypesRepository goodTypeRep;
    @Autowired
    private GoodSubtypesRepository goodSubtypeRep;
    @Autowired
    private IngStructuresRepository ingStructRep;
    @Autowired
    private IngredientsRepository ingsRep;
    @Autowired
    private NutritionsRepository nutrRep;

    public List<GoodSubtypes> getSubtypesByParentId(Integer parentId) {
        return goodSubtypeRep.findByParentId(parentId);
    }

    public Iterable<GoodTypes> getAllTypes() {
        return goodTypeRep.findAll();
    }

    public GoodTypes getTypeById(Integer id) {
        return goodTypeRep.findById(id).get();
    }

    public List<Goods> getGoodsByTypeId(Integer id) {
        Collection<GoodSubtypes> subtypes = getSubtypesByParentId(id);
        return goodRep.findBySubtypeIn(subtypes);
    }

    public Goods getGoodById(Integer id) {
        return goodRep.findByArticle(id).get();
    }

    public List<String> getGoodIngsNamesByGoodId(Integer id) {
        List<Integer> ingsIn_good = ingStructRep.findIngIdByArticle(id);
        return ingsRep.findNameByIdIn(ingsIn_good);
    }

    public String getGoodIngsTextByGoodId(Integer id) {
        List<String> names = getGoodIngsNamesByGoodId(id);
        if (names == null) {
            return "";
        }
        String resultText = names.get(0);
        String sepr = ", ";
        names.remove(0);
        for (String name : names) {
            resultText = resultText.concat(sepr).concat(name);
        }
        return resultText;
    }

    public Nutritions getGoodNutrByGoodId(Integer id) {
        Optional<Nutritions> nurt = nutrRep.findByArticle(id);
        if (nurt.isPresent()) {
            return nurt.get();
        }
        Nutritions res_nutr = new Nutritions(goodRep.findByArticle(id).get(),
                0f, 0f, 0f, 0f, 0f, 0f);
        return res_nutr;
    }

    public void deleteFromOGS(List<Integer> ids) {
        Iterable<OrderGoodStructs> ogs = OGSRep.findAll();
        for (OrderGoodStructs ogsOne : ogs) {
            if (ids.contains(ogsOne.getArticle().getArticle())) {
                OGSRep.delete(ogsOne);
            }
        }
    }

}
