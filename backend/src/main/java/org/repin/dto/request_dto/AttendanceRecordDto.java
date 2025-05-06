package org.repin.dto.request_dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.repin.model.Attendance;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
public class AttendanceRecordDto {
    @JsonProperty("attendanceList")
    private final List<AttendanceDto> attendanceList;

    public AttendanceRecordDto(List<AttendanceDto> attendanceList) {
        this.attendanceList = attendanceList;
    }

}
