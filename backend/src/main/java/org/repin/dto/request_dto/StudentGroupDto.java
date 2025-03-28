package org.repin.dto.request_dto;

import lombok.Data;
import org.repin.model.Faculty;

@Data
public class StudentGroupDto {
    String name;
    String speciality;
    Faculty faculty;
    String email;
}
