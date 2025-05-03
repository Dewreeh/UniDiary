package org.repin.dto.request_dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AttendanceDto {

    UUID studentId;

    UUID scheduleItemId;

    Boolean attendanceStatus;

    LocalDateTime timestamp;
}
