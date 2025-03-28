package org.repin.dto.request_dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FacultyDto {
    @NotBlank(message = "Поле не должно быть пустым!")
    private String name;
    @NotBlank(message = "Поле не должно быть пустым!")
    private String email;
    @NotBlank(message = "Поле не должно быть пустым!")
    private String phoneNumber;
}
