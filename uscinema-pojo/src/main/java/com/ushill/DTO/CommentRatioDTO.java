package com.ushill.DTO;

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
public class CommentRatioDTO implements Serializable {

    private int excellent;
    private int good;
    private int mixed;
    private int bad;
    private int shit;

    private int total;
    private int display;
    private BigDecimal rating;
    private static DecimalFormat df = new DecimalFormat("0.0");

    public String getRating() {
        if(rating != null) {
            return df.format(rating);
        }
        return null;
    }
}
