package com.plintus.sweetstore.domain;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class GoodTypes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer type_id;
    private String name;
    private
    GoodTypes() {

    }

    public GoodTypes(Integer type_id, String name) {
        this.type_id = type_id;
        this.name = name;
    }

    public GoodTypes(String name) {
        this.name = name;
    }

    public Integer getType_id() {
        return type_id;
    }

    public void setType_id(Integer type_id) {
        this.type_id = type_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
