package com.ushill.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ushill.exception.http.ServerErrorException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCommentSummaryDTO implements Serializable {

    @JsonProperty("movie_id")
    private Integer movieId;

    @JsonProperty("movie_title")
    private String movieTitle;

    @JsonProperty("movie_image")
    private String movieImage;

    private BigDecimal score;

    private static DecimalFormat df = new DecimalFormat("0.0");

    public String getScore() {
        return score == null? null: df.format(score);
    }

    public String getMovieImage() {
        if(movieImage == null || movieImage.startsWith("/static")){
            return movieImage;
        }
        return "/static/posters/" + movieImage;
    }
}
