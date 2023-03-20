package com.plintus.sweetstore.domain;

import jakarta.persistence.*;

@Entity
public class OrderGoodStructs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "orderId",
            referencedColumnName = "id")
    private UserOrders orderId;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "article",
            referencedColumnName = "article")
    private Goods article;
    private Integer count;

    public OrderGoodStructs() {
    }

    public OrderGoodStructs(UserOrders orderId, Goods article, Integer count) {
        this.orderId = orderId;
        this.article = article;
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

    public Goods getArticle() {
        return article;
    }

    public void setArticle(Goods article) {
        this.article = article;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
