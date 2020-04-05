package com.ushill.VO.search.item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ushill.enums.RefineSearchType;
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
@JsonIgnoreProperties(value = {"paraList", "searchType"})
public class MovieRefineSearchItemVO {

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

    @JsonProperty("summary")
    private String summary;

    private List<String> paraList;

    private RefineSearchType searchType;

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

    public String getTitle() {
        if(searchType == RefineSearchType.ALL
            ||searchType == RefineSearchType.MOVIES_BY_NAME){
            return MarkMatchedHtml.markMatchedHtml(title, paraList);
        }
        return title;
    }

    public String getNickname() {
        if(searchType == RefineSearchType.ALL
                ||searchType == RefineSearchType.MOVIES_BY_NAME){
            return MarkMatchedHtml.markMatchedHtml(nickname, paraList);
        }
        return nickname;
    }

    public String getActorsName() {
        String actors = String.join(" / ", actorsName.split("/"));
        if(searchType == RefineSearchType.ALL
                ||searchType == RefineSearchType.MOVIES_BY_PEOPLE){
            return MarkMatchedHtml.markMatchedHtml(actors, paraList);
        }
        return actors;
    }

    public String getDirectorsName() {
        String directors = String.join(" / ", directorsName.split("/"));
        if(searchType == RefineSearchType.ALL
                ||searchType == RefineSearchType.MOVIES_BY_PEOPLE){
            return MarkMatchedHtml.markMatchedHtml(directors, paraList);
        }
        return directors;
    }

}
