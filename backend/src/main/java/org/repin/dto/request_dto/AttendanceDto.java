package org.repin.dto.request_dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.UUID;

@Data
public class AttendanceDto {

    @JsonProperty("studentId")
    UUID studentId;

    @JsonProperty("attendanceStatus")
    Boolean attendanceStatus;

}
