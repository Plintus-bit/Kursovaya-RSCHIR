package com.plintus.sweetstore.domain;

import jakarta.persistence.*;

@Entity
public class IngStructures {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "goodId",
                referencedColumnName = "id")
    private Goods goodId;;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "ingId",
                referencedColumnName = "id")
    private Ingredients ingId;

    public IngStructures() {
    }

    public IngStructures(Goods goodId, Ingredients ingId) {
        this.goodId = goodId;
        this.ingId = ingId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Goods getGoodId() {
        return goodId;
    }

    public void setGoodId(Goods goodId) {
        this.goodId = goodId;
    }

    public Ingredients getIngId() {
        return ingId;
    }

    public void setIngId(Ingredients ingId) {
        this.ingId = ingId;
    }
}
