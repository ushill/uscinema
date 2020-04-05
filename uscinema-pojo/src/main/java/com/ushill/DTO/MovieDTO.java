package com.ushill.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO implements Serializable {
    private Integer id;

    @JsonProperty("actors_name")
    private String actorsName;

    private Byte bnm;

    private String countrys;

    @JsonProperty("directors_name")
    private String directorsName;

    @JsonProperty("date")
    private String releaseDateCn;

    private String genres;

    private String nickname;

    private String imdb;

    @JsonProperty("image")
    private String posterStorePath;

    @JsonProperty("rating")
    private BigDecimal usccRating;

    @JsonProperty("release_dates")
    private String releaseDates;

    @JsonProperty("screenplayers_name")
    private String screenplayersName;

    private Integer runtime;

    private String title;

    @JsonProperty("year")
    private String firstYear;

    @JsonProperty("user_rating")
    private BigDecimal userRating;

    @JsonProperty("user_rating_count")
    private Integer usccRatingCnt;

    private String summary;

    private static DecimalFormat df = new DecimalFormat("0.0");

    public String getActorsName() {
        return Arrays.stream(actorsName.split("/")).map(String::trim).collect(Collectors.joining(" / "));
    }

    public String getDirectorsName() {
        return Arrays.stream(directorsName.split("/")).map(String::trim).collect(Collectors.joining(" / "));
    }

    public String getScreenplayersName() {
        return Arrays.stream(screenplayersName.split("/")).map(String::trim).collect(Collectors.joining(" / "));
    }

    public String getReleaseDates() {
        return Arrays.stream(releaseDates.split("/")).map(String::trim).collect(Collectors.joining(" / "));
    }

    public String getUsccRating() {
        if(usccRating != null) {
            return df.format(usccRating);
        }
        return null;
    }

//    public void setUsccRating(BigDecimal usccRating) {
//        this.usccRating = usccRating;
//    }

    public String getUserRating() {
        if(userRating != null) {
            return df.format(userRating);
        }
        return null;
    }

//    public void setUserRating(BigDecimal userRating) {
//        this.userRating = userRating;
//    }


    public String getPosterStorePath() {
        if(posterStorePath.startsWith("/static/posters/")){
            return posterStorePath;
        }
        return "/static/posters/" + posterStorePath;
    }
}
