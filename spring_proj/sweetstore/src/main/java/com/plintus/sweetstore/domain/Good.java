package com.plintus.sweetstore.domain;

import jakarta.persistence.*;
import lombok.ToString;
import org.hibernate.annotations.GeneratedColumn;


@Entity
public class Good {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Integer article;
    private String name;
    private String descript;
    private Integer cost;
    private String url;

    public Good(Integer article, String name, String descript, Integer cost, String url){
        this.article = article;
        this.name = name;
        this.descript = descript;
        this.cost = cost;
        this.url = url;
    }

    public Good() {

    }

    //    Getters
    public Long getId() {
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

    public Integer getCost() {
        return cost;
    }

    public String getUrl() {
        return url;
    }


//    Setters

    public void setId(Long id) {
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

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Good{" +
                "id=" + id +
                ", article=" + article +
                ", name='" + name + '\'' +
                ", descript='" + descript + '\'' +
                ", cost=" + cost +
                ", url='" + url + '\'' +
                '}';
    }
}
