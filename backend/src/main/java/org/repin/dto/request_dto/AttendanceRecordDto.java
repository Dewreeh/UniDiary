package org.repin.dto.request_dto;

import lombok.Data;
import org.repin.model.Attendance;

import java.util.List;

@Data
public class AttendanceRecordDto {
    private final List<Attendance> attendanceList;
}
