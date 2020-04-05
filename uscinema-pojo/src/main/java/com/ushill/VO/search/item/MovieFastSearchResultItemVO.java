package com.ushill.VO.search.item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ushill.enums.MovieSearchMatchType;
import com.ushill.utils.MarkMatchedHtml;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"paraList"})
public class MovieFastSearchResultItemVO {

    private Integer id;

    private Byte bnm;

    @JsonProperty("title_html")
    private String title;

    @JsonProperty("nickname_html")
    private String nickname;

    @JsonProperty("directors_html")
    private String directorsName;

    @JsonProperty("actors_html")
    private String actorsName;

    @JsonProperty("image")
    private String posterStorePath;

    @JsonProperty("rating")
    private BigDecimal usccRating;

    @JsonProperty("year")
    private String firstYear;

    private List<String> paraList;

    @JsonProperty("match_type")
    private MovieSearchMatchType matchType;

    private static DecimalFormat df = new DecimalFormat("0.0");

    public String getUsccRating() {
        if(usccRating != null) {
            return df.format(usccRating);
        }
        return null;
    }

    public String getPosterStorePath() {
        return "/static/posters/" + posterStorePath;
    }

    public int getMatchType(){
        return matchType.getMatchType();
    }

    public String getTitle() {
        if(matchType == MovieSearchMatchType.TITLE){
            return MarkMatchedHtml.markMatchedHtml(title, paraList);
        }
        return title;
    }

    public String getNickname() {
        if(matchType == MovieSearchMatchType.NICKNAME){
            return MarkMatchedHtml.markMatchedHtml(nickname, paraList);
        }
        return nickname;
    }

    public String getActorsName() {
        String actors = String.join(" / ", actorsName.split("/"));
        if(matchType == MovieSearchMatchType.ACTORS){
            return MarkMatchedHtml.markMatchedHtml(actors, paraList);
        }
        return actors;
    }

    public String getDirectorsName() {
        String directors = String.join(" / ", directorsName.split("/"));
        if(matchType == MovieSearchMatchType.DIRECTORS){
            return MarkMatchedHtml.markMatchedHtml(directors, paraList);
        }
        return directors;
    }

}
