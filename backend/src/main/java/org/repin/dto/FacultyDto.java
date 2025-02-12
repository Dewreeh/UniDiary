package org.repin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class FacultyDto {
    @JsonProperty("Название")
    private String name;
    @JsonProperty("Почта")
    private String address;
    @JsonProperty("Номер телефона")
    private String phoneNumber;
}
