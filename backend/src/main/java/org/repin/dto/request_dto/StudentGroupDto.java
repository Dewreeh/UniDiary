package org.repin.dto.request_dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.repin.model.Faculty;
import org.repin.model.Speciality;

@Data
public class StudentGroupDto {
    @NotBlank(message = "Поле не должно быть пустым!")
    String name;

    @NotNull(message = "Поле не должно быть пустым!")
    Speciality speciality;

    @NotNull(message = "Поле не должно быть пустым!")
    Faculty faculty;

    @NotBlank(message = "Поле не должно быть пустым!")
    String email;
}
