package org.repin.dto.request_dto;

import lombok.Data;
import org.repin.model.Faculty;

import java.util.UUID;

@Data
public class DisciplineDto {
    String name;
    UUID facultyId;
}
