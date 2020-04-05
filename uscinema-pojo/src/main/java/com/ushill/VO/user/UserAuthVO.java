package com.ushill.VO.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author ：五羊
 * @description：返回用户注册/登录结果
 * @date ：2020/3/29 下午10:47
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthVO {
    private boolean result;
    private String msg;
}
