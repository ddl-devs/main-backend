package br.com.ifrn.ddldevs.pets_backend.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class AgeValidator implements ConstraintValidator<MinAge, LocalDate> {

    @Override
    public void initialize(MinAge constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDate dateOfBirth,
                           ConstraintValidatorContext constraintValidatorContext) {
        if (dateOfBirth == null) {
            return true;
        }

        LocalDate now = LocalDate.now();
        int age = Period.between(dateOfBirth, now).getYears();

        return age >= 13;
    }
}
