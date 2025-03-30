package org.repin.dto.request_dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.repin.model.StudentGroup;

@Data
public class StudentDto {
    @NotBlank(message = "Поле не должно быть пустым!")
    String name;
    @NotBlank(message = "Поле не должно быть пустым!")
    StudentGroup studentGroup;
    @NotBlank(message = "Поле не должно быть пустым!")
    StudentGroup speciality;
    @NotBlank(message = "Поле не должно быть пустым!")
    String password;
}
