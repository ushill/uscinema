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
 * @description：login/by/email API通过请求参数传入User类属性
 * @date ：2020/3/29 下午9:31
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class UserLoginByEmailDTO {
    @NotBlank
    @Length(min = 6, max = 32, message = "密码长度应限制在6-32之间")
    private String password;

    @Email
    @NotBlank
    @Length(min = 8, max = 64, message = "邮箱长度应限制在8-64之间")
    private String info;
}