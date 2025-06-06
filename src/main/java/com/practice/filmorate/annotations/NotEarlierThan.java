package com.practice.filmorate.annotations;

import com.practice.filmorate.validators.NotEarlierThanValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = NotEarlierThanValidator.class)
@Documented
public @interface NotEarlierThan {

    String message() default "{NotEarlierThan.invalid}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
