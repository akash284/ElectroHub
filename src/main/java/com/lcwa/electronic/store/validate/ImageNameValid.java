package com.lcwa.electronic.store.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ImageNameValidator.class)
public @interface ImageNameValid {


    // default message
    String message() default "{ Invalid image Name !! }";

    // represents group of constraints
    Class<?>[] groups() default {};

    // additional information about annotation
    Class<? extends Payload>[] payload() default {};


}
