package com.ushill.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class UserStatDTO implements Serializable {
    private int id;
    private String nickname;
    private String image;
    @JsonProperty(value = "avg_score")
    private BigDecimal avgScore;
    private int total;
    private String title;
    private int shit;
    private int bad;
    private int mixed;
    private int good;
    private int excellent;

    private static DecimalFormat df = new DecimalFormat("0.0");

    public String getAvgScore() {
        return avgScore == null? null: df.format(avgScore);
    }

    public String getImage() {
        if(image != null && image.startsWith("/static")) {
            return image;
        }
        return "/static/user_images/" + image;
    }
}
