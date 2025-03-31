package org.repin.dto.request_dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.repin.model.StudentGroup;

@Data
public class StudentDto {
    @NotBlank(message = "Поле не должно быть пустым!")
    String name;
    @NotNull(message = "Поле не должно быть пустым!")
    StudentGroup studentGroup;
    @NotBlank(message = "Поле не должно быть пустым!")
    String password;
}
