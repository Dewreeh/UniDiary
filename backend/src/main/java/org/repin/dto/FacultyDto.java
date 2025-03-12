package org.repin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class FacultyDto {
    private String name;
    private String email;
    private String phoneNumber;
}
