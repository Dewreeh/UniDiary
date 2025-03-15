package org.repin.controller;

import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    //TODO переделать возвращаемые ошибки под ErrorMessageDto
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Object> SQLExceptionHandler(){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Что-то с БД :(");
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<Object> JSONParseExceptionHandler(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка обработки JSON");
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> MethodArgumentNotValidExceptionHandler(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Поля невалидны");
    }
}
