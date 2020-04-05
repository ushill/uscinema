package com.ushill.DTO.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
@Constraint(validatedBy = PasswordValidator.class )
public @interface PasswordEqual {
    int min() default 4;

    int max() default 6;

    String message() default "密码不符合规范";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
