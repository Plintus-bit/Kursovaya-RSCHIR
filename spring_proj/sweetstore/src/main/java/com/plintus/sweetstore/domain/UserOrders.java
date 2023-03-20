package com.plintus.sweetstore.domain;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class UserOrders {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    private User customer;
    private String custPhone;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    private ProductMethods prM;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "id")
    private OrderStatuses status;

    public UserOrders() {
    }

    public UserOrders(Date orderDate, User customer, String custPhone, ProductMethods prM, OrderStatuses status) {
        this.orderDate = orderDate;
        this.customer = customer;
        this.custPhone = custPhone;
        this.prM = prM;
        this.status = status;
    }
    public UserOrders(Date orderDate, User customer, String custPhone, OrderStatuses status) {
        this.orderDate = orderDate;
        this.customer = customer;
        this.custPhone = custPhone;
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

    public String getCustPhone() {
        return custPhone;
    }

    public void setCustPhone(String custPhone) {
        this.custPhone = custPhone;
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
