package com.plintus.sweetstore.domain;

import jakarta.persistence.*;

@Entity
public class ProductMethods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "method_id",  referencedColumnName = "id")
    private MethodTypes methodId;
    private String address;

    public ProductMethods() {
    }

    public ProductMethods(MethodTypes methodId, String address) {
        this.methodId = methodId;
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MethodTypes getMethodId() {
        return methodId;
    }

    public void setMethodId(MethodTypes methodId) {
        this.methodId = methodId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
