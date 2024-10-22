package com.lcwa.electronic.store.exceptions;

import com.lcwa.electronic.store.dtos.ApiResponseMessage;
import org.apache.coyote.Response;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;


// to handle the exceptions from any controller
@RestControllerAdvice
public class GlobalExceptionHandler {



    Logger logger= LoggerFactory.getLogger(GlobalExceptionHandler.class);
    // handler of resource not found exception
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> resourseNotFoundExceptionHandler(ResourceNotFoundException ex) {


        logger.info("Exception handler invoked !");
        ApiResponseMessage response = ApiResponseMessage
                .builder()
                .message(ex.getMessage())
                .success(true)
                .status(NOT_FOUND)
                .build();

        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    // Handling Exception of Validation api
    // data coming from client may not follow protocols like email aka@dev so it throws exception methodArgumentNotValidException
    // jab hum validation rules ko follow ni krpate tab hume
    // MethodArgumentNotValidException milta hein or bahut bada message milta he pr ye client ko smj ni ayga to make it appropriate hum ise use krege

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){

        List<ObjectError> allErrors=ex.getBindingResult().getAllErrors();
        Map<String,Object>response=new HashMap<>();

        allErrors.stream().forEach( objectError ->{

            String message=objectError.getDefaultMessage();
            String field = ((FieldError) objectError).getField();

            response.put(field,message);
        });
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    // handle bad api request
    @ExceptionHandler(BadApiRequest.class)
    public ResponseEntity<ApiResponseMessage> HandleBadApiRequest(BadApiRequest ex) {


        logger.info("Bad Api request");
        ApiResponseMessage response = ApiResponseMessage
                .builder()
                .message(ex.getMessage())
                .success(false)
                .status(HttpStatus.BAD_REQUEST)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
