package com.ushill.VO.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author ：五羊
 * @description：TODO
 * @date ：2020/3/29 下午10:47
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCheckVO {
    @JsonProperty(value = "is_valid")
    private boolean isValid;
    @JsonProperty(value = "error")
    private String msg;
}
