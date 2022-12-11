package com.plintus.sweetstore.domain;

import jakarta.persistence.*;

@Entity
public class Good {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer article;
    private String name;
    private String descript;
    private Integer count;
    private String url;

//    Getters
    public Integer getId() {
        return id;
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

    public Integer getCount() {
        return count;
    }

    public String getUrl() {
        return url;
    }

//    Setters
    public void setId(Integer id) {
        this.id = id;
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

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
