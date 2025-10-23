package com.laptrinhjavaweb.news.validator;

import java.util.Objects;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StrongPassWordValidator implements ConstraintValidator<StrongPassword, String> {
    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).*$";

    @Override
    public void initialize(StrongPassword constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(password)) return true;
        return password.matches(PASSWORD_PATTERN);
    }
}
