package com.ushill.DTO.user;

import com.ushill.DTO.validators.PasswordEqual;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author ：五羊
 * @description：login/by/username API通过请求参数传入User类属性
 * @date ：2020/3/29 下午9:31
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class UserLoginByUsernameDTO {
    @NotBlank
    @Length(min = 6, max = 24, message = "用户名长度应限制在6-24之间")
    @Pattern(regexp = "^[A-Za-z0-9_\\-.]{6,24}$", message = "用户名格式不符合规范")
    private String info;

    @NotBlank
    @Length(min = 6, max = 32, message = "密码长度应限制在6-32之间")
    private String password;
}