package com.plintus.sweetstore.domain;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class GoodTypes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String url;

    @OneToMany(mappedBy = "parent",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private Set<GoodSubtypes> subtypes;

    GoodTypes() {

    }

    public GoodTypes(String name, String url) {
        this.name = name;
        this.url = url;
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

    public Set<GoodSubtypes> getSubtypes() {
        return subtypes;
    }

    public void setSubtypes(Set<GoodSubtypes> subtypes) {
        this.subtypes = subtypes;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
