package org.repin.dto.response_dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class WeekReportGeneral {
    private LocalDate startDate;
    private LocalDate endDate;
    private List<LocalDate> dates;
    private List<WeekReportGeneralStudent> students;
}
