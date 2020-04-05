package com.ushill.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class MovieSummaryDTO implements Serializable {
    @JsonProperty("movie_id")
    private Integer movieId;
    @JsonProperty("movie_image")
    private String movieImage;
    @JsonProperty("movie_title")
    private String movieTitle;
    private BigDecimal score;
    private Integer weight;

    private static DecimalFormat df = new DecimalFormat("0.0");

    public String getScore() {
        return score == null? null: df.format(score);
    }

    public String getMovieImage() {
        if(movieImage != null && movieImage.startsWith("/static/posters/")){
            return movieImage;
        }
        return "/static/posters/" + movieImage;
    }
}
