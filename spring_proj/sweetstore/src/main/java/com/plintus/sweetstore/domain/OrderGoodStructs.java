package com.plintus.sweetstore.domain;

import jakarta.persistence.*;

@Entity
public class OrderGoodStructs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "orderId",
            referencedColumnName = "id")
    private UserOrders orderId;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "goodId",
            referencedColumnName = "id")
    private Goods goodId;
    private Integer count;

    public OrderGoodStructs() {
    }

    public OrderGoodStructs(UserOrders orderId, Goods goodId, Integer count) {
        this.orderId = orderId;
        this.goodId = goodId;
        this.count = count;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserOrders getOrderId() {
        return orderId;
    }

    public void setOrderId(UserOrders orderId) {
        this.orderId = orderId;
    }

    public Goods getGoodId() {
        return goodId;
    }

    public void setGoodId(Goods article) {
        this.goodId = article;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
