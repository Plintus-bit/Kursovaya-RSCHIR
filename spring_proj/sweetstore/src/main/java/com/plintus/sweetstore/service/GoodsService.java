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

    public List<Integer> getIntegerArrayFromStringArray(String[] array) {
        List<Integer> resultArray = new ArrayList<>();
        for (String item : array) {
            resultArray.add(Integer.parseInt(item));
        }
        return resultArray;
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

    public void deleteFromIngStructs(List<Integer> ids) {
        Iterable<IngStructures> ings = ingStructRep.findAll();
        for (IngStructures ing : ings) {
            if (ids.contains(ing.getArticle().getArticle())) {
                ingStructRep.delete(ing);
            }
        }
    }

    public String[] getStringArrayFromStringData(String data, String separator) {
        return data.split(";");
    }

    public String[] getStringArrayFromStringData(String data) {
        return data.split(",");
    }

    public void addGoods(String articles,
                         String names,
                         String descripts,
                         String subtypes,
                         String costs,
                         String counts,
                         String urls) {
        List<Integer> newArticles = getIntegerArrayFromStringArray(
                getStringArrayFromStringData(articles));
        if (newArticles.size() != 0) {
            List<Integer> allGoodsArticles = goodRep.findAllArticle();
            String[] newNames = getStringArrayFromStringData(names);
            String[] newDescripts = getStringArrayFromStringData(descripts, ";");
            String[] newSubtypes = getStringArrayFromStringData(subtypes);
            List<GoodSubtypes> newSubtypeObjs = goodSubtypeRep.findAllByNameIn(Arrays.stream(newSubtypes).toList());
            List<Integer> newCosts = getIntegerArrayFromStringArray(
                    getStringArrayFromStringData(costs));
            List<Integer> newCounts = getIntegerArrayFromStringArray(
                    getStringArrayFromStringData(counts));
            String[] newUrls = getStringArrayFromStringData(urls);

            // Проверка совпадения размеров
            boolean hasOneUrl = newUrls.length == 1;
            boolean hasOneSubtype = newSubtypes.length == 1;
            boolean isWillUpdated = newArticles.size() == newCosts.size();
            isWillUpdated = isWillUpdated && (newArticles.size() == newNames.length);
            isWillUpdated = isWillUpdated && (newCounts.size() == newNames.length);
            isWillUpdated = isWillUpdated && (newDescripts.length == newNames.length);
            isWillUpdated = isWillUpdated && ((newUrls.length == newNames.length) || hasOneUrl);
            isWillUpdated = isWillUpdated && ((newSubtypes.length == newNames.length) || hasOneSubtype);
            if (isWillUpdated) {
                List<Goods> willInserted = new ArrayList<>();
                for (int i = 0; i < newArticles.size(); ++i) {
                    if (!allGoodsArticles.contains(newArticles.get(i))) {
                        Goods item = new Goods();
                        item.setArticle(newArticles.get(i));
                        item.setCost(newCosts.get(i));
                        item.setCount(newCounts.get(i));
                        item.setName(newNames[i]);
                        item.setDescript(newDescripts[i]);
                        item.setSubtype(hasOneSubtype ? newSubtypeObjs.get(0) : newSubtypeObjs.get(i));
                        item.setUrl(hasOneUrl ? urls : newUrls[i]);
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
        List<Integer> newArticles = getIntegerArrayFromStringArray(
                getStringArrayFromStringData(articles));
        if (newArticles.size() != 0) {
            List<Integer> allGoodsArticles = goodRep.findAllArticle();
            String[] newNames = getStringArrayFromStringData(names);
            String[] newDescripts = getStringArrayFromStringData(descripts, ";");
            String[] newSubtypes = getStringArrayFromStringData(subtypes);
            List<GoodSubtypes> newSubtypeObjs = goodSubtypeRep.findAllByNameIn(Arrays.stream(newSubtypes).toList());
            List<Integer> newCosts = getIntegerArrayFromStringArray(
                    getStringArrayFromStringData(costs));
            List<Integer> newCounts = getIntegerArrayFromStringArray(
                    getStringArrayFromStringData(counts));
            String[] newUrls = getStringArrayFromStringData(urls);
            // Проверка совпадения размеров
            boolean hasOneUrl = newUrls.length == 1;
            boolean hasOneSubtype = newSubtypes.length == 1;
            List<Goods> willUpdated = goodRep.findAllByArticleIn(
                    getIntegerArrayFromStringArray(
                            getStringArrayFromStringData(oldArticles)));
            Integer length = willUpdated != null ? willUpdated.size() : 0;
            if (length > 0) {
                for (int i = 0; i < length; ++i) {
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
                    if (newNames.length == length) {
                        item.setName(newNames[i]);
                    }
                    if (newDescripts.length == length) {
                        item.setDescript(newDescripts[i]);
                    }
                    if (newSubtypeObjs.size() == length) {
                        item.setSubtype(newSubtypeObjs.get(i));
                    } else if (hasOneSubtype) {
                        item.setSubtype(newSubtypeObjs.get(0));
                    }
                    if (newUrls.length == length) {
                        item.setUrl(newUrls[i]);
                    } else if (hasOneUrl) {
                        item.setUrl(urls);
                    }
                    willUpdated.set(i, item);
                }
                goodRep.saveAll(willUpdated);
            }
        }
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
