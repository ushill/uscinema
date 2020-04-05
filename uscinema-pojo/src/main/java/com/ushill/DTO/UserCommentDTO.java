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
public class UserCommentDTO implements Serializable {

    private int id;

    private String comment;

    @JsonProperty("comment_time")
    private String commentTime;

    private BigDecimal score;

    @JsonProperty("movie")
    private MovieDTO movieDTO;

    private static DecimalFormat df = new DecimalFormat("0.0");

    public String getScore() {
        return score == null? null: df.format(score);
    }

    public String getCommentTime() {
        if(commentTime.length() < "2010-01-01 00:00:00".length())
        {
            throw new ServerErrorException(30002);
        }
        return commentTime.substring(0, "2010-01-01 00:00:00".length());
    }
}

