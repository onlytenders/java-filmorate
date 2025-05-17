package com.practice.filmorate.validators;

import com.practice.filmorate.annotations.NotEarlierThan;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class NotEarlierThanValidator implements ConstraintValidator<NotEarlierThan, LocalDate> {

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        return !date.isBefore(LocalDate.of(1895, 12, 28));
    }
}
