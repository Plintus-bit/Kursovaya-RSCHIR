package com.plintus.sweetstore.service;

import com.plintus.sweetstore.domain.MethodTypes;
import com.plintus.sweetstore.domain.ProductMethods;
import com.plintus.sweetstore.domain.UserOrders;
import com.plintus.sweetstore.repos.MethodTypesRepository;
import com.plintus.sweetstore.repos.ProductMethodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DeliveryService {
    @Autowired
    private MethodTypesRepository MTRep;
    @Autowired
    private ProductMethodsRepository PMRep;

    public Iterable<MethodTypes> getDeliveryTypes() {
        return MTRep.findAll();
    }

    public UserOrders addPrMethodToOrder(UserOrders order, String address, Integer methodID) {
        ProductMethods orderPrMethod = new ProductMethods();
        orderPrMethod.setMethodId(MTRep.findById(methodID).get());
        orderPrMethod.setAddress(address);
        orderPrMethod = PMRep.save(orderPrMethod);
        order.setOrderDate(new Date());
        order.setPrM(orderPrMethod);
        return order;
    }
}
