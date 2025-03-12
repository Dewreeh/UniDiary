package org.repin.dto;

import lombok.Data;
import org.repin.model.Faculty;

@Data
public class StaffMemberDto {
    private String name;
    private String email;
    private Faculty faculty;
}
