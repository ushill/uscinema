package com.ushill.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckRatingDTO {
    private BigDecimal rating = new BigDecimal(0);
    private int total;
    private int excellent;
    @JsonProperty("movie_id")
    private int movieId;
    @JsonProperty("first_year")
    private int firstYear;

    public BigDecimal getRating()
    {
        return this.rating.multiply(new BigDecimal(2));
    }
}
