package org.repin.dto;

import lombok.Data;

@Data
public class LoginInfoDto {
    private final String email;
    private final String password;
    LoginInfoDto(String email, String password){
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString(){
        return("Пароль для аккаунта {}: {}" + this.email + this.password);
    }
}
