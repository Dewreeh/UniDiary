package org.repin.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.repin.model.Faculty;

@Data
public class StaffMemberDto {
    @NotBlank(message = "Поле не должно быть пустым!")
    private String name;
    @NotBlank(message = "Поле не должно быть пустым!")
    private String email;
    @NotNull(message = "Поле не должно быть пустым!")
    private Faculty faculty;
}
