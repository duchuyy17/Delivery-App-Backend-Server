package com.laptrinhjavaweb.news.validator;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = {PhoneNumberValidator.class})
public @interface PhoneNumberConstraint {
    String message() default "{jakarta.validation.constraints.Size.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
