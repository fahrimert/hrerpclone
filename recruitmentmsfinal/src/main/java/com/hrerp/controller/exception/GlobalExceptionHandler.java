package com.hrerp.jobposting.application.controller.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse> handle(MethodArgumentNotValidException exception) {
        var errors = new HashMap<String,String>();
        exception.getBindingResult().getAllErrors()
                .forEach(error -> {
                    var fieldName = ((FieldError) error).getField();
                    var errorMessage = error.getDefaultMessage();
                    errors.put(fieldName,errorMessage);
                });

    return  ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new CustomErrorResponse(errors));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ValidatingErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex ,
            WebRequest request){
        String errorMessage = "Geçersiz Request Formatı";

        if (ex.getCause() instanceof JsonParseException){
            errorMessage = "Geçersiz JSON formatı";
        }else  if (ex.getCause() instanceof InvalidFormatException){
            InvalidFormatException ife = (InvalidFormatException) ex.getCause();

            errorMessage = String.format("'%s' alanı için geçersiz değer : %s",
                    ife.getPath().get(ife.getPath().size()-1).getFieldName(),
            ife.getValue());
        }else if (ex.getMessage() != null && ex.getMessage().contains("Required request body is missing")){
            errorMessage="Request body eksik";
        }
        Map<String, String> errors = new HashMap<>();

        ValidatingErrorResponse errorResponse = new ValidatingErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                errorMessage,
                request.getDescription(false)
        );

        return  new ResponseEntity< >(errorResponse,HttpStatus.BAD_REQUEST);
    }


}
