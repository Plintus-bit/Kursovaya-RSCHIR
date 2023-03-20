package com.plintus.sweetstore.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;


@Entity
public class Goods {
    @Id
    private Integer article;
    private String name;
    private String descript;
    private Integer cost;
    private String url;
    private Integer count;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "subtypeId", referencedColumnName = "id")
    private GoodSubtypes subtype;

    public Goods() {

    }

    public Goods(Integer article, String name,
                 String descript, String url,
                 Integer count, GoodSubtypes subtype) {
        this.article = article;
        this.name = name;
        this.descript = descript;
        this.url = url;
        this.count = count;
        this.subtype = subtype;
        this.cost = 100;
    }

    public Goods(Integer article, String name,
                 String descript, Integer cost,
                 String url, Integer count,
                 GoodSubtypes subtype) {
        this.article = article;
        this.name = name;
        this.descript = descript;
        this.cost = cost;
        this.url = url;
        this.count = count;
        this.subtype = subtype;
    }

    public Integer getArticle() {
        return article;
    }

    public String getName() {
        return name;
    }

    public String getDescript() {
        return descript;
    }

    public Integer getCost() {
        return cost;
    }

    public String getUrl() {
        return url;
    }

    public void setArticle(Integer article) {
        this.article = article;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public GoodSubtypes getSubtype() {
        return subtype;
    }

    public void setSubtype(GoodSubtypes subtype) {
        this.subtype = subtype;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "article=" + article +
                ", name='" + name + '\'' +
                ", descript='" + descript + '\'' +
                ", cost=" + cost +
                ", url='" + url + '\'' +
                '}';
    }
}
