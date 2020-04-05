package com.ushill.VO.userComments;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ushill.exception.http.ServerErrorException;
import com.ushill.models.UserComments;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyCommentVO {

    @JsonProperty("is_commented")
    boolean isCommented = true;

    @JsonProperty("my_comment")
    MyCommentVOInfo myCommentVOInfo;

    private static DecimalFormat df = new DecimalFormat("0.0");

    public MyCommentVO(UserComments userComments){
        if(userComments != null) {
            this.myCommentVOInfo = new MyCommentVOInfo();
            BeanUtils.copyProperties(userComments, this.myCommentVOInfo);
        }else{
            this.isCommented = false;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    private class MyCommentVOInfo{
        private int id;

        private String comment;

        @JsonProperty("comment_time")
        private String commentTime;

        private BigDecimal score = new BigDecimal(0);

        public String getScore() {
            return score == null? null: df.format(score);
        }

        public String getCommentTime() {
            if(comment == null){
                return "";
            }
            if(commentTime.length() < "2010-01-01 00:00:00".length())
            {
                throw new ServerErrorException(30002);
            }
            return commentTime.substring(0, "2010-01-01 00:00:00".length());
        }
    }
}
