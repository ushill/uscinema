package com.ushill.VO.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ushill.exception.http.ServerErrorException;
import com.ushill.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserVO implements Serializable {
    private Integer id;

    private String username;

    private String nickname;

    @JsonProperty(value = "image")
    private String imageStorePath;

    @JsonProperty(value = "create_time")
    private String createTime;

    @JsonProperty(value = "is_critic")
    private Byte isCritic;

    private Integer authority;

    public String getImageStorePath() {
        if(imageStorePath == null){
            imageStorePath = "/static/user_images/default.jpg";
        }
        if(imageStorePath.startsWith("/static")){
            return imageStorePath;
        }
        return "/static/user_images/" + imageStorePath;
    }

    public String getCreateTime() {
        if(createTime.length() < "2010-01-01".length()
                || (isCritic == 0 && createTime.length() < "2010-01-01 00:00:00".length()))
        {
            throw new ServerErrorException(30002);
        }
        return isCritic == 1 ? createTime.substring(0, "2010-01-01".length()):
                createTime.substring(0, "2010-01-01 00:00:00".length());
    }

    public UserVO(User user){
        BeanUtils.copyProperties(user, this);
    }
}