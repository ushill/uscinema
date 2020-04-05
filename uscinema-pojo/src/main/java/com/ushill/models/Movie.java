package com.ushill.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


@Table(name="movies")
public class Movie {
    @Id
    private Integer id;

    @Column(name = "douban_id")
    @JsonProperty(value = "douban_id")
    private String doubanId;

    private String title;

    @Column(name = "first_year")
    private String firstYear;

    @Column(name = "release_date_cn")
    private String releaseDateCn;

    @Column(name = "uscc_rating")
    private BigDecimal usccRating;

    @Column(name = "uscc_rating_origin")
    private BigDecimal usccRatingOrigin;

    @Column(name = "uscc_rating_cnt")
    private Integer usccRatingCnt;

    @Column(name = "user_rating")
    private BigDecimal userRating;

    @Column(name = "douban_rating")
    private BigDecimal doubanRating;

    private Byte bnm;

    @Column(name = "db_rate_num")
    private Integer dbRateNum;

    @Column(name = "directors_name")
    private String directorsName;

    @Column(name = "screenplayers_name")
    private String screenplayersName;

    @Column(name = "actors_name")
    private String actorsName;

    private Integer runtime;

    @Column(name = "poster_store_path")
    private String posterStorePath;

    private String genres;

    private String countrys;

    @Column(name = "release_dates")
    private String releaseDates;

    private String nickname;

    private String imdb;

    @Column(name = "directors_id")
    private String directorsId;

    @Column(name = "screenplayers_id")
    private String screenplayersId;

    @Column(name = "actors_id")
    private String actorsId;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "rating_update_time")
    private Date ratingUpdateTime;

    private Boolean status;

    private String summary;

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
     * @return first_year
     */
    public String getFirstYear() {
        return firstYear;
    }

    /**
     * @param firstYear
     */
    public void setFirstYear(String firstYear) {
        this.firstYear = firstYear;
    }

    /**
     * @return release_date_cn
     */
    public String getReleaseDateCn() {
        return releaseDateCn;
    }

    /**
     * @param releaseDateCn
     */
    public void setReleaseDateCn(String releaseDateCn) {
        this.releaseDateCn = releaseDateCn;
    }

    /**
     * @return uscc_rating
     */
    public BigDecimal getUsccRating() {
        return usccRating;
    }

    /**
     * @param usccRating
     */
    public void setUsccRating(BigDecimal usccRating) {
        this.usccRating = usccRating;
    }

    /**
     * @return uscc_rating_origin
     */
    public BigDecimal getUsccRatingOrigin() {
        return usccRatingOrigin;
    }

    /**
     * @param usccRatingOrigin
     */
    public void setUsccRatingOrigin(BigDecimal usccRatingOrigin) {
        this.usccRatingOrigin = usccRatingOrigin;
    }

    /**
     * @return uscc_rating_cnt
     */
    public Integer getUsccRatingCnt() {
        return usccRatingCnt;
    }

    /**
     * @param usccRatingCnt
     */
    public void setUsccRatingCnt(Integer usccRatingCnt) {
        this.usccRatingCnt = usccRatingCnt;
    }

    /**
     * @return user_rating
     */
    public BigDecimal getUserRating() {
        return userRating;
    }

    /**
     * @param userRating
     */
    public void setUserRating(BigDecimal userRating) {
        this.userRating = userRating;
    }

    /**
     * @return douban_rating
     */
    public BigDecimal getDoubanRating() {
        return doubanRating;
    }

    /**
     * @param doubanRating
     */
    public void setDoubanRating(BigDecimal doubanRating) {
        this.doubanRating = doubanRating;
    }

    /**
     * @return bnm
     */
    public Byte getBnm() {
        return bnm;
    }

    /**
     * @param bnm
     */
    public void setBnm(Byte bnm) {
        this.bnm = bnm;
    }

    /**
     * @return db_rate_num
     */
    public Integer getDbRateNum() {
        return dbRateNum;
    }

    /**
     * @param dbRateNum
     */
    public void setDbRateNum(Integer dbRateNum) {
        this.dbRateNum = dbRateNum;
    }

    /**
     * @return directors_name
     */
    public String getDirectorsName() {
        return directorsName;
    }

    /**
     * @param directorsName
     */
    public void setDirectorsName(String directorsName) {
        this.directorsName = directorsName;
    }

    /**
     * @return screenplayers_name
     */
    public String getScreenplayersName() {
        return screenplayersName;
    }

    /**
     * @param screenplayersName
     */
    public void setScreenplayersName(String screenplayersName) {
        this.screenplayersName = screenplayersName;
    }

    /**
     * @return actors_name
     */
    public String getActorsName() {
        return actorsName;
    }

    /**
     * @param actorsName
     */
    public void setActorsName(String actorsName) {
        this.actorsName = actorsName;
    }

    /**
     * @return runtime
     */
    public Integer getRuntime() {
        return runtime;
    }

    /**
     * @param runtime
     */
    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    /**
     * @return poster_store_path
     */
    public String getPosterStorePath() {
        return posterStorePath;
    }

    /**
     * @param posterStorePath
     */
    public void setPosterStorePath(String posterStorePath) {
        this.posterStorePath = posterStorePath;
    }

    /**
     * @return genres
     */
    public String getGenres() {
        return genres;
    }

    /**
     * @param genres
     */
    public void setGenres(String genres) {
        this.genres = genres;
    }

    /**
     * @return countrys
     */
    public String getCountrys() {
        return countrys;
    }

    /**
     * @param countrys
     */
    public void setCountrys(String countrys) {
        this.countrys = countrys;
    }

    /**
     * @return release_dates
     */
    public String getReleaseDates() {
        return releaseDates;
    }

    /**
     * @param releaseDates
     */
    public void setReleaseDates(String releaseDates) {
        this.releaseDates = releaseDates;
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
     * @return imdb
     */
    public String getImdb() {
        return imdb;
    }

    /**
     * @param imdb
     */
    public void setImdb(String imdb) {
        this.imdb = imdb;
    }

    /**
     * @return directors_id
     */
    public String getDirectorsId() {
        return directorsId;
    }

    /**
     * @param directorsId
     */
    public void setDirectorsId(String directorsId) {
        this.directorsId = directorsId;
    }

    /**
     * @return screenplayers_id
     */
    public String getScreenplayersId() {
        return screenplayersId;
    }

    /**
     * @param screenplayersId
     */
    public void setScreenplayersId(String screenplayersId) {
        this.screenplayersId = screenplayersId;
    }

    /**
     * @return actors_id
     */
    public String getActorsId() {
        return actorsId;
    }

    /**
     * @param actorsId
     */
    public void setActorsId(String actorsId) {
        this.actorsId = actorsId;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return rating_update_time
     */
    public Date getRatingUpdateTime() {
        return ratingUpdateTime;
    }

    /**
     * @param ratingUpdateTime
     */
    public void setRatingUpdateTime(Date ratingUpdateTime) {
        this.ratingUpdateTime = ratingUpdateTime;
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
     * @return summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     * @param summary
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }
}
