package com.ushill.DTO.user;

import com.ushill.DTO.validators.PasswordEqual;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

/**
 * @author ：五羊
 * @description：调用register/by/email API通过请求参数传入User类属性
 * @date ：2020/3/29 下午9:31
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Validated
@PasswordEqual(min = 6, max = 32)
public class UserRegisterByEmailDTO {
    @NotBlank
    @Length(min = 6, max = 24, message = "用户名长度应限制在6-24之间")
    @Pattern(regexp = "^[A-Za-z0-9_\\-.]{6,24}$", message = "用户名格式不符合规范")
    private String username;

    @NotBlank
    @Length(min = 6, max = 32, message = "密码长度应限制在6-32之间")
    private String password;

    @NotBlank
    @Length(min = 6, max = 32, message = "密码长度应限制在6-32之间")
    private String password2;

    @NotBlank
    @Length(min = 1, max = 16, message = "昵称长度应限制在1-16之间")
    private String nickname;
    private String tel;

    @Email
    @NotBlank
    @Length(min = 8, max = 64, message = "邮箱长度应限制在8-64之间")
    private String email;

}