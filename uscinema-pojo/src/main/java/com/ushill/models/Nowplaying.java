package com.ushill.models;

import javax.persistence.*;

public class Nowplaying {
    @Id
    private Integer id;

    @Column(name = "douban_id")
    private Integer doubanId;

    private Integer weight;

//    private Movie movie;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return douban_id
     */
    public Integer getDoubanId() {
        return doubanId;
    }

    /**
     * @param doubanId
     */
    public void setDoubanId(Integer doubanId) {
        this.doubanId = doubanId;
    }

    /**
     * @return weight
     */
    public Integer getWeight() {
        return weight;
    }

    /**
     * @param weight
     */
    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}