package org.repin.dto.response_dto;

import lombok.Data;

@Data
public class ErrorMessageDto {
    String message;
    public ErrorMessageDto(String message){
        this.message = message;
    }
}
