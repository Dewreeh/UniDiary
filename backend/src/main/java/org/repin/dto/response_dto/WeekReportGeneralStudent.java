package org.repin.dto.response_dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Data
public class WeekReportGeneralStudent {
    private UUID studentId;
    private String studentName;
    private Map<LocalDate, Map<String, Boolean>> attendanceByDate;
}
