package com.lcwa.electronic.store.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


// annotation k name then jis field par validation krre uska type yahape imagename pr krre jo ki String he
public class ImageNameValidator implements ConstraintValidator<ImageNameValid,String> {


    Logger logger= LoggerFactory.getLogger(ImageNameValidator.class);
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {


        logger.info("Message from isvalid : {}",value);
        // logic for validation

        if(value.isBlank()){
            return false;
        }


        return true;
    }
}
