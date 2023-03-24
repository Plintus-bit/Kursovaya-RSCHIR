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

    public List<Integer> getIntegerListFromStringList(List<String> array) {
        List<Integer> resultArray = new ArrayList<>();
        for (String item : array) {
            if (!Objects.equals(item, "")) {
                resultArray.add(Integer.parseInt(item));
            }
        }
        return resultArray;
    }

    public List<String> getStringListFromStringData(String data, String separator) {
        List<String> resultData = new ArrayList<>();
        if (data.contains(separator)) {
            resultData.addAll(Arrays.stream(data.split(separator)).toList());
        } else if (!data.equals("")){
            resultData.add(data);
        }
        return resultData;
    }

    public List<String> getStringListFromStringData(String data) {
        return getStringListFromStringData(data, ",");
    }

    public void addGoods(String articles,
                         String names,
                         String descripts,
                         String subtypes,
                         String costs,
                         String counts,
                         String urls) {
        List<Integer> newArticles = getIntegerListFromStringList(
                getStringListFromStringData(articles)
        );
        if (newArticles.size() != 0) {
            List<Integer> allGoodsArticles = goodRep.findAllArticle();
            List<String> newNames = getStringListFromStringData(names);
            List<String> newDescripts = getStringListFromStringData(descripts, ";");
            List<String> newSubtypes = getStringListFromStringData(subtypes);
            List<GoodSubtypes> newSubtypeObjs = goodSubtypeRep.findAllByNameIn(newSubtypes);
            List<Integer> newCosts = getIntegerListFromStringList(
                    getStringListFromStringData(costs));
            List<Integer> newCounts = getIntegerListFromStringList(
                    getStringListFromStringData(counts));
            List<String> newUrls = getStringListFromStringData(urls);

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
                getIntegerListFromStringList(
                        getStringListFromStringData(oldArticles)));
        int length = willUpdated != null ? willUpdated.size() : 0;
        if (length > 0) {
            List<Integer> allGoodsArticles = goodRep.findAllArticle();
            List<String> newNames = getStringListFromStringData(names);
            List<String> newDescripts = getStringListFromStringData(descripts, ";");
            List<String> newSubtypes = getStringListFromStringData(subtypes);
            List<GoodSubtypes> newSubtypeObjs = goodSubtypeRep.findAllByNameIn(newSubtypes);
            List<Integer> newCosts = getIntegerListFromStringList(
                    getStringListFromStringData(costs));
            List<Integer> newCounts = getIntegerListFromStringList(
                    getStringListFromStringData(counts));
            List<String> newUrls = getStringListFromStringData(urls);
            // Проверка совпадения размеров
            boolean hasOneUrl = newUrls.size() == 1;
            boolean hasOneSubtype = newSubtypes.size() == 1;
            List<Integer> newArticles = getIntegerListFromStringList(
                    getStringListFromStringData(articles));
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

}
