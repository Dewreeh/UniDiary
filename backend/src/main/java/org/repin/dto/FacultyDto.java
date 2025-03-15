package org.repin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
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
