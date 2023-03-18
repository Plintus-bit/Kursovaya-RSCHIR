package com.plintus.sweetstore.domain;

import jakarta.persistence.*;

@Entity
public class GoodSubtypes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer subtype_id;
    private String name;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id")
    private GoodTypes parent;

    public GoodSubtypes() {

    }

    public GoodSubtypes(Integer subtype_id, String name, GoodTypes parent) {
        this.subtype_id = subtype_id;
        this.name = name;
        this.parent = parent;
    }

    public GoodSubtypes(String name, GoodTypes parent) {
        this.name = name;
        this.parent = parent;
    }

    public Integer getSubtype_id() {
        return subtype_id;
    }

    public void setSubtype_id(Integer subtype_id) {
        this.subtype_id = subtype_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GoodTypes getParent() {
        return parent;
    }

    public void setParent(GoodTypes parent) {
        this.parent = parent;
    }
}
