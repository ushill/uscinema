package com.ushill.models;

import javax.persistence.*;
import java.math.BigDecimal;

@Table(name="users")
public class User {
    @Id
    private Integer id;

    private String username;

    @Column(name = "douban_id")
    private String doubanId;

    private String password;

    private String nickname;

    private String tel;

    private String email;

    @Column(name = "image_store_path")
    private String imageStorePath;

    private String place;

    private String title;

    private BigDecimal weight;

    private Integer authority;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "update_time")
    private String updateTime;

    private Boolean status;

    @Column(name = "is_critic")
    private Byte isCritic;

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
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return douban_id
     */
    public String getDoubanId() {
        return doubanId;
    }

    /**
     * @param doubanId
     */
    public void setDoubanId(String doubanId) {
        this.doubanId = doubanId;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return tel
     */
    public String getTel() {
        return tel;
    }

    /**
     * @param tel
     */
    public void setTel(String tel) {
        this.tel = tel;
    }

    /**
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return image_store_path
     */
    public String getImageStorePath() {
        return imageStorePath;
    }

    /**
     * @param imageStorePath
     */
    public void setImageStorePath(String imageStorePath) {
        this.imageStorePath = imageStorePath;
    }

    /**
     * @return place
     */
    public String getPlace() {
        return place;
    }

    /**
     * @param place
     */
    public void setPlace(String place) {
        this.place = place;
    }

    /**
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return weight
     */
    public BigDecimal getWeight() {
        return weight;
    }

    /**
     * @param weight
     */
    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    /**
     * @return authority
     */
    public Integer getAuthority() {
        return authority;
    }

    /**
     * @param authority
     */
    public void setAuthority(Integer authority) {
        this.authority = authority;
    }

    /**
     * @return create_time
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * @return update_time
     */
    public String getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return status
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(Boolean status) {
        this.status = status;
    }

    /**
     * @return is_critic
     */
    public Byte getIsCritic() {
        return isCritic;
    }

    /**
     * @param isCritic
     */
    public void setIsCritic(Byte isCritic) {
        this.isCritic = isCritic;
    }
}