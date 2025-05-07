package org.repin.controller;

import com.fasterxml.jackson.core.JsonParseException;
import jakarta.persistence.EntityNotFoundException;
import org.repin.dto.response_dto.ErrorMessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.security.sasl.AuthenticationException;
import java.sql.SQLException;

@org.springframework.web.bind.annotation.ControllerAdvice
public class GlobalExceptionHandler {

    //TODO переделать возвращаемые ошибки под ErrorMessageDto
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Object> SQLExceptionHandler(){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessageDto("Что-то с БД :("));
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<Object> JSONParseExceptionHandler(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessageDto("Ошибка обработки JSON"));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> AuthExceptionHandler(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorMessageDto("Ошибка аутентификации"));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> UserNotFoundExceptionHandler(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessageDto("Пользователь не найден"));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> EntityNotFoundExceptionHandler(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessageDto("Сущность не найдена"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> IllegalArgumentExceptionHandler(Exception e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessageDto(e.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> IllegalStateExceptionHandler(Exception e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessageDto(e.getMessage()));
    }


}
