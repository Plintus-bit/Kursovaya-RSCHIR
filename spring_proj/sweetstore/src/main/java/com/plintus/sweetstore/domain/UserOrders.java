package com.plintus.sweetstore.domain;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class UserOrders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(referencedColumnName = "id")
    private User customer;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(referencedColumnName = "id")
    private ProductMethods prM;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "id")
    private OrderStatuses status;

    public UserOrders() {
    }

    public UserOrders(Date orderDate, User customer, ProductMethods prM, OrderStatuses status) {
        this.orderDate = orderDate;
        this.customer = customer;
        this.prM = prM;
        this.status = status;
    }
    public UserOrders(Date orderDate, User customer, OrderStatuses status) {
        this.orderDate = orderDate;
        this.customer = customer;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public ProductMethods getPrM() {
        return prM;
    }

    public void setPrM(ProductMethods prM) {
        this.prM = prM;
    }

    public OrderStatuses getStatus() {
        return status;
    }

    public void setStatus(OrderStatuses status) {
        this.status = status;
    }
}
