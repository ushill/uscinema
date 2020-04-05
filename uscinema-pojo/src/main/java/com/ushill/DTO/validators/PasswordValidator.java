/**
 * @作者 7七月
 * @微信公号 林间有风
 * @开源项目 $ http://7yue.pro
 * @免费专栏 $ http://course.7yue.pro
 * @我的课程 $ http://imooc.com/t/4294850
 * @创建时间 2020-01-21 15:34
 */
package com.ushill.DTO.validators;


import com.ushill.DTO.user.UserRegisterByEmailDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordEqual, UserRegisterByEmailDTO> {
    private int min;
    private int max;
    @Override
    public void initialize(PasswordEqual constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(UserRegisterByEmailDTO userRegisterByEmailDTO, ConstraintValidatorContext constraintValidatorContext) {
        String password = userRegisterByEmailDTO.getPassword();
        String password2 = userRegisterByEmailDTO.getPassword2();
        if(password != null && password.equals(password2)){
            return password.length() >= min && password.length() <= max;
        }
        return false;
    }
}
