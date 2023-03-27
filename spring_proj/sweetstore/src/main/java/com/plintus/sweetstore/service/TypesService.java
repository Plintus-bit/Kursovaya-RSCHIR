package com.plintus.sweetstore.service;

import com.plintus.sweetstore.domain.*;
import com.plintus.sweetstore.repos.GoodRepository;
import com.plintus.sweetstore.repos.GoodSubtypesRepository;
import com.plintus.sweetstore.repos.GoodTypesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TypesService {
    @Autowired
    private GoodTypesRepository goodTypesRep;
    @Autowired
    private GoodSubtypesRepository goodSubtypesRep;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodRepository goodRep;

    public Iterable<GoodTypes> getAllTypes() {
        return goodTypesRep.findAll();
    }
    public Iterable<GoodSubtypes> getAllSubtypes() {
        return  goodSubtypesRep.findAll();
    }

    public GoodTypes getTypeById(Integer id) {
        return goodTypesRep.findById(id).get();
    }

    public void insertSubtypes(String subtypes, String parentType) {
        List<String> subtypesData = UtilService.getStringListFromStringData(subtypes);
        List<String> parentTypeData = UtilService.getStringListFromStringData(parentType);
        List<String> allNames = goodSubtypesRep.findAllName();
        List<GoodSubtypes> subtypesToInsert = new ArrayList<>();
        if (parentTypeData.size() == 1) {
            GoodTypes parent = goodTypesRep.findByName(parentType).get(0);
            if (parent != null) {
                for (String item : subtypesData) {
                    if (!allNames.contains(item)) {
                        subtypesToInsert.add(new GoodSubtypes(item, parent));
                    }
                }
            }
        } else if (parentTypeData.size() == subtypesData.size()) {
            for (int i = 0; i < subtypesData.size(); ++i) {
                if (!allNames.contains(subtypesData.get(i))) {
                    subtypesToInsert.add(
                            new GoodSubtypes(subtypesData.get(i),
                                    goodTypesRep.findByName(parentTypeData.get(i)).get(0)));
                }
            }
        }
        goodSubtypesRep.saveAll(subtypesToInsert);
    }
    public void insertTypes(String types, String urls) {
        List<String> itemsData = UtilService.getStringListFromStringData(types);
        List<String> urlsData = UtilService.getStringListFromStringData(urls)
;       List<GoodTypes> res = new ArrayList<GoodTypes>();
        List<String> all = goodTypesRep.findAllName();
        if (urlsData.size() == 1) {
            for (String item : itemsData) {
                if (!all.contains(item)) {
                    res.add(new GoodTypes(item, urls));
                }
            }
        } else if (itemsData.size() == urlsData.size()) {
            for (int i = 0; i < itemsData.size(); ++i) {
                if (!all.contains(itemsData.get(i))) {
                    res.add(new GoodTypes(itemsData.get(i), urlsData.get(i)));
                }
            }
        }
        goodTypesRep.saveAll(res);
    }

    private void deleteFromSubtypes(List<Integer> ids) {
        Iterable<GoodSubtypes> subtypes = goodSubtypesRep.findAll();
        for (GoodSubtypes subtype : subtypes) {
            if (ids.contains(subtype.getParent().getId())) {
                goodSubtypesRep.delete(subtype);
            }
        }
    }

    public void updateTypes(String oldNames,
                            String newNames,
                            String urls) {
        List<String> oldNamesList = UtilService.getStringListFromStringData(oldNames);
        List<String> newNamesList = UtilService.getStringListFromStringData(newNames);
        List<String> newUrls = UtilService.getStringListFromStringData(urls);
        List<String> allIngsNames = goodTypesRep.findAllName();
        List<GoodTypes> willUpdate = goodTypesRep.findAllByNameIn(oldNamesList);

        if (willUpdate != null) {
            if (willUpdate.size() == newNamesList.size()) {
                if (newUrls.size() == 1) {
                    for (int i = 0; i < newNamesList.size(); ++i) {
                        if (!allIngsNames.contains(newNamesList.get(i))) {
                            GoodTypes item = willUpdate.get(i);
                            item.setName(newNamesList.get(i));
                            item.setUrl(urls);
                            willUpdate.set(i, item);
                        }
                    }
                } else {
                    for (int i = 0; i < newNamesList.size(); ++i) {
                        if (!allIngsNames.contains(newNamesList.get(i))) {
                            GoodTypes item = willUpdate.get(i);
                            item.setName(newNamesList.get(i));
                            item.setUrl(newUrls.get(i));
                            willUpdate.set(i, item);
                        }
                    }
                }
            } else if (willUpdate.size() == newUrls.size()) {
                for (int i = 0; i < newUrls.size(); ++i) {
                    GoodTypes item = willUpdate.get(i);
                    item.setUrl(newUrls.get(i));
                    willUpdate.set(i, item);
                }
            } else if (newUrls.size() == 1) {
                for (int i = 0; i < willUpdate.size(); ++i) {
                    GoodTypes item = willUpdate.get(i);
                    item.setUrl(urls);
                    willUpdate.set(i, item);
                }
            }
            goodTypesRep.saveAll(willUpdate);
        }
    }

    public void updateSubtypes(String oldNames,
                               String newNames,
                               String newParentIds) {
        List<String> oldNamesList = UtilService.getStringListFromStringData(oldNames);
        List<String> newNamesList = UtilService.getStringListFromStringData(newNames);
        List<String> newParents = UtilService.getStringListFromStringData(newParentIds);
        List<String> allNames = goodTypesRep.findAllName();
        List<GoodSubtypes> willUpdate = goodSubtypesRep.findAllByNameIn(oldNamesList);
        List<GoodTypes> parents = goodTypesRep.findAllByNameIn(newParents);
        if (willUpdate != null) {
            if (willUpdate.size() == newNamesList.size()) {
                if (newParents.size() == 1) {
                    for (int i = 0; i < newNamesList.size(); ++i) {
                        if (!allNames.contains(newNamesList.get(i))) {
                            GoodSubtypes item = willUpdate.get(i);
                            item.setName(newNamesList.get(i));
                            item.setParent(parents.get(0));
                            willUpdate.set(i, item);
                        }
                    }
                } else if (newParents.size() == 0) {
                    for (int i = 0; i < newNamesList.size(); ++i) {
                        if (!allNames.contains(newNamesList.get(i))) {
                            GoodSubtypes item = willUpdate.get(i);
                            item.setName(newNamesList.get(i));
                            willUpdate.set(i, item);
                        }
                    }
                } else {
                    for (int i = 0; i < newNamesList.size(); ++i) {
                        if (!allNames.contains(newNamesList.get(i))) {
                            GoodSubtypes item = willUpdate.get(i);
                            item.setName(newNamesList.get(i));
                            item.setParent(parents.get(i));
                            willUpdate.set(i, item);
                        }
                    }
                }
            } else if (willUpdate.size() == newParents.size()) {
                for (int i = 0; i < newParents.size(); ++i) {
                    GoodSubtypes item = willUpdate.get(i);
                    item.setParent(parents.get(i));
                    willUpdate.set(i, item);
                }
            } else if (newParents.size() == 1) {
                for (int i = 0; i < willUpdate.size(); ++i) {
                    GoodSubtypes item = willUpdate.get(i);
                    item.setParent(parents.get(0));
                    willUpdate.set(i, item);
                }
            }
            goodSubtypesRep.saveAll(willUpdate);
        }
    }


    public void deleteTypes(String names) {
        List<String> listNames = UtilService.getStringListFromStringData(names);
        List<GoodTypes> willUpdateIngs = goodTypesRep.findAllByNameIn(listNames);
        if (willUpdateIngs != null) {
            List<Integer> ids = new ArrayList<>();
            for (GoodTypes elem : willUpdateIngs) {
                ids.add(elem.getId());
            }
            deleteFromSubtypes(ids);
            goodTypesRep.deleteAll(willUpdateIngs);
        }
    }
    public void deleteFromGoods(List<Integer> ids) {
        Iterable<Goods> goods = goodRep.findAll();
        List<Goods> willDeleted = new ArrayList<>();
        List<Integer> goodIds = new ArrayList<>();
        for (Goods good : goods) {
            if (ids.contains(good.getSubtype().getId())) {
                willDeleted.add(good);
                goodIds.add(good.getArticle());
            }
        }
        goodsService.deleteFromOGS(goodIds);
        goodRep.deleteAll(willDeleted);
    }

    public void deleteSubtypes(String names) {
        List<String> listNames = UtilService.getStringListFromStringData(names);
        List<GoodSubtypes> willDeleted = goodSubtypesRep.findAllByNameIn(listNames);
        if (willDeleted != null) {
            List<Integer> ids = new ArrayList<>();
            for (GoodSubtypes elem : willDeleted) {
                ids.add(elem.getId());
            }
            deleteFromGoods(ids);
            goodSubtypesRep.deleteAll(willDeleted);
        }
    }
}
