package com.atfarm.field.controller.advice;

import com.atfarm.field.controller.dto.ErrorDto;
import com.atfarm.field.controller.dto.ResponseDto;
import com.atfarm.field.exception.InvalidTimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorDto handleBadRequestTimeStampException() {
        return new ErrorDto("Bad Request");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleErrorException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorDto(e.getMessage()));
    }


    @ExceptionHandler(InvalidTimeException.class)
    public ResponseEntity<ResponseDto> handleInvalidTimeStampException(Exception e) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
            .body(new ResponseDto("Invalid time range"));
    }

}
