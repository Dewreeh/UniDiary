package org.repin.dto;

import lombok.Data;
import org.repin.model.Faculty;
import org.repin.model.StudentGroup;

@Data
public class StudentDto {
    String name;
    StudentGroup studentGroup;
    Faculty faculty;
    String password;
}
