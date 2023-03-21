package com.plintus.sweetstore.domain;

import jakarta.persistence.*;

@Entity
public class IngStructures {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "article",
            referencedColumnName = "article")
    private Goods article;;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "ingId",
            referencedColumnName = "id")
    private Ingredients ingId;

    public IngStructures() {
    }

    public IngStructures(Goods article, Ingredients ingId) {
        this.article = article;
        this.ingId = ingId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Goods getArticle() {
        return article;
    }

    public void setArticle(Goods article) {
        this.article = article;
    }

    public Ingredients getIngId() {
        return ingId;
    }

    public void setIngId(Ingredients ingId) {
        this.ingId = ingId;
    }
}
