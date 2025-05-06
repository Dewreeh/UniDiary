package org.repin.dto.request_dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AttendanceDto {

    @JsonProperty("studentId")
    UUID studentId;

    @JsonProperty("scheduleItemId")
    UUID scheduleItemId;

    @JsonProperty("attendanceStatus")
    Boolean attendanceStatus;

    @JsonProperty("timestamp")
    LocalDateTime timestamp;
}
