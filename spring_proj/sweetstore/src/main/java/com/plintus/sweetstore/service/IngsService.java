package com.plintus.sweetstore.service;

import com.plintus.sweetstore.domain.Goods;
import com.plintus.sweetstore.domain.IngStructures;
import com.plintus.sweetstore.domain.Ingredients;
import com.plintus.sweetstore.domain.Nutritions;
import com.plintus.sweetstore.repos.GoodRepository;
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
    @Autowired
    private GoodRepository goodRep;

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

    public Iterable<IngStructures> getAllIngsInGoods() {
        return ingStructRep.findAll();
    }

    public void insertIngsStructs(String articles, String ings) {
        List<String> ingsStructs = UtilService.getStringListFromStringData(ings, ";");
        List<Integer> articleList = UtilService.getIntegerListFromStringList(
                UtilService.getStringListFromStringData(articles));
        List<Ingredients> allIngs = ingsRep.findAllIngs();
        boolean hasOneStruct = ingsStructs.size() == 1;

        if ((articleList.size() == ingsStructs.size()) || hasOneStruct) {
            List<IngStructures> willInserted = new ArrayList<>();
            if (hasOneStruct) {
                List<String> ingNames = UtilService.getStringListFromStringData(ingsStructs.get(0));
                List<Ingredients> foundIngs = ingsRep.findAllByNameIn(ingNames);
                for (Integer integer : articleList) {
                    Goods curGood = goodRep.findByArticle(integer).get();
                    List<Integer> ingIdsForGood = ingStructRep.findIngIdByGoodId(curGood.getId());
                    for (Ingredients ing : foundIngs) {
                        if (allIngs.contains(ing) && !ingIdsForGood.contains(ing.getId())) {
                            IngStructures tempIngStr = new IngStructures();
                            tempIngStr.setIngId(ing);
                            tempIngStr.setGoodId(curGood);
                            willInserted.add(tempIngStr);
                        }
                    }
                    ingStructRep.saveAll(willInserted);
                    willInserted.clear();
                }
            } else {
                for (int i = 0; i < articleList.size(); ++i) {
                    List<String> ingNames = UtilService.getStringListFromStringData(ingsStructs.get(i));
                    List<Ingredients> foundIngs = ingsRep.findAllByNameIn(ingNames);
                    Goods curGood = goodRep.findByArticle(articleList.get(i)).get();
                    List<Integer> ingIdsForGood = ingStructRep.findIngIdByGoodId(curGood.getId());
                    for (Ingredients ing : foundIngs) {
                        if (allIngs.contains(ing) && !ingIdsForGood.contains(ing.getId())) {
                            IngStructures tempIngStr = new IngStructures();
                            tempIngStr.setIngId(ing);
                            tempIngStr.setGoodId(curGood);
                            willInserted.add(tempIngStr);
                        }
                    }
                    ingStructRep.saveAll(willInserted);
                    willInserted.clear();
                }
            }
        }
    }

    public void deleteIngsStructs(String articles, String ings) {
        List<Integer> articleList = UtilService.getIntegerListFromStringList(
                UtilService.getStringListFromStringData(articles));
        List<Integer> goodIds = goodRep.findAllIdByArticleIn(articleList);
        boolean hasIngs = ings != null && !ings.equals("");

        List<IngStructures> willDeleted;
        if (hasIngs) {
            willDeleted = new ArrayList<>();
            List<IngStructures> forIngsDelete = ingStructRep.findAllByGoodId(goodIds);
            List<String> ingsWillDeleted = UtilService.getStringListFromStringData(ings);
            for (IngStructures item : forIngsDelete) {
                if (ingsWillDeleted.contains(item.getIngId().getName())) {
                    willDeleted.add(item);
                }
            }
        } else {
            willDeleted = ingStructRep.findAllByGoodId(goodIds);
        }
        if (willDeleted != null && willDeleted.size() > 0) {
            ingStructRep.deleteAll(willDeleted);
        }
    }
}
