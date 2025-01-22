package br.com.ifrn.ddldevs.pets_backend.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = AgeValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface MinAge {
    String message() default "Usuário tem que ter pelo menos 10 anos";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
