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

    public void insertTypes(String types, String urls) {
        String[] itemsData = types.split(",");
        String[] urlsData = urls.split(",");
        List<GoodTypes> res = new ArrayList<GoodTypes>();
        List<String> all = goodTypesRep.findAllName();
        if (urlsData.length == 1) {
            for (String item : itemsData) {
                if (!all.contains(item)) {
                    res.add(new GoodTypes(item, urls));
                }
            }
        } else if (itemsData.length == urlsData.length) {
            for (int i = 0; i < itemsData.length; ++i) {
                if (!all.contains(itemsData[i])) {
                    res.add(new GoodTypes(itemsData[i], urlsData[i]));
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
        String[] oldNamesList = oldNames.split(",");
        String[] newNamesList = newNames.split(",");
        String[] newUrls = urls.split(",");
        List<String> allIngsNames = goodTypesRep.findAllName();
        List<GoodTypes> willUpdate = goodTypesRep.findAllByNameIn(Arrays.stream(oldNamesList).toList());

        if (willUpdate != null) {
            if (willUpdate.size() == newNamesList.length) {
                if (newUrls.length == 1) {
                    for (int i = 0; i < newNamesList.length; ++i) {
                        if (!allIngsNames.contains(newNamesList[i])) {
                            GoodTypes item = willUpdate.get(i);
                            item.setName(newNamesList[i]);
                            item.setUrl(urls);
                            willUpdate.set(i, item);
                        }
                    }
                } else {
                    for (int i = 0; i < newNamesList.length; ++i) {
                        if (!allIngsNames.contains(newNamesList[i])) {
                            GoodTypes item = willUpdate.get(i);
                            item.setName(newNamesList[i]);
                            item.setUrl(newUrls[i]);
                            willUpdate.set(i, item);
                        }
                    }
                }
            } else if (willUpdate.size() == newUrls.length) {
                for (int i = 0; i < newUrls.length; ++i) {
                    GoodTypes item = willUpdate.get(i);
                    item.setUrl(newUrls[i]);
                    willUpdate.set(i, item);
                }
            } else if (newUrls.length == 1) {
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
        String[] oldNamesList = oldNames.split(",");
        String[] newNamesList = newNames.split(",");
        String[] newParents = newParentIds.split(",");
        List<String> allNames = goodTypesRep.findAllName();
        List<GoodSubtypes> willUpdate = goodSubtypesRep.findAllByNameIn(Arrays.stream(oldNamesList).toList());
        List<GoodTypes> parents = goodTypesRep.findAllByNameIn(Arrays.stream(newParents).toList());
        if (willUpdate != null) {
            if (willUpdate.size() == newNamesList.length) {
                if (newParents.length == 1) {
                    for (int i = 0; i < newNamesList.length; ++i) {
                        if (!allNames.contains(newNamesList[i])) {
                            GoodSubtypes item = willUpdate.get(i);
                            item.setName(newNamesList[i]);
                            item.setParent(parents.get(0));
                            willUpdate.set(i, item);
                        }
                    }
                } else if (newParents.length == 0) {
                    for (int i = 0; i < newNamesList.length; ++i) {
                        if (!allNames.contains(newNamesList[i])) {
                            GoodSubtypes item = willUpdate.get(i);
                            item.setName(newNamesList[i]);
                            willUpdate.set(i, item);
                        }
                    }
                } else {
                    for (int i = 0; i < newNamesList.length; ++i) {
                        if (!allNames.contains(newNamesList[i])) {
                            GoodSubtypes item = willUpdate.get(i);
                            item.setName(newNamesList[i]);
                            item.setParent(parents.get(i));
                            willUpdate.set(i, item);
                        }
                    }
                }
            } else if (willUpdate.size() == newParents.length) {
                for (int i = 0; i < newParents.length; ++i) {
                    GoodSubtypes item = willUpdate.get(i);
                    item.setParent(parents.get(i));
                    willUpdate.set(i, item);
                }
            } else if (newParents.length == 1) {
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
        String[] listNames = names.split(",");
        List<GoodTypes> willUpdateIngs = goodTypesRep.findAllByNameIn(Arrays.stream(listNames).toList());
        if (willUpdateIngs != null) {
            List<Integer> ids = new ArrayList<>();
            for (GoodTypes elem : willUpdateIngs) {
                ids.add(elem.getId());
            }
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
        String[] listNames = names.split(",");
        List<GoodSubtypes> willDeleted = goodSubtypesRep.findAllByNameIn(Arrays.stream(listNames).toList());
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
