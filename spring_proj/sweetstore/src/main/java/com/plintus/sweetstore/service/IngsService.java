package com.plintus.sweetstore.service;

import com.plintus.sweetstore.domain.IngStructures;
import com.plintus.sweetstore.domain.Ingredients;
import com.plintus.sweetstore.repos.IngStructuresRepository;
import com.plintus.sweetstore.repos.IngredientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class IngsService {
    @Autowired
    private IngStructuresRepository ingStructRep;
    @Autowired
    private IngredientsRepository ingsRep;

    public Iterable<Ingredients> getAllIngredients() {
        return ingsRep.findAll();
    }

    public void insertIngredients(String ings) {
        String[] ingsData = ings.split(",");
        List<Ingredients> resIngs = new ArrayList<Ingredients>();
        List<String> allIngs = ingsRep.findAllName();
        for (String ing : ingsData) {
            if (!allIngs.contains(ing)) {
                resIngs.add(new Ingredients(ing));
            }
        }
        ingsRep.saveAll(resIngs);
    }

    public void updateIngredients(String oldNames, String newNames) {
        String[] oldIngNames = oldNames.split(",");
        String[] newIngNames = newNames.split(",");
        List<String> allIngsNames = ingsRep.findAllName();
        List<Ingredients> willUpdateIngs = ingsRep.findAllByNameIn(Arrays.stream(oldIngNames).toList());
        if (willUpdateIngs != null && willUpdateIngs.size() == newIngNames.length) {
            for (int i = 0; i < newIngNames.length; ++i) {
                if (!allIngsNames.contains(newIngNames[i])) {
                    Ingredients ing = willUpdateIngs.get(i);
                    ing.setName(newIngNames[i]);
                    willUpdateIngs.set(i, ing);
                }
            }
            ingsRep.saveAll(willUpdateIngs);
        }
    }

    private void deleteFromIngStructure(List<Integer> ingIds) {
        Iterable<IngStructures> ingsInGoods = ingStructRep.findAll();
        for (IngStructures ingStruct : ingsInGoods) {
            if (ingIds.contains(ingStruct.getIngId().getId())) {
                ingStructRep.delete(ingStruct);
            }
        }
    }

    public void deleteIngredients(String names) {
        String[] listNames = names.split(",");
        List<Ingredients> willUpdateIngs = ingsRep.findAllByNameIn(Arrays.stream(listNames).toList());
        if (willUpdateIngs != null) {
            List<Integer> ingIds = new ArrayList<>();
            for (Ingredients ing : willUpdateIngs) {
                ingIds.add(ing.getId());
            }
            deleteFromIngStructure(ingIds);
            ingsRep.deleteAll(willUpdateIngs);
        }
    }
}
