package org.repin.dto.request_dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.repin.model.Attendance;

import java.util.List;

@Data
public class AttendanceRecordDto {
    @JsonProperty("attendanceList")
    private final List<AttendanceDto> attendanceList;
}
