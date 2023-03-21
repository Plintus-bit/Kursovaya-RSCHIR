package com.plintus.sweetstore.domain;

import jakarta.persistence.*;

@Entity
public class GoodSubtypes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "parentId", referencedColumnName = "id")
    private GoodTypes parent;

    public GoodSubtypes() {

    }

    public GoodSubtypes(String name, GoodTypes parent) {
        this.name = name;
        this.parent = parent;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
