package org.repin.dto.request_dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class LecturerDto {
    @NotBlank(message = "Поле не должно быть пустым!")
    String name;
    @NotNull(message = "Поле не должно быть пустым!")
    String email;
    @NotNull(message = "Поле не должно быть пустым!")
    UUID facultyId;

}