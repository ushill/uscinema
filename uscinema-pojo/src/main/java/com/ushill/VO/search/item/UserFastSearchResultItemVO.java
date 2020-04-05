package com.ushill.VO.search.item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ushill.utils.MarkMatchedHtml;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"paraList"})
public class UserFastSearchResultItemVO {
    private Integer id;

    @JsonProperty("nickname_html")
    private String nickname;

    @JsonProperty("image")
    private String imageStorePath;

    @Column(name = "is_critic")
    private Byte isCritic;

    private List<String> paraList;

    public String getImageStorePath() {
        return "/static/user_images/" + imageStorePath;
    }

    public String getNickname() {
        return MarkMatchedHtml.markMatchedHtml(nickname, paraList);
    }
}
