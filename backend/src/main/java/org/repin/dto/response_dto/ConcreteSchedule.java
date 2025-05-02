package org.repin.dto.response_dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ConcreteSchedule {
    private LocalDate date;
    private List<ScheduleResponseDto> schedulesInfo;
}
