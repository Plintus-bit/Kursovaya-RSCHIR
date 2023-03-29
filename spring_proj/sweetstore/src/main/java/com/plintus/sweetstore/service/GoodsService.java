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
    private UserOrdersRepository UORep;
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
    @Autowired
    private OrderStatusesRepository OStRep;

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
        return goodRep.findById(id).get();
    }

    public List<String> getGoodIngsNamesByGoodId(Integer id) {
        List<Integer> ingsIn_good = ingStructRep.findIngIdByGoodId(id);
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

    public Iterable<Goods> getAllGoods() {
        return goodRep.findAll();
    }

    public Nutritions getGoodNutrByGoodId(Integer id) {
        Optional<Nutritions> nurt = nutrRep.findByGoodId(id);
        if (nurt.isPresent()) {
            return nurt.get();
        }
        Nutritions res_nutr = new Nutritions(goodRep.findById(id).get(),
                0f, 0f, 0f, 0f, 0f, 0f);
        return res_nutr;
    }

    public void deleteFromOGS(List<Integer> ids) {
        Iterable<OrderGoodStructs> ogs = OGSRep.findAll();
        for (OrderGoodStructs ogsOne : ogs) {
            if (ids.contains(ogsOne.getGoodId().getArticle())) {
                OGSRep.delete(ogsOne);
            }
        }
    }

    public void deleteFromIngStructs(List<Integer> ids) {
        Iterable<IngStructures> ings = ingStructRep.findAll();
        for (IngStructures ing : ings) {
            if (ids.contains(ing.getGoodId().getArticle())) {
                ingStructRep.delete(ing);
            }
        }
    }

    public void insertGoods(String articles,
                         String names,
                         String descripts,
                         String subtypes,
                         String costs,
                         String counts,
                         String urls) {
        List<Integer> newArticles = UtilService.getIntegerListFromStringList(
                UtilService.getStringListFromStringData(articles)
        );
        if (newArticles.size() != 0) {
            List<Integer> allGoodsArticles = goodRep.findAllArticle();
            List<String> newNames = UtilService.getStringListFromStringData(names);
            List<String> newDescripts = UtilService.getStringListFromStringData(descripts, ";");
            List<String> newSubtypes = UtilService.getStringListFromStringData(subtypes);
            List<GoodSubtypes> newSubtypeObjs = goodSubtypeRep.findAllByNameIn(newSubtypes);
            List<Integer> newCosts = UtilService.getIntegerListFromStringList(
                    UtilService.getStringListFromStringData(costs));
            List<Integer> newCounts = UtilService.getIntegerListFromStringList(
                    UtilService.getStringListFromStringData(counts));
            List<String> newUrls = UtilService.getStringListFromStringData(urls);

            // Проверка совпадения размеров
            boolean hasOneUrl = newUrls.size() == 1;
            boolean hasOneSubtype = newSubtypes.size() == 1;
            boolean isWillUpdated = newArticles.size() == newCosts.size();
            isWillUpdated = isWillUpdated && (newArticles.size() == newNames.size());
            isWillUpdated = isWillUpdated && (newCounts.size() == newNames.size());
            isWillUpdated = isWillUpdated && (newDescripts.size() == newNames.size());
            isWillUpdated = isWillUpdated && ((newUrls.size() == newNames.size()) || hasOneUrl);
            isWillUpdated = isWillUpdated && ((newSubtypes.size() == newNames.size()) || hasOneSubtype);
            if (isWillUpdated) {
                List<Goods> willInserted = new ArrayList<>();
                for (int i = 0; i < newArticles.size(); ++i) {
                    if (!allGoodsArticles.contains(newArticles.get(i))) {
                        Goods item = new Goods();
                        item.setArticle(newArticles.get(i));
                        item.setCost(newCosts.get(i));
                        item.setCount(newCounts.get(i));
                        item.setName(newNames.get(i));
                        item.setDescript(newDescripts.get(i));
                        item.setSubtype(hasOneSubtype ? newSubtypeObjs.get(0) : newSubtypeObjs.get(i));
                        item.setUrl(hasOneUrl ? urls : newUrls.get(i));
                        willInserted.add(item);
                    }
                }
                goodRep.saveAll(willInserted);
            }
        }
    }

    public void updateGoods(String oldArticles,
                            String articles,
                            String names,
                            String descripts,
                            String subtypes,
                            String costs,
                            String counts,
                            String urls) {
        List<Goods> willUpdated = goodRep.findAllByArticleIn(
                UtilService.getIntegerListFromStringList(
                        UtilService.getStringListFromStringData(oldArticles)));
        int length = willUpdated != null ? willUpdated.size() : 0;
        if (length > 0) {
            List<Integer> allGoodsArticles = goodRep.findAllArticle();
            List<String> newNames = UtilService.getStringListFromStringData(names);
            List<String> newDescripts = UtilService.getStringListFromStringData(descripts, ";");
            List<String> newSubtypes = UtilService.getStringListFromStringData(subtypes);
            List<GoodSubtypes> newSubtypeObjs = goodSubtypeRep.findAllByNameIn(newSubtypes);
            List<Integer> newCosts = UtilService.getIntegerListFromStringList(
                    UtilService.getStringListFromStringData(costs));
            List<Integer> newCounts = UtilService.getIntegerListFromStringList(
                    UtilService.getStringListFromStringData(counts));
            List<String> newUrls = UtilService.getStringListFromStringData(urls);
            // Проверка совпадения размеров
            boolean hasOneUrl = newUrls.size() == 1;
            boolean hasOneSubtype = newSubtypes.size() == 1;
            List<Integer> newArticles = UtilService.getIntegerListFromStringList(
                    UtilService.getStringListFromStringData(articles));
            for (int i = 0; i < length; ++i) {
                try {
                    Goods item = willUpdated.get(i);
                    if (newArticles.size() == length &&
                            !allGoodsArticles.contains(newArticles.get(i))) {
                        item.setArticle(newArticles.get(i));
                    }
                    if (newCosts.size() == length) {
                        item.setCost(newCosts.get(i));
                    }
                    if (newCounts.size() == length) {
                        item.setCount(newCounts.get(i));
                    }
                    if (newNames.size() == length) {
                        item.setName(newNames.get(i));
                    }
                    if (newDescripts.size() == length) {
                        item.setDescript(newDescripts.get(i));
                    }
                    if (newSubtypeObjs.size() == length) {
                        item.setSubtype(newSubtypeObjs.get(i));
                    } else if (hasOneSubtype) {
                        item.setSubtype(newSubtypeObjs.get(0));
                    }
                    if (newUrls.size() == length) {
                        item.setUrl(newUrls.get(i));
                    } else if (hasOneUrl) {
                        item.setUrl(urls);
                    }
                    willUpdated.set(i, item);
                } catch (Exception e) {
                    System.out.println(e.toString());
                    System.out.println(e.getMessage());
                }
            }
            goodRep.saveAll(willUpdated);
        }
    }

    public boolean updateGoodsCount(UserOrders order) {
        List<OrderGoodStructs> ogs = OGSRep.findAllByOrderId(order.getId());
        List<Goods> willBeSavedGoods = new ArrayList<>();
        for (OrderGoodStructs curOGS : ogs) {
            Goods curGood = curOGS.getGoodId();
            int newCount = curGood.getCount() - curOGS.getCount();
            if (newCount < 0) {
                return false;
            }
            curGood.setCount(newCount);
            willBeSavedGoods.add(curGood);
        }
        goodRep.saveAll(willBeSavedGoods);
        checkGoodsInCarts(willBeSavedGoods, order.getId());
        return true;
    }

    public void checkGoodsInCarts(List<Goods> wasUpdatedGoods, Integer orderIdToNotChange) {
        List<Integer> goodIds = new ArrayList<>();
        List<Integer> orderIds = UORep.findAllIdByStatusAndNotId(1, orderIdToNotChange);
        for (Goods g : wasUpdatedGoods) {
            goodIds.add(g.getId());
        }
        List<OrderGoodStructs> ogsToCheck = OGSRep.findByOrderIdInAndGoodIdIn(orderIds, goodIds);
        List<OrderGoodStructs> ogsToDelete = new ArrayList<>();
        for (OrderGoodStructs curOGS : ogsToCheck) {
            Integer newGoodsCount = curOGS.getGoodId().getCount();
            if (curOGS.getCount() > newGoodsCount) {
                ogsToDelete.add(curOGS);
            }
        }
        OGSRep.deleteAll(ogsToDelete);
    }

    public void deleteGoods(String articles) {
        String[] listArticles = articles.split(",");
        List<Integer> intListArticles = new ArrayList<>();
        for (String item : listArticles) {
            intListArticles.add(Integer.parseInt(item));
        }
        List<Goods> willUpdate = goodRep.findAllByArticleIn(intListArticles);
        if (willUpdate != null) {
            List<Integer> ids = new ArrayList<>();
            for (Goods elem : willUpdate) {
                ids.add(elem.getArticle());
            }
            deleteFromOGS(ids);
            deleteFromIngStructs(ids);
            goodRep.deleteAll(willUpdate);
        }
    }

    public Iterable<Nutritions> getAllKBJS() {
        return nutrRep.findAll();
    }

    public void insertNutrition(String articles, String caloric,
                                String fats, String proteins,
                                String diet_fiber, String carbhyd,
                                String water) {
        List<Integer> articlesList = UtilService.getIntegerListFromStringList(
                UtilService.getStringListFromStringData(articles));
        List<Float> caloricList = UtilService.getFloatListFromStringList(
                UtilService.getStringListFromStringData(caloric));
        List<Float> fatsList = UtilService.getFloatListFromStringList(
                UtilService.getStringListFromStringData(fats));
        List<Float> proteinsList = UtilService.getFloatListFromStringList(
                UtilService.getStringListFromStringData(proteins));
        List<Float> dietFiberList = UtilService.getFloatListFromStringList(
                UtilService.getStringListFromStringData(diet_fiber));
        List<Float> carbhydList = UtilService.getFloatListFromStringList(
                UtilService.getStringListFromStringData(carbhyd));
        List<Float> waterList = UtilService.getFloatListFromStringList(
                UtilService.getStringListFromStringData(water));

        boolean hasOneCaloric = caloricList.size() == 1;
        boolean hasOneProteins = proteinsList.size() == 1;
        boolean hasOneFats = fatsList.size() == 1;
        boolean hasOneCarbhyd = carbhydList.size() == 1;
        boolean hasOneDietFiber = dietFiberList.size() == 1;
        boolean hasOneWater = waterList.size() == 1;

        int maxSize = articlesList.size();

        boolean needToInsert = maxSize == caloricList.size() || hasOneCaloric;
        needToInsert = needToInsert && (maxSize == fatsList.size() || hasOneFats);
        needToInsert = needToInsert && (maxSize == proteinsList.size() || hasOneProteins);
        needToInsert = needToInsert && (maxSize == dietFiberList.size() || hasOneDietFiber);
        needToInsert = needToInsert && (maxSize == waterList.size() || hasOneWater);
        needToInsert = needToInsert && (maxSize == carbhydList.size() || hasOneCarbhyd);

        if (needToInsert) {
            List<Integer> allGoodIdInNutr = nutrRep.findAllGoodId();
            List<Integer> allInsertedGoodIds = goodRep.findAllIdByArticleIn(articlesList);
            List<Integer> allGoodIds = goodRep.findAllId();

            List<Nutritions> willInsertedNutrs = new ArrayList<>();
            for (int i = 0; i < allInsertedGoodIds.size(); ++i) {
                Integer tempId = allInsertedGoodIds.get(i);
                if (!allGoodIdInNutr.contains(tempId)
                        && allGoodIds.contains(tempId)) {
                    Nutritions tempNutr = new Nutritions();
                    tempNutr.setGoodId(goodRep.findById(tempId).get());
                    tempNutr.setCaloric(hasOneCaloric ? caloricList.get(0) : caloricList.get(i));
                    tempNutr.setFats(hasOneFats ? fatsList.get(0) : fatsList.get(i));
                    tempNutr.setProteins(hasOneProteins ? proteinsList.get(0) : proteinsList.get(i));
                    tempNutr.setDiet_fiber(hasOneDietFiber ? dietFiberList.get(0) : dietFiberList.get(i));
                    tempNutr.setWater(hasOneWater ? waterList.get(0) : waterList.get(i));
                    tempNutr.setCarbhyd(hasOneCarbhyd ? carbhydList.get(0) : carbhydList.get(i));
                    willInsertedNutrs.add(tempNutr);
                }
            }
            nutrRep.saveAll(willInsertedNutrs);
        }
    }

    public void updateNutrition(String articlesToUpd, String caloric,
                                String fats, String proteins,
                                String dietFiber, String carbhyd,
                                String water) {
        List<Integer> articlesList = UtilService.getIntegerListFromStringList(
                UtilService.getStringListFromStringData(articlesToUpd));
        List<Float> caloricList = UtilService.getFloatListFromStringList(
                UtilService.getStringListFromStringData(caloric));
        List<Float> fatsList = UtilService.getFloatListFromStringList(
                UtilService.getStringListFromStringData(fats));
        List<Float> proteinsList = UtilService.getFloatListFromStringList(
                UtilService.getStringListFromStringData(proteins));
        List<Float> dietFiberList = UtilService.getFloatListFromStringList(
                UtilService.getStringListFromStringData(dietFiber));
        List<Float> carbhydList = UtilService.getFloatListFromStringList(
                UtilService.getStringListFromStringData(carbhyd));
        List<Float> waterList = UtilService.getFloatListFromStringList(
                UtilService.getStringListFromStringData(water));

        boolean hasCaloric = caloricList.size() > 0;
        boolean hasProteins = proteinsList.size() > 0;
        boolean hasFats = fatsList.size() > 0;
        boolean hasCarbhyd = carbhydList.size() > 0;
        boolean hasDietFiber = dietFiberList.size() > 0;
        boolean hasWater = waterList.size() > 0;

        boolean hasOneCaloric = caloricList.size() == 1;
        boolean hasOneProteins = proteinsList.size() == 1;
        boolean hasOneFats = fatsList.size() == 1;
        boolean hasOneCarbhyd = carbhydList.size() == 1;
        boolean hasOneDietFiber = dietFiberList.size() == 1;
        boolean hasOneWater = waterList.size() == 1;

        int maxSize = articlesList.size();

        boolean needToUpdate = maxSize == caloricList.size() || !hasCaloric || hasOneCaloric;
        needToUpdate = needToUpdate && (maxSize == fatsList.size() || !hasFats || hasOneFats);
        needToUpdate = needToUpdate && (maxSize == proteinsList.size() || !hasProteins || hasOneProteins);
        needToUpdate = needToUpdate && (maxSize == dietFiberList.size() || !hasDietFiber || hasOneDietFiber);
        needToUpdate = needToUpdate && (maxSize == waterList.size() || !hasWater || hasOneWater);
        needToUpdate = needToUpdate && (maxSize == carbhydList.size() || !hasCarbhyd || hasOneCarbhyd);

        if (needToUpdate) {
            List<Integer> goodIdsInNutr = goodRep.findAllIdByArticleIn(articlesList);
            List<Nutritions> willUpdatedNutrs = nutrRep.findAllByGoodIdIn(goodIdsInNutr);
            for (int i = 0; i < willUpdatedNutrs.size(); ++i) {
                Nutritions tempNutr = willUpdatedNutrs.get(i);
                if (hasOneCaloric) {
                    tempNutr.setCaloric(caloricList.get(0));
                } else if (hasCaloric) {
                    tempNutr.setCaloric(caloricList.get(i));
                }
                if (hasOneCarbhyd) {
                    tempNutr.setCarbhyd(carbhydList.get(0));
                } else if (hasCarbhyd) {
                    tempNutr.setCarbhyd(carbhydList.get(i));
                }
                if (hasOneFats) {
                    tempNutr.setFats(fatsList.get(0));
                }
                else if (hasFats) {
                    tempNutr.setFats(fatsList.get(i));
                }
                if (hasOneDietFiber) {
                    tempNutr.setDiet_fiber(dietFiberList.get(0));
                } else if (hasDietFiber) {
                    tempNutr.setDiet_fiber(dietFiberList.get(i));
                }
                if (hasOneProteins) {
                    tempNutr.setProteins(proteinsList.get(0));
                } else if (hasProteins) {
                    tempNutr.setProteins(proteinsList.get(i));
                }
                if (hasOneWater) {
                    tempNutr.setWater(waterList.get(0));
                } else if (hasWater) {
                    tempNutr.setWater(waterList.get(i));
                }
                willUpdatedNutrs.set(i, tempNutr);
            }
            nutrRep.saveAll(willUpdatedNutrs);
        }
    }

    public void deleteNutrition(String articles) {
        List<Integer> articlesList = UtilService.getIntegerListFromStringList(
                UtilService.getStringListFromStringData(articles));
        List<Integer> idsToDelete = goodRep.findAllIdByArticleIn(articlesList);
        List<Nutritions> willDeleted = nutrRep.findAllByGoodIdIn(idsToDelete);
        if (willDeleted.size() > 0) {
            nutrRep.deleteAll(willDeleted);
        }
    }
}
