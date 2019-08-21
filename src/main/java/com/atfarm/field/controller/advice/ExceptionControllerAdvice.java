package com.atfarm.field.controller.advice;

import com.atfarm.field.exception.InvalidTimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler(InvalidTimeException.class)
    public void handleInvalidTimeStampException(){

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public void handleBadRequestTimeStampException(){
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleErrorException(Exception e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Some error occured : "+e.getMessage());
    }

}
