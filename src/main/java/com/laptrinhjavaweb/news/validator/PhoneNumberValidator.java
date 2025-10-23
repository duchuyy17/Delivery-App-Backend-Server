package com.laptrinhjavaweb.news.validator;

import java.util.Objects;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumberConstraint, String> {
    private static final String PHONE_NUMBER_PATTERN = "^0\\d{9}$";

    @Override
    public void initialize(PhoneNumberConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(phoneNumber)) return true;
        return phoneNumber.matches(PHONE_NUMBER_PATTERN);
    }
}
