package com.plintus.sweetstore.domain;

import jakarta.persistence.*;

@Entity
public class Nutritions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "goodId",
            referencedColumnName = "id")
    private Goods goodId;
    private Float caloric;
    private Float proteins;
    private Float fats;
    private Float carbhyd;
    private Float diet_fiber;
    private Float water;

    public Nutritions() {
    }

    public Nutritions(Goods goodId, Float caloric, Float proteins, Float fats, Float carbhyd, Float diet_fiber, Float water) {
        this.goodId = goodId;
        this.caloric = caloric;
        this.proteins = proteins;
        this.fats = fats;
        this.carbhyd = carbhyd;
        this.diet_fiber = diet_fiber;
        this.water = water;
    }

    public Goods getGoodId() {
        return goodId;
    }

    public void setGoodId(Goods goodId) {
        this.goodId = goodId;
    }

    public Float getCaloric() {
        return caloric;
    }

    public void setCaloric(Float caloric) {
        this.caloric = caloric;
    }

    public Float getProteins() {
        return proteins;
    }

    public void setProteins(Float proteins) {
        this.proteins = proteins;
    }

    public Float getFats() {
        return fats;
    }

    public void setFats(Float fats) {
        this.fats = fats;
    }

    public Float getCarbhyd() {
        return carbhyd;
    }

    public void setCarbhyd(Float carbhyd) {
        this.carbhyd = carbhyd;
    }

    public Float getDiet_fiber() {
        return diet_fiber;
    }

    public void setDiet_fiber(Float diet_fiber) {
        this.diet_fiber = diet_fiber;
    }

    public Float getWater() {
        return water;
    }

    public void setWater(Float water) {
        this.water = water;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
