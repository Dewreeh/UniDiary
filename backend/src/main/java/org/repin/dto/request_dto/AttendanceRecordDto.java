package org.repin.dto.request_dto;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class AttendanceRecordDto {
    private UUID groupId;

    private UUID scheduleItemId;

    private LocalDateTime timestamp;

    @JsonProperty("attendanceList")
    private final List<AttendanceDto> attendanceList;

    public AttendanceRecordDto(List<AttendanceDto> attendanceList) {
        this.attendanceList = attendanceList;
    }

}
