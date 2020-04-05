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
public class CommentSummaryDTO implements Serializable {
    private Integer id;

    @JsonProperty("movie_id")
    private Integer movieId;

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("comment_time")
    private String commentTime;

    @JsonProperty("is_critic")
    private Byte isCritic;

    private String comment;
    private Integer votes;
    private String image;
    private String nickname;
    private BigDecimal score;


    private static DecimalFormat df = new DecimalFormat("0.0");

    public String getScore() {
        return score == null? null: df.format(score);
    }

    public String getImage() {
        if(image != null && image.startsWith("/static")){
            return image;
        }
        return "/static/user_images/" + image;
    }

    public String getCommentTime() {
        if(commentTime.length() < "2010-01-01".length()
        || (isCritic == 0 && commentTime.length() < "2010-01-01 00:00:00".length()))
        {
            throw new ServerErrorException(30002);
        }
        return isCritic == 1 ? commentTime.substring(0, "2010-01-01".length()):
                commentTime.substring(0, "2010-01-01 00:00:00".length());
    }

}
