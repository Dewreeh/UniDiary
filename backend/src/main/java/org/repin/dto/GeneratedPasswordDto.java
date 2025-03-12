package org.repin.dto;

import lombok.Data;

@Data
public class GeneratedPasswordDto {
    String generatedPassword;

    public GeneratedPasswordDto(String generatedPassword) {
        this.generatedPassword = generatedPassword;
    }
}
