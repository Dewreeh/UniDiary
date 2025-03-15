package org.repin.dto;

import lombok.Data;

@Data
public class ErrorMessageDto {
    String message;
    public ErrorMessageDto(String message){
        this.message = message;
    }
}
