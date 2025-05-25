package org.repin.dto.request_dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.repin.model.StudentGroup;

import java.util.UUID;

@Data
public class StudentDto {
    @NotBlank(message = "Поле не должно быть пустым!")
    String name;
    @NotNull(message = "Поле не должно быть пустым!")
    String email;
    @NotNull(message = "Поле не должно быть пустым!")
    @JsonProperty("studentGroup")
    UUID studentGroup;
}
